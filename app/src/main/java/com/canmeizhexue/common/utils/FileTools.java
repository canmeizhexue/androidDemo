package com.canmeizhexue.common.utils;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.widget.Toast;

public class FileTools {
	public static final String TAG = FileTools.class.getSimpleName();

	public static boolean isSdCardExist(Context context, boolean showToast) {
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if (!sdCardExist && showToast) {
			Toast.makeText(context, "没有SD卡，请插入SD后再进行操作", 1).show();
		} else if (!sdCardExist && !showToast) {
			System.out.println("no sdcard");
		}
		return sdCardExist;
	}

	public static boolean copyAssetFile(Context context, String assetsFileName,
			String filePath) {
		try {
			InputStream is = context.getResources().getAssets()
					.open(assetsFileName);

			File outFile = new File(filePath, assetsFileName);
			if (outFile.exists())
				outFile.delete();
			OutputStream out = new FileOutputStream(outFile);

			// Transfer bytes from in to out
			byte[] buf = new byte[1024];
			int len;
			while ((len = is.read(buf)) > 0) {
				out.write(buf, 0, len);
			}

			is.close();
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	/** 读取文件 */
	public static InputStream readAssets(Context context, String filename) {
		InputStream in = null;
		AssetManager assetManager = context.getAssets();
		try {
			in = assetManager.open(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return in;
	}

	/** 获取文件列表 */
	public static List<String> getAssetsFilelist(Context context, String path) {
		List<String> list = new ArrayList<String>();
		AssetManager assetManager = context.getAssets();
		String[] strtmp = null;
		try {
			strtmp = assetManager.list(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (String tmp : strtmp) {
			list.add(tmp);
		}
		return list;
	}

	/**
	 * 读取sdcard文件的内容
	 * 
	 * @param filename
	 *            文件名称
	 * @return
	 * @throws Exception
	 */
	public static String readFile(Context context, String filename)
			throws Exception {
		// 获得输入流
		FileInputStream inStream = context.openFileInput(filename);
		// new一个缓冲区
		byte[] buffer = new byte[1024];
		int len = 0;
		// 使用ByteArrayOutputStream类来处理输出流
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1) {
			// 写入数据
			outStream.write(buffer, 0, len);
		}
		// 得到文件的二进制数据
		byte[] data = outStream.toByteArray();
		// 关闭流
		outStream.close();
		inStream.close();
		return new String(data);
	}

	/**
	 * 以默认私有方式保存文件内容至SDCard中
	 * 
	 * @param filename
	 * @param content
	 * @throws Exception
	 */
	public static void saveToSDCard(Context context, String filename,
			String content) throws Exception {
		// 通过getExternalStorageDirectory方法获取SDCard的文件路径
		File file = new File(Environment.getExternalStorageDirectory(),
				filename);
		// 获取输出流
		FileOutputStream outStream = new FileOutputStream(file);
		outStream.write(content.getBytes());
		outStream.close();
	}

	/**
	 * 以默认私有方式保存文件内容，存放在手机存储空间中
	 * 
	 * @param filename
	 * @param content
	 * @throws Exception
	 */
	public static void save(Context context, String filename, String content)
			throws Exception {
		//
		FileOutputStream outStream = context.openFileOutput(filename,
				Context.MODE_PRIVATE);
		outStream.write(content.getBytes());
		outStream.close();
	}

	/**
	 * 以追加的方式保存文件内容
	 * 
	 * @param filename
	 *            文件名称
	 * @param content
	 *            文件内容
	 * @throws Exception
	 */
	public static void saveAppend(Context context, String filename,
			String content) throws Exception {
		FileOutputStream outStream = context.openFileOutput(filename,
				Context.MODE_APPEND);
		outStream.write(content.getBytes());
		outStream.close();
	}

	/**
	 * 以允许其他应用从该文件中读取内容的方式保存文件(Context.MODE_WORLD_READABLE)
	 * 
	 * @param filename
	 *            文件名称
	 * @param content
	 *            文件内容
	 * @throws Exception
	 */
	public static void saveReadable(Context context, String filename,
			String content) throws Exception {
		FileOutputStream outStream = context.openFileOutput(filename,
				Context.MODE_WORLD_READABLE);
		outStream.write(content.getBytes());
		outStream.close();
	}

	/**
	 * 以允许其他应用往该文件写入内容的方式保存文件
	 * 
	 * @param filename
	 *            文件名称
	 * @param content
	 *            文件内容
	 * @throws Exception
	 */
	public static void saveWriteable(Context context, String filename,
			String content) throws Exception {
		FileOutputStream outStream = context.openFileOutput(filename,
				Context.MODE_WORLD_WRITEABLE);
		outStream.write(content.getBytes());
		outStream.close();
	}

	/**
	 * 以允许其他应用对该文件读和写的方式保存文件(MODE_WORLD_READABLE与MODE_WORLD_WRITEABLE)
	 * 
	 * @param filename
	 *            文件名称
	 * @param content
	 *            文件内容
	 * @throws Exception
	 */
	public static void saveRW(Context context, String filename, String content)
			throws Exception {
		FileOutputStream outStream = context.openFileOutput(filename,
				Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
		// Context.MODE_WORLD_READABLE(1) +
		// Context.MODE_WORLD_WRITEABLE(2),其实可用3替代
		outStream.write(content.getBytes());
		outStream.close();
	}
	
	//读机身内存文件
	public static String readInSystem(String fileName){
		 FileInputStream in;
		try {
			File file=new File(fileName);
			in = new FileInputStream(file);
			try {
				   ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				   byte[] buffer = new byte[1024];
				   int length = -1;
				   while((length = in.read(buffer)) != -1 ){
				    outStream.write(buffer, 0, length);
				   }
				   outStream.close();
				   in.close();
				   return outStream.toString();
				  } catch (IOException e){
				  }
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		  
		  return null;
		}


	public static boolean exist(File file) {
		boolean exist = false;
		if (!(exist = file.exists()))
		System.out.println(file.toString() + " ---> 文件不存在");
		return exist;
	}

	public static boolean exist(String filePath) {
		return exist(new File(filePath));
	}

	public static boolean exist(String filePath, String child) {
		return exist(new File(filePath, child));
	}

	/***************************** DELETE **************************************************************/

	public static void delFolder(String folderPath) {
        try {
                delAllFile(folderPath); //删除完里面所有内容
                String filePath = folderPath;
                filePath = filePath.toString();
                File myFilePath = new File(filePath);
                myFilePath.delete(); //删除空文件夹

        }
        catch (Exception e) {
                System.out.println("删除文件夹操作出错");
                e.printStackTrace();

        }
}

	/**
	 * 删除文件夹里面的所有文件
	 * @param path String 文件夹路径 如 c:/fqf
	 */
	public static void delAllFile(String path) {
	        File file = new File(path);
	        if (!file.exists()) {
	        	System.out.println("jjjj");
	                return;
	        }
	        if (!file.isDirectory()) {
	        	System.out.println("kkkk");
	       return;
	        }
	        System.out.println("lll");
	        String[] tempList = file.list();
	        File temp = null;
	        for (int i = 0; i < tempList.length; i++) {
	        		
	                if (path.endsWith(File.separator)) {
	                        temp = new File(path + tempList[i]);
	                }
	                else {
	                        temp = new File(path + File.separator + tempList[i]);
	                }
	                if (temp.isFile()) {
	                        temp.delete();
	                }

	        }
	}
	
	
	public static void delDirectoryChild(String path) {
		delDirectoryChild(new File(path));
	}

	public static void delDirectoryChild(File file) {
		if (!exist(file))
			return;
		if (file.isDirectory()) {
			File f[] = file.listFiles();
			for (int i = 0; i < f.length; i++) {
				delFile(f[i]);
			}
		} else {
			delFile(file);
		}
	}

	public static void delFile(String path) {
		delFile(new File(path));
	}

	public static void delFile(File file) {
		if (!exist(file))
			return;
		file.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				if (file.isFile()) {
					file.delete();
					return true;
				} else if (file.isDirectory()) {
					delFile(file);
				}
				return false;
			}
		});
		file.delete();
	}
	
	public static void deleteFile(File file) 
	{
		
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法 你应该知道 是删除的意思;
			} else if (file.isDirectory()) { // 否则如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
				}
			}
			file.delete();
		} else {
			System.out.println("file no exist" + '\n');
		}
	}

	// 读取assets数据
	public static String readTextFile(InputStream inputStream) {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte buf[] = new byte[1024];
		int len;
		try {
			while ((len = inputStream.read(buf)) != -1) {
				outputStream.write(buf, 0, len);
			}
			outputStream.close();

			inputStream.close();

		} catch (IOException e) {

		}

		return outputStream.toString();

	}

	// 读取assets数据，解决中文乱码
	public static String readAccsetsFile(InputStream is) {
		try {
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			return new String(buffer, "UTF-8");

		} catch (Exception e) {
			return null;
		}
	}
	
	// 读取assets数据，解决中文乱码
		public static String readFileToString(Context context,String fileName) {
			
			try {
				AssetManager assetManager = context.getAssets();
				InputStream is;
				is=assetManager.open(fileName);
				int size = is.available();
				byte[] buffer = new byte[size];
				is.read(buffer);
				is.close();
				return new String(buffer, "gb2312");

			} catch (Exception e) {
				return null;
			}
		}
		
	public static String readAssetFileToString(Context context,String fileName ){
		try {
			AssetManager assetManager = context.getAssets();
			InputStream is;
			is=assetManager.open(fileName);
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			return new String(buffer, "UTF-8");

		} catch (Exception e) {
			return null;
		}
	}

	public static void writeStringToFile(Context context,String configFile, String jsonString) {
		
		try {
			FileOutputStream outStream = context.openFileOutput(configFile,
					Context.MODE_PRIVATE);
			outStream.write(jsonString.getBytes());
			outStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


}

