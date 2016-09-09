package com.canmeizhexue.common.utils;

import android.graphics.Bitmap;
import android.view.View;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**屏幕截图
 * Created by zengyaping on 2016-9-9.
 */
public class ScreenShotUtil {
    /**
     * 将view树当前的图形保存到文件
     * @param view view
     * @param file 文件
     */
    public static void saveViewToFile(View view, File file){
        if(view!=null && file!=null){
            FileOutputStream fileOutputStream=null;
            BufferedOutputStream bufferedOutputStream = null;
            try {
                if(!file.exists()){
                    file.createNewFile();
                }
                view.setDrawingCacheEnabled(true);
                view.buildDrawingCache();
                Bitmap bitmap = view.getDrawingCache();
                if(bitmap!=null){
                    fileOutputStream = new FileOutputStream(file);
                    bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,bufferedOutputStream);
                    bufferedOutputStream.flush();
                }

            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(bufferedOutputStream!=null){
                    try {
                        bufferedOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                view.destroyDrawingCache();
            }
        }
    }
}
