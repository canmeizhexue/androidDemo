package com.canmeizhexue.common.ui.scandemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.canmeizhexue.common.R;
import com.canmeizhexue.common.views.scan.custom.dimenscode.QRCodeFactory;
import com.canmeizhexue.common.views.scan.custom.dimenscode.ScanHelper;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * <p>生成二维码界面</p>
 *
 * @author canmeizhexue<br/>
 * @version 1.0 (2015-11-05)
 */
public class QRCodeActivity extends Activity {
    private ImageView mQR;
    private static final String TAG = "QRCodeActivity";
    private Bitmap mBmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_qr);
        mQR = (ImageView) findViewById(R.id.qr);
        mQR.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Result res = null;
                try {
                    res = ScanHelper.scanningQRCode(mBmp);
                } catch (NotFoundException e) {
                    e.printStackTrace();
                } catch (ChecksumException e) {
                    e.printStackTrace();
                } catch (FormatException e) {
                    e.printStackTrace();
                }
                Toast.makeText(QRCodeActivity.this, res.getText(), Toast.LENGTH_SHORT).
                        show();
                return false;
            }
        });
    }

    /**
     * 创建存储于文件的二维码
     *
     * @param view button
     */
    public void createQRCodeInFile(View view) {
        String path = getFileRoot() + File.separator + "qr_infile1" + ".jpg";
        try {
            boolean res = QRCodeFactory.createQRCodeInFile("Hello", 500, 500, path, true);
            Log.d(TAG, res + "");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建存储于文件的带icon二维码
     *
     * @param view button
     */
    public void createQRCodeWithIconInFile(View view) {
        String path = getFileRoot() + File.separator + "qr_with_icon_infile2"
                + ".jpg";
        try {
            boolean res = QRCodeFactory
                    .createQRCodeWithIconInFile("你好", 200, 200, BitmapFactory
                                    .decodeResource(getResources(), R.mipmap.ic_launcher),
                            path, false);
            Log.d(TAG, res + "");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建二维码
     *
     * @param view button
     */
    public void createQRCode(View view) {
        try {
            mBmp = QRCodeFactory.createQRCode("Yo~ 切克闹", 200, 200, true);
            if (mBmp != null) {
                mQR.setImageBitmap(mBmp);
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建带icon的二维码
     *
     * @param view button
     */
    public void createQRCodeWithIcon(View view) {
        try {
            mBmp = QRCodeFactory
                    .createQRCodeWithIcon("Hey, Man!!", 550, 550, BitmapFactory
                            .decodeResource(getResources(), R.mipmap.ic_launcher), false);
            if (mBmp != null) {
                mQR.setImageBitmap(mBmp);
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建带颜色的二维码
     *
     * @param view button
     */
    public void createQRCodeWithColor(View view) {
        try {
            mBmp = QRCodeFactory.createQRCodeWithColor("I'm color..",
                    400, 400, 0xff2196F3, 0xffE3F2FD, false);
            if (mBmp != null) {
                mQR.setImageBitmap(mBmp);
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建带颜色和图标的二维码
     *
     * @param view button
     */
    public void createQRCodeWithIconAndColor(View view) {
        try {
            mBmp = QRCodeFactory
                    .createQRCodeWithIconAndColor("I'm icon and color...", 400,
                            400, BitmapFactory.decodeResource(getResources(),
                                    R.mipmap.ic_launcher), 0xff2196F3, 0xffffffff, false);
            if (mBmp != null) {
                mQR.setImageBitmap(mBmp);
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取files文件路径
     *
     * @return 文件路径
     */
    private String getFileRoot() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File external = this.getExternalFilesDir(null);
            if (external != null) {
                return external.getAbsolutePath();
            }
        }
        return this.getFilesDir().getAbsolutePath();
    }

}
