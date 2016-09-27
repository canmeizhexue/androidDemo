package com.canmeizhexue.common.bluetooth;

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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by silence on 2016/9/25.
 *,通过客户端和服务端的程序，可以看出俩个蓝牙设备没有配对也是可以通信的,,使用一个UUID来标识一个RFECOMM信道，服务端必须在某一个信道上监听，客户端在同一个信道上发起连接请求
 * 如果想深入，可以看设置程序里面的蓝牙相关部分代码
 * http://blog.csdn.net/q610098308/article/details/45248423
 * http://www.jb51.net/article/79335.htm
 */
public class BluetoothMainActivity extends BaseActivity  implements AdapterView.OnItemClickListener {

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
        demoModels.add(new DemoModel("客户端程序", BluetoothClientActivity.class));
        demoModels.add(new DemoModel("服务端程序", BluetoothServerActivity.class));
        demoModels.add(new DemoModel("蓝牙功能", BluetoothChatActivity.class));
        demoModels.add(new DemoModel("蓝牙设备扫描", DeviceListActivity.class));

    }
}
