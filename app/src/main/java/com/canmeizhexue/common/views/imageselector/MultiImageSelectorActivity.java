package com.canmeizhexue.common.views.imageselector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.canmeizhexue.common.R;

import java.util.ArrayList;

/**
 * <p>图片选择器界面</p>
 *
 * @author canmeizhexue<br/>
 * @version 1.0 (2015-10-23)
 */
public class MultiImageSelectorActivity extends FragmentActivity implements MultiImageSelectorFragment.Callback {

    /**
     * 最大图片选择次数，int类型，默认9
     */
    public static final String EXTRA_SELECT_COUNT = "max_select_count";
    /**
     * 图片选择模式，默认多选
     */
    public static final String EXTRA_SELECT_MODE = "select_count_mode";
    /**
     * 是否显示相机，默认显示
     */
    public static final String EXTRA_SHOW_CAMERA = "show_camera";
    /**
     * 选择结果，返回为 ArrayList&lt;String&gt; 图片路径集合
     */
    public static final String EXTRA_RESULT = "select_result";
    /**
     * 默认选择集
     */
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_list";

    /**
     * 单选
     */
    public static final int MODE_SINGLE = 0;
    /**
     * 多选
     */
    public static final int MODE_MULTI = 1;

    private ArrayList<String> resultList = new ArrayList<>();
    private Button mSubmitButton;
    private int mDefaultCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageselector_activity_multi_image);

        Intent intent = getIntent();
        mDefaultCount = intent.getIntExtra(EXTRA_SELECT_COUNT, 9);
        int mode = intent.getIntExtra(EXTRA_SELECT_MODE, MODE_MULTI);
        boolean isShow = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, true);
        if (mode == MODE_MULTI && intent.hasExtra(EXTRA_DEFAULT_SELECTED_LIST)) {
            resultList = (ArrayList<String>) intent.getSerializableExtra(EXTRA_DEFAULT_SELECTED_LIST);
        }

        Bundle bundle = new Bundle();
        bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_COUNT, mDefaultCount);
        bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_MODE, mode);
        bundle.putBoolean(MultiImageSelectorFragment.EXTRA_SHOW_CAMERA, isShow);
        bundle.putSerializable(MultiImageSelectorFragment.EXTRA_DEFAULT_SELECTED_LIST, resultList);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.image_grid, Fragment.instantiate(this, MultiImageSelectorFragment.class.getName(), bundle))
                .commit();

        // 返回按钮
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        // 完成按钮
        mSubmitButton = (Button) findViewById(R.id.commit);
        if (mode == MODE_MULTI) {
            if (resultList == null || resultList.size() <= 0) {
                mSubmitButton.setText("完成");
                mSubmitButton.setEnabled(false);
            } else {
                mSubmitButton.setText("完成(" + resultList.size() + "/" + mDefaultCount + ")");
                mSubmitButton.setEnabled(true);
            }
            mSubmitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (resultList != null && resultList.size() > 0) {
                        // 返回已选择的图片数据
                        Intent data = new Intent();
                        data.putExtra(EXTRA_RESULT, resultList);
                        setResult(RESULT_OK, data);
                        finish();
                    }
                }
            });
        } else {
            mSubmitButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSingleImageSelected(String image) {
        Intent data = new Intent();
        resultList.add(image);
        data.putExtra(EXTRA_RESULT, resultList);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onCameraShot(String image) {
        Intent data = new Intent();
        resultList.add(image);
        data.putExtra(EXTRA_RESULT, resultList);
        setResult(RESULT_OK, data);
        finish();
    }

    /**
     * 选中图片刷新
     * @param newImages
     */
    @Override
    public void onImagesRefresh(ArrayList<String> newImages) {
        if (newImages == null) {
            return;
        }

        // 刷新数据
        resultList = newImages;

        if (resultList.size() == 0) {
            mSubmitButton.setText("完成");
            mSubmitButton.setEnabled(false);
        } else {
            mSubmitButton.setText(String.format("完成(%d/%d)", resultList.size(), mDefaultCount));
            mSubmitButton.setEnabled(true);
        }
    }
}
