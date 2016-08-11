package com.canmeizhexue.common.views.imageselector;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.ListPopupWindow;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.canmeizhexue.common.R;
import com.canmeizhexue.common.views.imageselector.adapter.FolderAdapter;
import com.canmeizhexue.common.views.imageselector.adapter.ImageGridAdapter;
import com.canmeizhexue.common.views.imageselector.bean.Folder;
import com.canmeizhexue.common.views.imageselector.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;


/**
 * <p>图片选择器fragment</p>
 *
 * @author canmeizhexue<br/>
 * @version 1.0 (2015-11-09)
 */
public class MultiImageSelectorFragment extends Fragment {

    private static final String TAG = "MultiImageSelector";

    /**
     * 最大图片选择次数，int类型
     */
    public static final String EXTRA_SELECT_COUNT = "max_select_count";
    /**
     * 图片选择模式，int类型
     */
    public static final String EXTRA_SELECT_MODE = "select_count_mode";
    /**
     * 是否显示相机，boolean类型
     */
    public static final String EXTRA_SHOW_CAMERA = "show_camera";
    /**
     * 默认选择的数据集
     */
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_result";
    /**
     * 单选
     */
    public static final int MODE_SINGLE = 0;
    /**
     * 多选
     */
    public static final int MODE_MULTI = 1;
    // 不同loader定义
    private static final int LOADER_ALL = 0;
    private static final int LOADER_CATEGORY = 1;
    // 请求加载系统照相机
    private static final int REQUEST_CAMERA = 100;
    // 请求进行浏览模式
    private static final int REQUEST_BROWSER = 200;


    // 结果数据
    private ArrayList<String> resultList = new ArrayList<>();
    // 文件夹数据
    private ArrayList<Folder> mResultFolder = new ArrayList<>();

    // 图片Grid
    private GridView mGridView;
    private Callback mCallback;

    private ImageGridAdapter mImageAdapter;
    private FolderAdapter mFolderAdapter;

    private ListPopupWindow mFolderPopupWindow;

    // 时间线
    private TextView mTimeLineText;
    // 类别
    private TextView mCategoryText;
    // 预览按钮
    private Button mPreviewBtn;
    // 底部View
    private View mPopupAnchorView;

    private int mDesireImageCount;

    private int selectMode;
    private boolean hasFolderGened = false;
    private boolean mIsShowCamera = false;

    private int mGridWidth, mGridHeight;

    private File mTmpFile;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (Callback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("The Activity must implement MultiImageSelectorFragment.Callback interface...");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.imageselector_fragment_multi_image, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 选择图片数量
        mDesireImageCount = getArguments().getInt(EXTRA_SELECT_COUNT);

        // 图片选择模式
        selectMode = getArguments().getInt(EXTRA_SELECT_MODE);

        // 默认选择
        if (selectMode == MODE_MULTI) {
            ArrayList<String> tmp = (ArrayList<String>) getArguments().getSerializable(EXTRA_DEFAULT_SELECTED_LIST);
            if (tmp != null && tmp.size() > 0) {
                resultList = tmp;
            }
        }

        // 是否显示照相机
        mIsShowCamera = getArguments().getBoolean(EXTRA_SHOW_CAMERA, true);
        mImageAdapter = new ImageGridAdapter(getActivity(), mIsShowCamera);
        // 是否显示选择指示器
        mImageAdapter.showSelectIndicator(selectMode == MODE_MULTI);

        mPopupAnchorView = view.findViewById(R.id.footer);

        mTimeLineText = (TextView) view.findViewById(R.id.timeline_area);
        // 初始化，先隐藏当前timeline
        mTimeLineText.setVisibility(View.GONE);

        mCategoryText = (TextView) view.findViewById(R.id.category_btn);
        // 初始化，加载所有图片
        mCategoryText.setText("所有照片");
        mCategoryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mFolderPopupWindow == null) {
                    createPopupFolderList(mGridWidth, mGridHeight);
                }

                if (mFolderPopupWindow.isShowing()) {
                    mFolderPopupWindow.dismiss();
                } else {
                    mFolderPopupWindow.show();
                    int index = mFolderAdapter.getSelectIndex();
                    index = index == 0 ? index : index - 1;
                    mFolderPopupWindow.getListView().setSelection(index);
                }
            }
        });

        mPreviewBtn = (Button) view.findViewById(R.id.preview);
        // 初始化，按钮状态初始化
        if (resultList == null || resultList.size() <= 0) {
            mPreviewBtn.setText("预览");
            mPreviewBtn.setEnabled(false);
        }
        mPreviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 预览
            }
        });

        mGridView = (GridView) view.findViewById(R.id.grid);
       /* mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int state) {
                if (state == SCROLL_STATE_IDLE) {
                    // 停止滑动，日期指示器消失
                    mTimeLineText.setVisibility(View.GONE);
                } else if (state == SCROLL_STATE_FLING) {
                    mTimeLineText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mTimeLineText.getVisibility() == View.VISIBLE) {
                    int index = firstVisibleItem + 1 == view.getAdapter().getCount() ? view.getAdapter().getCount() - 1 : firstVisibleItem + 1;
                    Image image = (Image) view.getAdapter().getItem(index);
                    if (image != null) {
                        mTimeLineText.setText(TimeUtils.formatPhotoDate(image.path));
                    }
                }
            }
        });*/
        mGridView.setAdapter(mImageAdapter);
        mGridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            public void onGlobalLayout() {

                final int width = mGridView.getWidth();
                final int height = mGridView.getHeight();

                mGridWidth = width;
                mGridHeight = height;

                final int desireSize = getResources().getDimensionPixelOffset(R.dimen.image_size);
                final int numCount = width / desireSize;
                final int columnSpace = getResources().getDimensionPixelOffset(R.dimen.space_size);
                int columnWidth = (width - columnSpace * (numCount - 1)) / numCount;
                mGridView.setNumColumns(numCount);
                mImageAdapter.setItemSize(columnWidth);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    mGridView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mImageAdapter.isShowCamera()) {
                    // 如果显示照相机，则第一个Grid显示为照相机，处理特殊逻辑
                    if (i == 0) {
                        showCameraAction();
                    } else {
                        // 正常操作
                        String image = (String) adapterView.getAdapter().getItem(i);
                        onImageSelected(image, i - 1); // 排除第一项
                    }
                } else {
                    // 正常操作
                    String image = (String) adapterView.getAdapter().getItem(i);
                    onImageSelected(image, i);
                }
            }
        });
        mFolderAdapter = new FolderAdapter(getActivity());

        // 设置选择监听器
        if (selectMode == MODE_MULTI) {
            mImageAdapter.setOnItemCheckListener(new ImageGridAdapter.OnItemCheckListener() {
                @Override
                public void onItemchecked(String image) {
                    onImageChecked(image);
                }
            });
        }
    }

    /**
     * 创建弹出的ListView
     */
    private void createPopupFolderList(int width, int height) {
        mFolderPopupWindow = new ListPopupWindow(getActivity());
        mFolderPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mFolderPopupWindow.setAdapter(mFolderAdapter);
        mFolderPopupWindow.setContentWidth(width);
        mFolderPopupWindow.setWidth(width);
        mFolderPopupWindow.setHeight(height * 5 / 8);
        mFolderPopupWindow.setAnchorView(mPopupAnchorView);
        mFolderPopupWindow.setModal(true);
        mFolderPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                mFolderAdapter.setSelectIndex(i);

                final int index = i;
                final AdapterView v = adapterView;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mFolderPopupWindow.dismiss();

                        if (index == 0) {
                            getActivity().getSupportLoaderManager().restartLoader(LOADER_ALL, null, mLoaderCallback);
                            mCategoryText.setText("所有照片");
                            if (mIsShowCamera) {
                                mImageAdapter.setShowCamera(true);
                            } else {
                                mImageAdapter.setShowCamera(false);
                            }
                            mImageAdapter.notifyDataSetChanged();
                        } else {
                            Folder folder = (Folder) v.getAdapter().getItem(index);
                            if (null != folder) {
                                mCategoryText.setText(folder.getName());
                                mImageAdapter.setData(folder.getImages());
                                // 设定默认选择
                                if (resultList != null && resultList.size() > 0) {
                                    mImageAdapter.setSelectedImages(resultList);
                                }
                            }
                            mImageAdapter.setShowCamera(false);
                            mImageAdapter.notifyDataSetChanged();
                        }

                        // 滑动到最初始位置
                        mGridView.smoothScrollToPosition(0);
                    }
                }, 100);

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 首次加载所有图片
        //new LoadImageTask().execute();
        getActivity().getSupportLoaderManager().initLoader(LOADER_ALL, null, mLoaderCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 相机拍照完成后，返回图片路径
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                if (mTmpFile != null) {
                    if (mCallback != null) {
                        mCallback.onCameraShot(mTmpFile.getAbsolutePath());
                    }
                }
            } else {
                if (mTmpFile != null && mTmpFile.exists()) {
                    mTmpFile.delete();
                }
            }
        } else if (requestCode == REQUEST_BROWSER) {
            if (resultCode == Activity.RESULT_OK) {
                ArrayList<String> selectedImages = (ArrayList<String>) data.getSerializableExtra(MultiImageBrowserActivity.EXTRA_RESULT);
                refreshSelectedImages(selectedImages);
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "on change");

        if (mFolderPopupWindow != null) {
            if (mFolderPopupWindow.isShowing()) {
                mFolderPopupWindow.dismiss();
            }
        }

        mGridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            public void onGlobalLayout() {

                final int height = mGridView.getHeight();

                final int desireSize = getResources().getDimensionPixelOffset(R.dimen.image_size);
                Log.d(TAG, "Desire Size = " + desireSize);
                final int numCount = mGridView.getWidth() / desireSize;
                Log.d(TAG, "Grid Size = " + mGridView.getWidth());
                Log.d(TAG, "num count = " + numCount);
                final int columnSpace = getResources().getDimensionPixelOffset(R.dimen.space_size);
                int columnWidth = (mGridView.getWidth() - columnSpace * (numCount - 1)) / numCount;
                mImageAdapter.setItemSize(columnWidth);

                if (mFolderPopupWindow != null) {
                    mFolderPopupWindow.setHeight(height * 5 / 8);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    mGridView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });

        super.onConfigurationChanged(newConfig);

    }

    /**
     * 选择相机
     */
    private void showCameraAction() {
        // 跳转到系统照相机
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // 设置系统相机拍照后的输出路径
            // 创建临时文件
            mTmpFile = FileUtils.createTmpFile(getActivity());
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        } else {
            Toast.makeText(getActivity(), "没有系统相机", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 进入浏览模式
     */
    private void showBrowserAction(int position) {
        Intent browserIntent = new Intent(getActivity(), MultiImageBrowserActivity.class);
        browserIntent.putExtra(MultiImageBrowserActivity.EXTRA_BROWSER_TYPE, MultiImageBrowserActivity.BROWSER_TYPE_SELECTOR);
        browserIntent.putExtra(MultiImageBrowserActivity.EXTRA_IMAGES, mImageAdapter.getData());
        browserIntent.putExtra(MultiImageBrowserActivity.EXTRA_CUR_POSITION, position);
        if (selectMode == MODE_SINGLE) {
            // 单选
            browserIntent.putExtra(MultiImageBrowserActivity.EXTRA_SELECT_MODE, MultiImageBrowserActivity.SELECT_MODE_SINGLE);
        } else {
            // 多选
            browserIntent.putExtra(MultiImageBrowserActivity.EXTRA_SELECT_MODE, MultiImageBrowserActivity.SELECT_MODE_MULTI);
            browserIntent.putExtra(MultiImageBrowserActivity.EXTRA_MAX_COUNT, mDesireImageCount);
            browserIntent.putExtra(MultiImageBrowserActivity.EXTRA_SELECTED_IMAGES, resultList);
        }
        startActivityForResult(browserIntent, REQUEST_BROWSER);
    }

    /**
     * 处理图片选择
     * @param image
     */
    private void onImageSelected(String image, int position) {
        if (image != null) {
            if (selectMode == MODE_MULTI) {
                // 多选模式
                showBrowserAction(position);
            } else if (selectMode == MODE_SINGLE) {
                // 单选模式
                if (mCallback != null) {
                    mCallback.onSingleImageSelected(image);
                }
            }
        }
    }

    /**
     * 选择图片操作
     *
     * @param image
     */
    private void onImageChecked(String image) {
        if (image != null) {
            // 多选模式
            if (selectMode == MODE_MULTI) {
                if (resultList.contains(image)) {
                    resultList.remove(image);
                } else {
                    // 判断选择数量问题
                    if (mDesireImageCount == resultList.size()) {
                        Toast.makeText(getActivity(), "已经达到最高选择数量", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    resultList.add(image);
                }
                if (mCallback != null) {
                    mCallback.onImagesRefresh(resultList);
                }
                mImageAdapter.setSelectedImages(resultList);
                mImageAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 处理图片浏览回调数据
     */
    private void refreshSelectedImages(ArrayList<String> selectedImags) {
        if (selectedImags == null) {
            return;
        }

        // 刷新选中图片
        if (selectMode == MODE_MULTI) {
            resultList = selectedImags;
            if (mCallback != null) {
                mCallback.onImagesRefresh(resultList);
            }
            mImageAdapter.setSelectedImages(resultList);
            mImageAdapter.notifyDataSetChanged();
        }
    }

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media._ID};

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == LOADER_ALL) {
                CursorLoader cursorLoader = new CursorLoader(getActivity(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        null, null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            } else if (id == LOADER_CATEGORY) {
                CursorLoader cursorLoader = new CursorLoader(getActivity(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        IMAGE_PROJECTION[0] + " like '%" + args.getString("path") + "%'", null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                ArrayList<String> images = new ArrayList<>();
                int count = data.getCount();
                if (count > 0) {
                    data.moveToFirst();
                    do {
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
//                        String imagePath = "file://" + path;
                        String imagePath = path;
                        images.add(imagePath);
                        if (!hasFolderGened) {
                            // 获取文件夹名称
                            File imageFile = new File(path);
                            File folderFile = imageFile.getParentFile();
                            Folder folder = new Folder();
                            folder.setName(folderFile.getName());
                            folder.setPath(folderFile.getAbsolutePath());
                            folder.setCover(imagePath);
                            if (!mResultFolder.contains(folder)) {
                                ArrayList<String> imageList = new ArrayList<>();
                                imageList.add(imagePath);
                                folder.setImages(imageList);
                                mResultFolder.add(folder);
                            } else {
                                // 更新
                                Folder f = mResultFolder.get(mResultFolder.indexOf(folder));
                                f.getImages().add(imagePath);
                            }
                        }

                    } while (data.moveToNext());

                    // 设定默认选择
                    if (resultList != null && resultList.size() > 0) {
                        mImageAdapter.setSelectedImages(resultList);
                    }
                    mImageAdapter.setData(images);
                    mImageAdapter.notifyDataSetChanged();

                    mFolderAdapter.setData(mResultFolder);
                    hasFolderGened = true;

                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    /**
     * 回调接口
     */
    public interface Callback {
        void onSingleImageSelected(String image);

        void onCameraShot(String image);

        void onImagesRefresh(ArrayList<String> newImages);
    }
}
