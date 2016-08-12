package com.canmeizhexue.common.ui.viewflowdemo;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.canmeizhexue.common.R;
import com.canmeizhexue.common.base.BaseActivity;
import com.canmeizhexue.common.views.viewflow.CircleFlowIndicator;
import com.canmeizhexue.common.views.viewflow.ViewFlow;

import java.util.ArrayList;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * <p>Banner轮播</p>
 *
 * @author canmeizhexue<br/>
 * @version 1.0 (2015-10-23)
 */
public class ViewFlowActivity extends BaseActivity {

    @Bind(R.id.viewflow)
    ViewFlow viewflow;
    @Bind(R.id.viewflowindic)
    CircleFlowIndicator viewflowindic;

    /**
     * dd
     */
    private BannerAdapter bannerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewflow);
        ButterKnife.bind(this);
        displayPhoto();
    }

    /**
     * 从网络加载图片
     */
    private void displayPhoto() {
        viewflow.setFlowIndicator(viewflowindic);
        viewflow.setTimeSpan(3000);
        bannerAdapter = new BannerAdapter(this);
        ArrayList<String> data = getData();
        bannerAdapter.addAll(data);
        viewflow.setmSideBuffer(data.size());
        viewflow.setSelection(data.size() * 1000);    //设置初始位置
        viewflow.startAutoFlowTimer();  //启动自动播放
        viewflow.setAdapter(bannerAdapter);

        viewflow.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewflow.stopAutoFlowTimer();
                ArrayList<String> data = getData();
                bannerAdapter.clear(false);
                bannerAdapter.addAll(data, false);
                viewflowindic.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                viewflow.setmSideBuffer(data.size());
                viewflow.setSelection(data.size() * 1000);    //设置初始位置
                if (data.size() > 1) {
                    viewflow.startAutoFlowTimer();  //启动自动播放
                }
            }
        }, 10000);
    }

    /**
     * sss
     *
     * @return dd
     */
    private ArrayList<String> getData() {
        ArrayList<String> data = new ArrayList<>();
        int len = new Random().nextInt(5) + 1;
        for (int i = 0; i < len; i++) {
            int yu = i % 3;
            switch (yu) {
                case 0:
                    data.add("http://img2.imgtn.bdimg.com/it/u=2964677894,3367949944&fm=21&gp=0.jpg");
                    break;
                case 1:
                    data.add("http://img4.imgtn.bdimg.com/it/u=524026896,580360078&fm=21&gp=0.jpg");
                    break;
                case 2:
                    data.add("http://www.deskcar.com/desktop/else/2004415110125/4.jpg");
                    break;
                default:
                    break;
            }
        }
        return data;
    }

}
