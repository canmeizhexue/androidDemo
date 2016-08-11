package com.canmeizhexue.common.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.canmeizhexue.common.R;
import com.canmeizhexue.common.base.BaseActivity;
import com.canmeizhexue.common.views.imageselector.MultiImageBrowserActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * <p>图片浏览器使用示例</p>
 *
 * @author canmeizhexue<br/>
 * @version 1.0 (2015-10-23)
 */
public class ImageBrowserActivity extends BaseActivity {
    @Bind(R.id.btn_preview)
    Button btnPreview;
    @Bind(R.id.btn_single_select)
    Button btnSingleSelect;
    @Bind(R.id.btn_mulity_select)
    Button btnMulitySelect;
    @Bind(R.id.result)
    TextView result;

    private static final int REQUEST_BROWSER = 1;
    private ArrayList<String> images = new ArrayList<>();
    private ArrayList<String> mSelectImages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_browser);
        ButterKnife.bind(this);

        // 浏览
        btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(ImageBrowserActivity.this, MultiImageBrowserActivity.class);
                browserIntent.putExtra(MultiImageBrowserActivity.EXTRA_BROWSER_TYPE, MultiImageBrowserActivity.BROWSER_TYPE_PREVIEW);
                browserIntent.putExtra(MultiImageBrowserActivity.EXTRA_IMAGES, images);
                browserIntent.putExtra(MultiImageBrowserActivity.EXTRA_CUR_POSITION, 0);
                startActivity(browserIntent);
            }
        });

        // 单选
        btnSingleSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(ImageBrowserActivity.this, MultiImageBrowserActivity.class);
                browserIntent.putExtra(MultiImageBrowserActivity.EXTRA_BROWSER_TYPE, MultiImageBrowserActivity.BROWSER_TYPE_SELECTOR);
                browserIntent.putExtra(MultiImageBrowserActivity.EXTRA_SELECT_MODE, MultiImageBrowserActivity.SELECT_MODE_SINGLE);
                browserIntent.putExtra(MultiImageBrowserActivity.EXTRA_IMAGES, images);
                browserIntent.putExtra(MultiImageBrowserActivity.EXTRA_CUR_POSITION, 5);
                startActivityForResult(browserIntent, REQUEST_BROWSER);
            }
        });

        initImagas();

        // 多选
        btnMulitySelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(ImageBrowserActivity.this, MultiImageBrowserActivity.class);
                browserIntent.putExtra(MultiImageBrowserActivity.EXTRA_BROWSER_TYPE, MultiImageBrowserActivity.BROWSER_TYPE_SELECTOR);
                browserIntent.putExtra(MultiImageBrowserActivity.EXTRA_SELECT_MODE, MultiImageBrowserActivity.SELECT_MODE_MULTI);
                browserIntent.putExtra(MultiImageBrowserActivity.EXTRA_IMAGES, images);
                browserIntent.putExtra(MultiImageBrowserActivity.EXTRA_CUR_POSITION, 1);
                browserIntent.putExtra(MultiImageBrowserActivity.EXTRA_MAX_COUNT, 12); //最多9张
                browserIntent.putExtra(MultiImageBrowserActivity.EXTRA_SELECTED_IMAGES, mSelectImages);
                startActivityForResult(browserIntent, REQUEST_BROWSER);
            }
        });


    }

    private void initImagas() {
        images.add("http://ht-img1.evergrande.com/group1/M00/03/8B/CjMAyVdWaF6AYBUjAAefIYrx_0U286.jpg");
        images.add("http://d.hiphotos.baidu.com/image/pic/item/6609c93d70cf3bc7fceda3ecd300baa1cc112ac4.jpg");
        images.add("http://imgt8.bdstatic.com/it/u=2,979988579&fm=25&gp=0.jpg");
        images.add("http://imgt6.bdstatic.com/it/u=2,830014915&fm=25&gp=0.jpg");
        images.add("http://h.hiphotos.baidu.com/image/pic/item/a5c27d1ed21b0ef464cffe5edfc451da81cb3e8e.jpg");
        images.add("http://b.hiphotos.baidu.com/image/w%3D230/sign=e0418ae9952bd40742c7d4fe4b889e9c/b21bb051f8198618df1ffb4548ed2e738ad4e693.jpg");
        images.add("http://a.hiphotos.baidu.com/image/w%3D230/sign=2bb9f3c39045d688a302b5a794c37dab/79f0f736afc37931e211bcd9e9c4b74542a911c0.jpg");
        images.add("http://img4.imgtn.bdimg.com/it/u=161688991,2544189153&fm=21&gp=0.jpg");
        images.add("http://img3.imgtn.bdimg.com/it/u=2183645777,3481302914&fm=21&gp=0.jpg");
        images.add("http://img0.imgtn.bdimg.com/it/u=2464668041,84597583&fm=21&gp=0.jpg");
        images.add("http://img2.imgtn.bdimg.com/it/u=1697768607,2718412287&fm=21&gp=0.jpg");
        images.add("http://img3.imgtn.bdimg.com/it/u=3658520503,255274304&fm=21&gp=0.jpg");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_BROWSER) {
            if (resultCode == RESULT_OK) {
                mSelectImages = (ArrayList<String>) data.getSerializableExtra(MultiImageBrowserActivity.EXTRA_RESULT);
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
