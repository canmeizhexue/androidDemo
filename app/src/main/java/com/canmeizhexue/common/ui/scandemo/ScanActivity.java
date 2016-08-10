package com.canmeizhexue.common.ui.scandemo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.canmeizhexue.common.R;
import com.canmeizhexue.common.views.scan.custom.app.DimensCodeTools;
import com.canmeizhexue.common.views.scan.custom.app.ScanParams;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * <p>扫一扫使用示例</p>
 *
 * @author canmeizhexue<br/>
 * @version 1.0 (2015-11-05)
 */
public class ScanActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
    }

    /**
     * 启动扫描
     *
     * @param view button
     */
    public void start(View view) {
        DimensCodeTools.startScan(this, ScanParams.ONLY_TITLE);
/*        // 开启扫描条形码
        DimensCodeTools.startScanWithFeature(this, ScanParams.BARCODE_FEATURES);
        // 开启扫描条形码 指定所有参数
        DimensCodeTools.startScan(this, ScanParams.BARCODE_FEATURES, 1000, 500, 1000, 1000);
        // 指定条行码扫描大小
        DimensCodeTools.starScanWithBarSize(this, 1000, 500);
        // 指定二维码扫描大小
        DimensCodeTools.starScanWithQRSize(this, 1000, 1000);*/
    }

    /**
     * 生成条形码
     *
     * @param view button
     */
    public void startCreateBarCode(View view) {
        Intent intent = new Intent(this, BarCodeActivity.class);
        startActivity(intent);
    }

    /**
     * 生成二维码
     *
     * @param view button
     */
    public void startCreateQRCode(View view) {
        Intent intent = new Intent(this, QRCodeActivity.class);
        startActivity(intent);
    }

    /**
     * 解析回调
     *
     * @param requestCode 请求标识
     * @param resultCode  返回标识
     * @param data        返回数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String res = DimensCodeTools.scanForResult(requestCode, resultCode, data);
        if (res != null) {
            Pattern p = Pattern.compile("[0-9]*");
            Matcher m = p.matcher(res);
            if (m.matches()) {
                super.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                        .parse("https://www.baidu.com/s?wd=" + res))); // 7
            } else {
                Toast.makeText(this, res, Toast.LENGTH_LONG).show();
            }
        }
    }

}
