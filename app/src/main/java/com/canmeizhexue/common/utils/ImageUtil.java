package com.canmeizhexue.common.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**图片处理<br>
 * 有些手机进入拍照页面时，会关闭前一个页面，返回去的时候又重建了前一个页面，所以应该在用户触发点去调往相机<br>
 * http://www.diycode.cc/topics/101
 * Created by zengyaping on 2016-9-9.
 */
public class ImageUtil {
    /**
     * 这个方法来源于UniversalImageLoader框架
     * 图片像素压缩使用，将图片压缩到指定宽高时，返回采样值
     * @param options 图片的options
     * @param reqWidth 期望的宽
     * @param reqHeight 期望的高
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        LogUtils.d("silence","---height,width------"+height+","+width);
        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }

            long totalPixels = width * height / inSampleSize;

            final long totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels > totalReqPixelsCap) {
                inSampleSize *= 2;
                totalPixels /= 2;
            }
        }
        return inSampleSize;
    }

    /**
     * 像素压缩图片，可能返回null，也可能返回原图
     * @param imagePath 图片地址
     * @param requestWidth 请求的宽度
     * @param requestHeight 请求的高度
     * @return
     */
    public static Bitmap decodeBitmapFromFile(String imagePath, int requestWidth, int requestHeight) {
        try {
            if (!TextUtils.isEmpty(imagePath)) {
                LogUtils.d("silence","---requestHeight,requestWidth------"+requestHeight+","+requestWidth);
                if (requestWidth <= 0 || requestHeight <= 0) {
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                    return bitmap;
                }
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;//不加载图片到内存，仅获得图片宽高
                BitmapFactory.decodeFile(imagePath, options);
                LogUtils.d("silence","---from Options height,original width------"+options.outHeight+","+options.outWidth);
                if (options.outHeight == -1 || options.outWidth == -1) {
                    // 因为有些奇葩手机通过上面的方法还是获取不了图片的宽高，比如小米4s
                    try {
                        ExifInterface exifInterface = new ExifInterface(imagePath);
                        int height = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的高度
                        int width = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的宽度
                        options.outWidth = width;
                        options.outHeight = height;
                        LogUtils.d("silence","---from ExifInterface height,width------"+options.outHeight+","+options.outWidth);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                options.inSampleSize = calculateInSampleSize(options, requestWidth, requestHeight); //计算获取新的采样率
                LogUtils.d("silence","---------inSampleSize----"+options.inSampleSize);
                options.inJustDecodeBounds = false;
                Bitmap decodeBitmap = BitmapFactory.decodeFile(imagePath, options);
                // 防止图片有角度问题
                int j = readPictureDegree(imagePath);
                Bitmap localBitmap2 = null;
                if ((decodeBitmap != null) && (j != 0)) {
                    localBitmap2 = rotateBitmap(j, decodeBitmap);
                    if(localBitmap2!=decodeBitmap){
                        decodeBitmap.recycle();

                    }
                    return localBitmap2;
                }
                return decodeBitmap;
            }
        }catch (Exception e){
            e.printStackTrace();
        }catch (Error error){
            // OOM
            error.printStackTrace();
        }
        return null;

    }
    /**
     * 获取图片角度
     * @param imagePath 图片路径
     * @return
     */
    public static int readPictureDegree(String imagePath) {
        int degree = 0;
        try {
            ExifInterface localExifInterface = new ExifInterface(imagePath);
            int orientation = localExifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                break;
                default:
                    break;
            }
        } catch (IOException localIOException) {
            localIOException.printStackTrace();
        }
        return degree;
    }
    /**
     * 旋转图片
     * @param paramInt 旋转角度
     * @param paramBitmap 旋转的图片实体
     * @return
     */
    public static Bitmap rotateBitmap(int paramInt, Bitmap paramBitmap) {
        Matrix localMatrix = new Matrix();
        localMatrix.postRotate(paramInt);
        return Bitmap.createBitmap(paramBitmap, 0, 0, paramBitmap.getWidth(), paramBitmap.getHeight(), localMatrix, true);
    }

    /**
     * 质量压缩,
     * @param image 被压缩的图片
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        //循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while ( baos.toByteArray().length / 1024>100) {
            options -= 10;//每次都减少10
            if (options>0) {
                //重置baos即清空baos
                baos.reset();
                //这里压缩options%，把压缩后的数据存放到baos中
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            }
        }
        //把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        return BitmapFactory.decodeStream(isBm, null, null);
    }

    /**
     * 利用matrix固定大小压缩
     * @param bitMap 被压缩的图片
     * @param outWidth 压缩后的图片宽度
     * @param outHeight 压缩后的图片高度
     * @return
     */
    public static Bitmap compressFixBitmap(Bitmap bitMap, int outWidth, int outHeight) {
        int width = bitMap.getWidth();
        int height = bitMap.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) outWidth) / width;
        float scaleHeight = ((float) outHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        return Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix, true);
    }
    /**
     * 图片转储为文件
     * @param bitmap 图像
     * @param imageFile 输出的文件路径
     */
    public static boolean saveBitmapTofile(Bitmap bitmap, File imageFile) {
        OutputStream os = null;
        BufferedOutputStream bufferedOutputStream=null;

        try {
            os = new FileOutputStream(imageFile);
            bufferedOutputStream = new BufferedOutputStream(os);
            boolean isOK = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bufferedOutputStream);
            bufferedOutputStream.flush();
            return isOK;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }finally {
            if(bufferedOutputStream!=null){
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
