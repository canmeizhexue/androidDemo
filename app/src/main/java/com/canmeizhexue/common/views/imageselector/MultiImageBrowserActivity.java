package com.canmeizhexue.common.views.imageselector;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.canmeizhexue.common.R;
import com.canmeizhexue.common.utils.FileUtils;
import com.canmeizhexue.common.views.ActionSheetDialogBuilder;
import com.canmeizhexue.common.views.imageselector.adapter.BrowserAdapter;
import com.canmeizhexue.common.views.imageselector.view.HackyViewPager;

import java.util.ArrayList;

/**
 * <p>图片浏览器</p>
 * <br/>提供两种模式:
 * <br/>1.浏览模式
 * <br/>2.浏览+选择模式
 *
 * @author canmeizhexue<br/>
 * @version 1.0 (2015-11-09)
 */
public class MultiImageBrowserActivity extends FragmentActivity implements BrowserAdapter.OnPaperSelectListener, View.OnClickListener, ViewPager.OnPageChangeListener {
    /**浏览类型，分浏览和选择两种*/
    public final static String EXTRA_BROWSER_TYPE = "browser_type";
    /**选择模式，分单选和多选两种*/
    public static final String EXTRA_SELECT_MODE = "select_mode";
    /**浏览图片集合*/
    public final static String EXTRA_IMAGES = "images";
    /**已选择图片集合*/
    public final static String EXTRA_SELECTED_IMAGES = "selected_images";
    /**最大选择数*/
    public static final String EXTRA_MAX_COUNT = "max_select_count";
    /**已选择图片结果集*/
    public static final String EXTRA_RESULT = "selected_result";
    /**当前选择图片在图片集合中的索引*/
    public static final String EXTRA_CUR_POSITION = "current_position";

    /**浏览类型之浏览*/
    public final static int BROWSER_TYPE_PREVIEW = 1;
    /**浏览类型之选择*/
    public final static int BROWSER_TYPE_SELECTOR = 2;
    /**选择模式之单选*/
    public static final int SELECT_MODE_SINGLE = 3;
    /**选择模式之多选*/
    public static final int SELECT_MODE_MULTI = 4;

    /**界面标题布局*/
    private RelativeLayout layoutTitle;
    /**返回按钮*/
    private ImageView btnBack;
    /**完成提交按钮*/
    private Button btnCommit;
    /**浏览界面viewpager*/
    private HackyViewPager viewpager;
    /**浏览模式的索引提示*/
    private TextView textviewIndicator;
    /**浏览器适配器*/
    private BrowserAdapter paperAdapter;

    /**图片集合*/
    private ArrayList<String> images = null;
    /**已选图片集合*/
    private ArrayList<String> selectedImags;
    /**浏览类型标识*/
    private int browserType;
    /**浏览模式标识*/
    private int selectMode;
    /**最大选择数*/
    private int maxCount;
    /**当前选择图片在图片集合中的索引*/
    private int curPosition = 0;
    /**
     * 长按弹出的对话框
     */
    private ActionSheetDialogBuilder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagebrowser_activity_multi_image);
        initView();
    }

    /**
     * 相关控件初始化
     */
    private void initView() {
        layoutTitle = (RelativeLayout) findViewById(R.id.layout_title);
        btnBack = (ImageView) findViewById(R.id.btn_back);
        btnCommit = (Button) findViewById(R.id.commit);
        viewpager = (HackyViewPager) findViewById(R.id.viewpager);
        textviewIndicator = (TextView) findViewById(R.id.textview_indicator);

        Intent intent = getIntent();
        browserType = intent.getIntExtra(EXTRA_BROWSER_TYPE, 1);
        curPosition = intent.getIntExtra(EXTRA_CUR_POSITION, 0);
        images = (ArrayList<String>) intent.getSerializableExtra(EXTRA_IMAGES);
        if (images == null || images.size() == 0) {
            finish();
            return;
        }

        paperAdapter = new BrowserAdapter(this, browserType);
        paperAdapter.setData(images);

        if (browserType == BROWSER_TYPE_PREVIEW) {
            // 浏览模式
            layoutTitle.setVisibility(View.GONE);
            if(images.size() > 1) {
                textviewIndicator.setText(String.format("%d/%d", curPosition + 1, images.size()));
                textviewIndicator.setVisibility(View.VISIBLE);
            } else {
                textviewIndicator.setVisibility(View.GONE);
            }
            viewpager.setOnPageChangeListener(this);
        } else if (browserType == BROWSER_TYPE_SELECTOR) {
            // 选择模式
            layoutTitle.setVisibility(View.VISIBLE);
            textviewIndicator.setVisibility(View.GONE);

            selectMode = intent.getIntExtra(EXTRA_SELECT_MODE, 3);
            if (selectMode == SELECT_MODE_SINGLE) {
                // 单选模式
                btnCommit.setVisibility(View.GONE);
                paperAdapter.setSelectMode(SELECT_MODE_SINGLE);
            } else if (selectMode == SELECT_MODE_MULTI) {
                // 多选模式
                maxCount = intent.getIntExtra(EXTRA_MAX_COUNT, 9);
                selectedImags = (ArrayList<String>) intent.getSerializableExtra(EXTRA_SELECTED_IMAGES);

                paperAdapter.setMaxCount(maxCount);
                paperAdapter.setSelectedImages(selectedImags);
                paperAdapter.setSelectMode(SELECT_MODE_MULTI);

                btnCommit.setVisibility(View.VISIBLE);
                btnCommit.setOnClickListener(this);
                setSelectStatus();
            }

            if (selectedImags == null) {
                selectedImags = new ArrayList<>();
            }

            btnBack.setOnClickListener(this);
        }

        // 限制屏幕最多预加载两个viewpager
        viewpager.setOffscreenPageLimit(2);
        viewpager.setAdapter(paperAdapter);
        paperAdapter.setOnselectListener(this);
        paperAdapter.notifyDataSetChanged();
        viewpager.setCurrentItem(curPosition, true);

    }

    @Override
    public void onSelected(String image) {
        if (browserType == BROWSER_TYPE_SELECTOR) {
            if (selectMode == SELECT_MODE_SINGLE) {
                if (!selectedImags.contains(image)) {
                    selectedImags.add(image);
                }
                finishForResult();
            } else if (selectMode == SELECT_MODE_MULTI) {
                if (selectedImags.size() >= maxCount) {
                    Toast.makeText(this, "已经达到最高选择数量", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!selectedImags.contains(image)) {
                    selectedImags.add(image);
                }
                paperAdapter.setSelectedImages(selectedImags);
                setSelectStatus();
            }
        }
    }

    @Override
    public void onUnselected(String image) {
        if (browserType == BROWSER_TYPE_SELECTOR) {
           if (selectMode == SELECT_MODE_MULTI) {
                if (selectedImags.contains(image)) {
                    selectedImags.remove(image);
                    paperAdapter.setSelectedImages(selectedImags);
                    setSelectStatus();
                }
            }
        }
    }

    @Override
    public void onClicked() {
        if (browserType == BROWSER_TYPE_PREVIEW) {
            finish();
        }
    }

    @Override
    public void onLongClicked(final int position) {
        if(browserType==BROWSER_TYPE_PREVIEW){
            if(dialog==null){
                dialog = new ActionSheetDialogBuilder(this);
                String operation = "保存图片";
                String cancel = "取消";
                dialog.setButtons(new String[]{operation,cancel},false, new ActionSheetDialogBuilder.ActionSheetDialogOnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int i) {
                        ArrayList<String> imgs = images;
                        if(i==0 && imgs!=null && imgs.size()>0 && position<imgs.size()){
                            String img = imgs.get(position);
                            if(img!=null && !TextUtils.isEmpty(img)){
                                new DownloadFileTask(img).execute();
                            }

                        }
                    }
                });

            }
            dialog.create().show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.commit) {
            finishForResult();
            return;
        }
    }

    /**
     * 设置提交按钮状态
     */
    private void setSelectStatus() {
        if (selectedImags == null || selectedImags.size() <= 0) {
            btnCommit.setText("完成");
            btnCommit.setEnabled(false);
        } else {
            btnCommit.setText(String.format("完成(%d/%d)", selectedImags.size(), maxCount));
            btnCommit.setEnabled(true);
        }
    }

    /**
     * 返回选择结果
     */
    private void finishForResult() {
        Intent data = new Intent();
        data.putExtra(EXTRA_RESULT, selectedImags);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (browserType == BROWSER_TYPE_PREVIEW) {
            textviewIndicator.setText(String.format("%d/%d", position + 1, images.size()));
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    /**
     * 下载图片
     */
    class DownloadFileTask extends AsyncTask<Void,Void,String> {
        String fileUrl;

        /**
         * 构造函数
         * @param fileUrl 文件网络地址
         */
        public DownloadFileTask(String fileUrl) {
            this.fileUrl = fileUrl;
        }

        @Override
        protected String doInBackground(Void... params) {
            String filePath = FileUtils.downloadImageFile(fileUrl);
/*
            try{
                String title =  fileUrl.substring(fileUrl.lastIndexOf(File.pathSeparator));
//                MediaStore.Images.Media.insertImage(getContentResolver(), filePath, title, title);
            }catch (Exception e){
                e.printStackTrace();
            }
*/
            return filePath;
        }

        @Override
        protected void onPostExecute(String filePath) {
            if(!TextUtils.isEmpty(filePath)){
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + filePath)));
                Toast.makeText(MultiImageBrowserActivity.this,"图片已经成功下载",Toast.LENGTH_LONG);
//                Toast.showToast(MultiImageBrowserActivity.this, getString(R.string.ht_download_file_successfully));
            }else{
                Toast.makeText(MultiImageBrowserActivity.this,"图片下载失败",Toast.LENGTH_LONG);
            }

        }
    }
}
