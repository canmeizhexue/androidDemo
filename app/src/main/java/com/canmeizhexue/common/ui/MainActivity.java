package com.canmeizhexue.common.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.canmeizhexue.common.R;
import com.canmeizhexue.common.adapter.DemoAdapter;
import com.canmeizhexue.common.base.BaseActivity;
import com.canmeizhexue.common.entity.DemoModel;
import com.canmeizhexue.common.ui.scandemo.ScanActivity;
import com.canmeizhexue.common.ui.viewflowdemo.ViewFlowActivity;
import com.canmeizhexue.common.views.webview.BrowerActivity;
import com.canmeizhexue.common.views.webview.WebActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @Bind(R.id.lv_demo)
    ListView lvDemo;
    @Bind(R.id.ll_root)
    LinearLayout llRoot;

    private List<DemoModel> demoModels;
    private DemoAdapter demoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        demoModels = new ArrayList<>();
        initData();
        demoAdapter = new DemoAdapter(this,demoModels);
        lvDemo.setAdapter(demoAdapter);
        lvDemo.setOnItemClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
/*        // TODO 测试BlockCanary
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        DemoModel currdemo = (DemoModel) adapterView.getItemAtPosition(i);
        if (currdemo!=null && currdemo.clazz != null) {
            startActivity(new Intent(this, currdemo.clazz));
        }
    }
    private void initData(){
        demoModels.clear();
        //WebActivity
        demoModels.add(new DemoModel("WebActivity", WebActivity.class));
        demoModels.add(new DemoModel("BrowerActivity", BrowerActivity.class));
        demoModels.add(new DemoModel("二维码和条形码", ScanActivity.class));
        demoModels.add(new DemoModel("ImageBrowserActivity", ImageBrowserActivity.class));
        demoModels.add(new DemoModel("ImageSelectorActivity", ImageSelectorActivity.class));
        demoModels.add(new DemoModel("Banner轮播", ViewFlowActivity.class));
    }
}
