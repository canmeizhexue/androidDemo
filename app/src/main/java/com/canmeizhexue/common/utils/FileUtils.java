package com.canmeizhexue.common.utils;

import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * <p>文件操作工具类 1、提供文件操作功能；</p>
 *
 * @author canmeizhexue
 * @version 1.0 (2015/10/19)
 */
public class FileUtils {

    /**
     * 文件后缀的“.”号
     */
    public static final String FILE_EXTENSION_SEPARATOR = ".";

    /**
     * 空文件长度
     */
    public static final long FILE_SIZE_ZERO = 0;
    /**
     * Bit长度
     */
    public static final long FILE_SIZE_B = 1024;
    /**
     * MB长度
     */
    public static final long FILE_SIZE_M = 1024 * 1024;
    /**
     * GB长度
     */
    public static final long FILE_SIZE_G = 1024 * 1024 * 1024;

    /**
     * 创建一个文件夹
     *
     * @param filePath 文件夹路径
     * @return true 成功，false 失败
     */
    public static boolean createFolder(String filePath) {
        return createFolder(filePath, false);
    }

    /**
     * 创建一个文件夹
     *
     * @param filePath 文件夹路径
     * @param recreate 是否重建
     * @return true 成功，false 失败
     */
    public static boolean createFolder(String filePath, boolean recreate) {
        String folderName = getFolderName(filePath);
        if (TextUtils.isEmpty(folderName)) {
            return false;
        }

        File folder = new File(folderName);
        if (folder.exists()) {
            if (recreate) {
                deleteFile(folderName);
                return folder.mkdirs();
            } else {
                return true;
            }
        } else {
            return folder.mkdirs();
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return true 存在，false 不存在
     */
    public static boolean isFileExist(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }

        File file = new File(filePath);
        return (file.exists() && file.isFile());
    }

    /**
     * 重命名文件/文件夹
     *
     * @param path    文件路径
     * @param newName 新名字
     * @return true 成功，false 失败
     */
    public static boolean reName(final String path, final String newName) {
        boolean result = false;
        if (TextUtils.isEmpty(path) || TextUtils.isEmpty(newName)) {
            return result;
        }
        try {
            File file = new File(path);
            if (file.exists()) {
                result = file.renameTo(new File(newName));
            }
        } catch (Exception e) {
        }

        return result;
    }

    /**
     * 从文件夹路径中获取文件夹名
     *
     * @param filePath 文件夹路径
     * @return 文件夹名
     */
    public static String getFolderName(String filePath) {

        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePos = filePath.lastIndexOf(File.separator);
        return (filePos == -1) ? "" : filePath.substring(0, filePos);
    }

    /**
     * 删除文件夹下的文件
     *
     * @param path 文件路径
     * @return true 成功，false 失败
     */
    public static boolean deleteFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return true;
        }

        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                f.delete();
            } else if (f.isDirectory()) {
                deleteFile(f.getAbsolutePath());
            }
        }
        return file.delete();
    }

    /**
     * 如果是文件夹就删除文件下所有文件，然后删除文件夹，如果是文件就直接删除文件
     *
     * @param filepath 文件路径
     * @return -1为路径不存在，否则为删除文件的总大小
     * @throws IOException
     */
    public static long deleteFiles(String filepath) {
        if (filepath == null) {
            return -1;
        }
        long total = 0;
        try {
            File f = new File(filepath);// 定义文件路径
            if (!f.exists()) {
                return -1;
            }
            if (f.isDirectory()) {// 目录
                int i = f.listFiles().length;
                if (i > 0) {
                    File delFile[] = f.listFiles();
                    for (int j = 0; j < i; j++) {
                        if (delFile[j].isDirectory()) {
                            // 递归调用del方法并取得子目录路径
                            total = total + deleteFiles(delFile[j].getAbsolutePath());
                        }
                        total += delFile[j].length();
                        delFile[j].delete();// 删除文件
                    }
                }
                f.delete();
            } else {
                total += f.length();
            }
            if (f.exists()) {
                f.delete();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    /***
     * 读取文件内容
     *
     * @param filePath 文件路径
     * @return StringBuilder
     */
    public static StringBuilder readFileToBuilder(String filePath) {
        File file = new File(filePath);
        StringBuilder fileContent = new StringBuilder("");
        if (file == null || !file.isFile()) {
            return null;
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!fileContent.toString().equals("")) {
                    fileContent.append("\r\n");
                }
                fileContent.append(line);
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * 读取文件内容
     *
     * @param filePath 文件路径
     * @return String
     */
    public static String readFileToString(String filePath) {
        StringBuilder sBuilder = readFileToBuilder(filePath);
        if (sBuilder != null) {
            return sBuilder.toString();
        }
        return "";
    }

    /**
     * 把字符串写入文件
     *
     * @param filePath 文件路径
     * @param content  文件内容
     * @param append   为true,则继续写入，false则清除文件原来的内容重新写入
     * @return return true 写入成功
     */
    public static boolean writeFile(String filePath, String content,
                                    boolean append) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(filePath, append);
            fileWriter.write(content);
            fileWriter.close();
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * 将数字大小转换成“MB"、“KB”、"GB"格式
     *
     * @param size 文件大小
     * @return String（“MB"、“KB”、"GB"）
     */
    public static String getSize(long size) {
        String result = null;
        if (size < FILE_SIZE_ZERO) {
            return result;
        }
        if (size > FILE_SIZE_G) {
            float f = (float) size / (FILE_SIZE_G);
            String s = String.valueOf(f);
            if (s.length() - s.indexOf(".") > 2) {
                result = s.substring(0, s.indexOf(".") + 3);
            } else {
                result = s;
            }
            return result + "GB";
        } else if (size > FILE_SIZE_M) {
            float f = (float) size / (FILE_SIZE_M);
            String s = String.valueOf(f);
            if (s.length() - s.indexOf(".") > 2) {
                result = s.substring(0, s.indexOf(".") + 3);
            } else {
                result = s;
            }
            return result + "MB";
        } else if (size > FILE_SIZE_B) {
            float f = (float) size / FILE_SIZE_B;
            String s = String.valueOf(f);
            if (s.length() - s.indexOf(".") > 2) {
                result = s.substring(0, s.indexOf(".") + 3);
            } else {
                result = s;
            }

            return result + "KB";
        } else if (size < FILE_SIZE_B) {
            return String.valueOf(size) + "B";
        } else {
            return null;
        }
    }

    /**
     * 从文件流中获取字节流
     *
     * @param is 文件输入流
     * @return
     * @throws IOException io读写异常
     */
    public static byte[] getBytesFromStream(InputStream is) throws IOException {
        int len;
        int size = 1024;
        byte[] buf;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        buf = new byte[size];
        while ((len = is.read(buf, 0, size)) != -1) {
            bos.write(buf, 0, len);
        }
        buf = bos.toByteArray();

        return buf;
    }

    /**
     * 持久化字节流-保存为本地文件
     *
     * @param bytes 字节流
     * @param path  持久化绝对路径
     * @return true保存成功，false保存失败
     */
    public static boolean saveBytesToFile(byte[] bytes, String path) {
        try {
            FileOutputStream fileOuputStream = new FileOutputStream(path);
            fileOuputStream.write(bytes);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 持久化文件流为本地文件
     *
     * @param is   文件输入流
     * @param path 持久化绝对路径
     * @return
     * @throws IOException io读写异常
     */
    public static boolean saveInputStreamToFile(InputStream is, String path) throws IOException {
        return saveBytesToFile(getBytesFromStream(is), path);
    }

    /**
     * 下载图片
     *
     * @param url 图片地址
     * @return 图片在sd卡的地址
     */
    public static String downloadImageFile(String url) {
        URL imgUrl;
        HttpURLConnection urlConn = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        if (!SDCardUtils.hasSDCard()) {
            return null;
        }
        File folder = new File(SDCardUtils.OTHER_CACHE_FOLDER);
        if (folder != null && !folder.exists()) {
            folder.mkdirs();
        }
        String filename = "ht"+ System.currentTimeMillis();

        File photoFile = new File(folder, filename + ".jpg");
        if (photoFile.exists()) {
            return photoFile.getAbsolutePath();
        }

        try {
            photoFile.createNewFile();

            imgUrl = new URL(url);
            urlConn = (HttpURLConnection) imgUrl.openConnection();
            urlConn.setDoInput(true);
            urlConn.connect();
            bis = new BufferedInputStream(urlConn.getInputStream());
            bos = new BufferedOutputStream(new FileOutputStream(photoFile));
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, length);
            }
            bos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //添加判空处理 by hxm
            if (urlConn != null) {
                urlConn.disconnect();
            }

        }

        return photoFile.getAbsolutePath();
    }

}
