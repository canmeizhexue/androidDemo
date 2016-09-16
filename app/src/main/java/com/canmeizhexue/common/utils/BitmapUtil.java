package com.canmeizhexue.common.utils;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class BitmapUtil {
	public static final String TAG = "BPUtil";
	public static final String JPEG_FILE_PREFIX = "IMG_";
	public static final String JPEG_FILE_SUFFIX = ".jpg";
	public static final String THUMBNAIL_FILE_PREFIX = "cc_";

	private static final char[] CHARS = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
			'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
			'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
			'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
			'X', 'Y', 'Z' };

	private static BitmapUtil bpUtil;

	public static BitmapUtil getInstance() {
		if (bpUtil == null)
			bpUtil = new BitmapUtil();
		return bpUtil;
	}

	// width="60" height="30"
	// base_padding_left="5"
	// range_padding_left="10"
	// base_padding_top="15"
	// range_padding_top="10"
	// codeLength="4"
	// line_number="3"
	// font_size="20"

	// default settings
	private static final int DEFAULT_CODE_LENGTH = 4;
	private static final int DEFAULT_FONT_SIZE = 20;
	private static final int DEFAULT_LINE_NUMBER = 3;
	private static final int BASE_PADDING_LEFT = 5, RANGE_PADDING_LEFT = 10,
			BASE_PADDING_TOP = 15, RANGE_PADDING_TOP = 10;
	private static final int DEFAULT_WIDTH = 60, DEFAULT_HEIGHT = 30;

	// settings decided by the layout xml
	// canvas width and height
	private int width = DEFAULT_WIDTH, height = DEFAULT_HEIGHT;

	// random word space and pading_top
	private int base_padding_left = BASE_PADDING_LEFT,
			range_padding_left = RANGE_PADDING_LEFT,
			base_padding_top = BASE_PADDING_TOP,
			range_padding_top = RANGE_PADDING_TOP;

	// number of chars, lines; font size
	private int codeLength = DEFAULT_CODE_LENGTH,
			line_number = DEFAULT_LINE_NUMBER, font_size = DEFAULT_FONT_SIZE;

	// variables
	private String code;
	private int padding_left, padding_top;
	private Random random = new Random();

	public Bitmap createBitmap() {
		padding_left = 0;

		Bitmap bp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas c = new Canvas(bp);

		code = createCode();

		c.drawColor(Color.WHITE);
		Paint paint = new Paint();
		paint.setTextSize(font_size);

		for (int i = 0; i < code.length(); i++) {
			randomTextStyle(paint);
			randomPadding();
			c.drawText(code.charAt(i) + "", padding_left, padding_top, paint);
		}

		for (int i = 0; i < line_number; i++) {
			drawLine(c, paint);
		}

		c.save(Canvas.ALL_SAVE_FLAG);// 保存
		c.restore();//
		return bp;
	}

	public String getCode() {
		return code;
	}

	private String createCode() {
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < codeLength; i++) {
			buffer.append(CHARS[random.nextInt(CHARS.length)]);
		}
		return buffer.toString();
	}
	
	/**
	 * 保存bitmap
	 * 
	 * @param context
	 * @param bitmap
	 * @return
	 */
	public static boolean saveBitmaps(Context context, Bitmap bitmap,File newFile) {
		try {
			
			if (!newFile.exists()) {
				newFile.createNewFile();
			}
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(
					newFile));			
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 压缩图片的方法当图片大于100kb就压缩
	 * @param image
	 * @param picsize
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image,int picsize) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while ( baos.toByteArray().length / 1024>picsize) {	//循环判断如果压缩后图片是否大于100kb,大于继续压缩		
			baos.reset();//重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;//每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	private void drawLine(Canvas canvas, Paint paint) {
		int color = randomColor();
		int startX = random.nextInt(width);
		int startY = random.nextInt(height);
		int stopX = random.nextInt(width);
		int stopY = random.nextInt(height);
		paint.setStrokeWidth(1);
		paint.setColor(color);
		canvas.drawLine(startX, startY, stopX, stopY, paint);
	}

	private int randomColor() {
		return randomColor(1);
	}

	private int randomColor(int rate) {
		int red = random.nextInt(256) / rate;
		int green = random.nextInt(256) / rate;
		int blue = random.nextInt(256) / rate;
		return Color.rgb(red, green, blue);
	}

	private void randomTextStyle(Paint paint) {
		int color = randomColor();
		paint.setColor(color);
		paint.setFakeBoldText(random.nextBoolean()); // true为粗体，false为非粗体
		float skewX = random.nextInt(11) / 10;
		skewX = random.nextBoolean() ? skewX : -skewX;
		paint.setTextSkewX(skewX); // float类型参数，负数表示右斜，整数左斜
		// paint.setUnderlineText(true); //true为下划线，false为非下划线
		// paint.setStrikeThruText(true); //true为删除线，false为非删除线
	}

	private void randomPadding() {
		padding_left += base_padding_left + random.nextInt(range_padding_left);
		padding_top = base_padding_top + random.nextInt(range_padding_top);
	}

	// decodes image and scales it to reduce memory consumption
	public static Bitmap decodeFile(File f) {
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// The new size we want to scale to
			final int REQUIRED_SIZE = 140;

			// Find the correct scale value. It should be the power of 2.
			int scale = 1;
			while (o.outWidth / scale / 2 >= REQUIRED_SIZE
					&& o.outHeight / scale / 2 >= REQUIRED_SIZE)
				scale *= 2;

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	/*
	 * 将图片加入相册中
	 */
	public static void galleryAddPic(Context context, String filePath) {
		Intent mediaScanIntent = new Intent(
				"android.intent.action.MEDIA_SCANNER_SCAN_FILE");
		File f = new File(filePath);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		context.sendBroadcast(mediaScanIntent);
	}

	private static final String CAMERA_DIR = "/dcim/";
	/**
	 * 获取相片中的存储路径
	 * @param context
	 * @return
	 */
	public static String getPictureStorePath(Context context,int sdPathSettingId){
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			//String path=context.getString(sdPathSettingId);
			File file= Environment.getExternalStorageDirectory();
			String pathFileName=file.getAbsoluteFile()  + File.separator + CAMERA_DIR;	
			File filePath=new File(pathFileName);
			if (!filePath.exists()){
				filePath.mkdirs();
			}
			String name=pathFileName + File.separator + ".nomedia";
			File fileNomedia=new File(name);
			if (fileNomedia.exists()){
				fileNomedia.delete();
			}
			return pathFileName;
		}else{
			String pathFileName= context.getFilesDir().getAbsolutePath() + File.separator + CAMERA_DIR;
			File filePath=new File(pathFileName);
			if (!filePath.exists()){
				filePath.mkdirs();
			}
			String name=pathFileName + File.separator + ".nomedia";
			File fileNomedia=new File(name);
			if (fileNomedia.exists()){
				fileNomedia.delete();
			}
			return pathFileName;
		}
	}

	
	/**
	 * 获取相片中的不被扫描的存储路径
	 * @param context
	 * @return
	 */
	public static String getUnScanPictureStorePath(Context context,int sdPathSettingId){
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			//String path=context.getString(sdPathSettingId);
			File file= Environment.getExternalStorageDirectory();
			String pathFileName=file.getAbsoluteFile()  + File.separator +context.getString(sdPathSettingId) +File.separator+ CAMERA_DIR;	
			File filePath=new File(pathFileName);
			if (!filePath.exists()){
				filePath.mkdirs();
			}
			String name=pathFileName + File.separator + ".nomedia";
			File fileNomedia=new File(name);
			if (!fileNomedia.exists()){
				try {
					fileNomedia.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return pathFileName;
		}else{
			String pathFileName= context.getFilesDir().getAbsolutePath() +File.separator+context.getString(sdPathSettingId) + File.separator + CAMERA_DIR;
			File filePath=new File(pathFileName);
			if (!filePath.exists()){
				filePath.mkdirs();
			}
			String name=pathFileName + File.separator + ".nomedia";
			File fileNomedia=new File(name);
			if (!fileNomedia.exists()){
				try {
					fileNomedia.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return pathFileName;
		}
	}
	/**
	 * 获得圆角图片
	 * 
	 * @param bitmap
	 * @param pixels
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
		// Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }

            long totalPixels = width / inSampleSize * height / inSampleSize ;

            final long totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels > totalReqPixelsCap) {
                inSampleSize *= 2;
                totalPixels /= 2;
            }
        }

        Log.i(TAG, "inSampleSize: " + inSampleSize);
        return inSampleSize;
	}

	/**
	 * 压缩图片，用于大图显示
	 * @param pathName
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeSampledBitmap(String pathName,int reqWidth, int reqHeight) {
	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(pathName, options);
	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeFile(pathName, options);
	}
	
	public static BitmapFactory.Options getBitmapDecodeOption(String pathName,int reqWidth, int reqHeight ){
		BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(pathName, options);
	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return options;
	}

	/**
	 * 
	 * @param bmpFilePath
	 * @param requestedWidth
	 *            宽度上限
	 * @param requestedHeight
	 *            高度上限
	 * @return 宽度上限和高度上限都小于或等于0时，不做任何缩放，返回原图片
	 */
	public static Bitmap resizeBitmap(String bmpFilePath, int requestedWidth,
			int requestedHeight) {
		if (requestedWidth <= 0 || requestedHeight <= 0) {
			Bitmap bmp = null;
			try {
				bmp = BitmapFactory.decodeFile(bmpFilePath);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return bmp;
		}

		final BitmapFactory.Options options = new BitmapFactory.Options();

		// 获得图片的宽高
		options.inJustDecodeBounds = true;
		try {
			BitmapFactory.decodeFile(bmpFilePath, options);
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		final int srcWidth = options.outWidth;
		final int srcHeight = options.outHeight;

		options.inSampleSize = (int) getScale(srcWidth, srcHeight,
				requestedWidth, requestedHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeFile(bmpFilePath, options);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (bitmap == null) {
			return null;
		}

		// 个别机型，用以上方法的压缩率不符合要求，所以再次精确压缩
		if (bitmap.getWidth() > requestedWidth
				|| bitmap.getHeight() > requestedHeight) {
			bitmap = resizeBitmapInOldWay(bitmap, requestedWidth,
					requestedHeight);
		}
		return bitmap;

	}
	
	/**
	 * 压缩大图
	 */
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
	        int reqWidth, int reqHeight) {
	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeResource(res, resId, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    Bitmap bitmap = BitmapFactory.decodeResource(res, resId, options);
	   // 个别机型，用以上方法的压缩率不符合要求，所以再次精确压缩
 		if (bitmap.getWidth() > reqWidth || bitmap.getHeight() > reqHeight) {
 			bitmap = resizeBitmapInOldWay(bitmap, reqWidth,reqHeight);
 		}
	    return bitmap;
	}

	public static Bitmap resizeBitmapInOldWay(Bitmap bitmap, int maxWidth,
			int maxHeight) {
		// 获得图片的宽高
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		// matrix的缩小参数应该为getScale的倒数(小数)
		float scale = 1 / getScale(width, height, maxWidth, maxHeight);
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		// 得到新的图片
		Bitmap newbm = null;
		try {
			newbm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,
					true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newbm;
	}
	/**
	 * 统一计算缩放比例
	 * 
	 * @param srcWidth
	 * @param requestedWidth
	 * @return
	 */
	public static float getScale(int srcWidth, int srcHeight,
			int requestedWidth, int requestedHeight) {
		float scale = 1;
		if (requestedWidth <= 0 && requestedHeight <= 0) {
			// 不做任何缩放
			scale = 1;
		} else if (requestedWidth > 0 && requestedHeight > 0) {

			float scaleWidth = srcWidth * 1.0f / requestedWidth;
			float scaleHeight = srcHeight * 1.0f / requestedHeight;
			if (scaleWidth < scaleHeight) {
				scale = scaleHeight;
			} else {
				scale = scaleWidth;
			}
		} else if (requestedWidth > 0 && requestedHeight <= 0) {
			float scaleWidth = srcWidth * 1.0f / requestedWidth;
			scale = scaleWidth;
		} else if (requestedWidth <= 0 && requestedHeight > 0) {
			float scaleHeight = srcHeight * 1.0f / requestedHeight;
			scale = scaleHeight;
		}

		return scale;
	}
	/**
	 * 保存bitmap
	 * 
	 * @param context
	 * @param bitmap
	 * @return
	 */
	public static boolean saveBitmap(Context context, Bitmap bitmap,File newFile) {
		try {
			
			if (!newFile.exists()) {
				newFile.createNewFile();
			}
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(newFile));			
			bitmap.compress(Bitmap.CompressFormat.JPEG, 75, bos);
			bos.flush();
			bos.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
		// 创建操作图片的用的Matrix对象
		Matrix matrix = new Matrix();
		// 缩放翻转图片的动作
		// sw sh的绝对值为绽放宽高的比例，sw为负数表示X方向翻转，sh为负数表示Y方向翻转
		// matrix.postScale(1, 1);
		// 旋转
		matrix.postRotate(rotate);
		// 创建一个新的图片
		Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),bitmap.getHeight(), matrix, true);
		return resizeBitmap;
	}

	/**
	 * 纠正图片裁剪后造成的图片旋转问题
	 * @param srcPath 正常的图片路径
	 * @param destPath 需要纠正的图片路径
	 * @throws IOException
	 */
	public static void correctOrientation(String srcPath , String destPath) throws IOException{
		ExifInterface srcExif = new ExifInterface(srcPath);
		ExifInterface destExif = new ExifInterface(destPath);
		destExif.setAttribute(ExifInterface.TAG_ORIENTATION, srcExif.getAttribute(ExifInterface.TAG_ORIENTATION));
		destExif.saveAttributes();
	}
}
