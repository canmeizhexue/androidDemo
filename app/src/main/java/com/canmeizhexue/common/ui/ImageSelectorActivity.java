package com.canmeizhexue.common.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.canmeizhexue.common.R;
import com.canmeizhexue.common.base.BaseActivity;
import com.canmeizhexue.common.views.imageselector.MultiImageSelectorActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * <p>图片选择器使用示例</p>
 *
 * @author canmeizhexue<br/>
 * @version 1.0 (2015-10-23)
 */
public class ImageSelectorActivity extends BaseActivity {

    private static final int REQUEST_IMAGE = 2;
    @Bind(R.id.single)
    RadioButton single;
    @Bind(R.id.multi)
    RadioButton multi;
    @Bind(R.id.choice_mode)
    RadioGroup choiceMode;
    @Bind(R.id.request_num)
    EditText requestNum;
    @Bind(R.id.show)
    RadioButton show;
    @Bind(R.id.no_show)
    RadioButton noShow;
    @Bind(R.id.show_camera)
    RadioGroup showCamera;
    @Bind(R.id.button)
    Button button;
    @Bind(R.id.result)
    TextView result;

    private ArrayList<String> mSelectImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_selector);
        ButterKnife.bind(this);

        choiceMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.multi) {
                    requestNum.setEnabled(true);
                } else {
                    requestNum.setEnabled(false);
                    requestNum.setText("");
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedMode = MultiImageSelectorActivity.MODE_MULTI;

                if (choiceMode.getCheckedRadioButtonId() == R.id.single) {
                    selectedMode = MultiImageSelectorActivity.MODE_SINGLE;
                } else {
                    selectedMode = MultiImageSelectorActivity.MODE_MULTI;
                }

                boolean isshowCamera = showCamera.getCheckedRadioButtonId() == R.id.show;

                int maxNum = 9;
                if (!TextUtils.isEmpty(requestNum.getText())) {
                    maxNum = Integer.valueOf(requestNum.getText().toString());
                }

                Intent intent = new Intent(ImageSelectorActivity.this, MultiImageSelectorActivity.class);
                // 是否显示拍摄图片
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, isshowCamera);
                // 最大可选择图片数量
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxNum);
                // 选择模式
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, selectedMode);
                // 默认选择
                if (mSelectImages != null && mSelectImages.size() > 0) {
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectImages);
                }
                startActivityForResult(intent, REQUEST_IMAGE);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                mSelectImages = (ArrayList<String>) data.getSerializableExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                StringBuilder sb = new StringBuilder();
                for (String image : mSelectImages) {
                    sb.append(image);
                    sb.append("\n");
                }
                result.setText(sb.toString());
            }
        }
    }
}
