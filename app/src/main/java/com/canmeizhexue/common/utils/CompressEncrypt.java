package com.canmeizhexue.common.utils;

import android.content.Context;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


public class CompressEncrypt {
    private static final String Algorithm = "DESede"; //定义 加密算法,可用 DES,DESede,Blowfish  
   
    
  //对对象进行压缩，并且进行加密
    //加密和压缩失败返回null
    public static byte[] encryptObject(Context context,Serializable object){
        return encryptObject(EncryptUtil.getKeyString(context),object);
    }
    //对对象进行解密，并且进行解压
    //解压和解密失败返回null
    public static Serializable decryptObject(Context context,byte[] data){
        return decryptObject(EncryptUtil.getKeyString(context),data);
    }
    //对对象进行压缩，并且进行加密
    //加密和压缩失败返回null
    public static byte[] encryptObject(String key,Serializable object){
        byte[] bytes=null;
        try {
           // object to bytearray
           ByteArrayOutputStream bo = new ByteArrayOutputStream();
           ObjectOutputStream oo = new ObjectOutputStream(bo);
           oo.writeObject(object);
           bytes = bo.toByteArray();
           bo.close();
           oo.close();
          } catch (Exception e) {
                e.printStackTrace();
             return null;
          }
          if (bytes==null){
              return null;
          }

        byte[] dt=null;
        try{
            dt= compress(bytes);
            if (dt==null){
                return null;
            }
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
        byte[] endata=DESUtil.encrypt(key,dt);
        return endata;
    }
    //对对象进行解密，并且进行解压
    //解压和解密失败返回null
    public static Serializable decryptObject(String key,byte[] data){
       byte[] dsdata=DESUtil.decrypt(key,data);
        if (dsdata==null){
            return null;
        }
        //System.out.println("fffff");
        //System.out.println(dsdata.length);
        byte[] objectdata=null;
        ByteArrayOutputStream out = null;
        ByteArrayInputStream in = null;
        try{
            out = new ByteArrayOutputStream();
            in = new ByteArrayInputStream(dsdata);
            //建立gzip压缩输入流
            GZIPInputStream gzin=new GZIPInputStream(in);
            byte[] buffer = new byte[1024];

            int n;
            while ((n = gzin.read(buffer)) >= 0) {
              out.write(buffer, 0, n);
            }
            gzin.close();
            in.close();
            objectdata=out.toByteArray();
            out.close();
            out=null;
            in=null;
        }catch(Exception ex){
            if (out!=null){
                try{
                    out.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
                out=null;
            }
            if (in!=null){
                try{
                    in.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
                in=null;
            }
             ex.printStackTrace();
            return null;
        }
        if (objectdata==null){
            return null;
        }
        try{
            in = new ByteArrayInputStream(objectdata);
            ObjectInputStream oi = new ObjectInputStream(in);
            Object obj=oi.readObject();
            if (obj instanceof Serializable){
                oi.close();
                in.close();
                return (Serializable)obj;
            }
        }catch(Exception ex){
            if (in!=null){
                try{
                    in.close();
                }catch(Exception e1){
                    e1.printStackTrace();
                }
                in=null;
            }
             ex.printStackTrace();
        }
       return null;
    }
    //对数据进行压缩，并且进行加密
    //加密和压缩失败返回null
    public static byte[] encryptCompress(Context context,String text){
        byte[] dt=null;
        try{
            dt= compress(text);
            if (dt==null){
                return null;
            }
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
       byte[] endata=DESUtil.encrypt(EncryptUtil.getKeyString(context),dt);
       return endata;
    }
    //对数据进行解密，并且进行解压
    //解压和解密失败返回null
    public static String decryptUnCompress(Context context,byte[] data){
        byte[] dsdata=DESUtil.decrypt(EncryptUtil.getKeyString(context),data);
        if (dsdata==null){
            return null;
        }
        return uncompress(dsdata);
    }
    //转换成十六进制字符串
    public static String byte2hex(byte[] b) {
        String hs="";
        String stmp="";
        for (int n=0;n<b.length;n++) {
            stmp=(Integer.toHexString(b[n] & 0XFF));
            if (stmp.length()==1){
                hs=hs+"0"+stmp;
            }else{
                hs=hs+stmp;
            }
        }
        return hs.toUpperCase();
    }
    public static byte[] hex2byte(String str) {
    	byte[] data=new byte[str.length()/2];
        for (int n=0;n<data.length;n++) {
        	String c="" + str.charAt(n*2);
        	 c+= str.charAt(n*2+1);
        	 data[n]=(byte)Integer.parseInt(c,16);
        }
        return data;
    }
    public  static String encrypthexstring(Context context,String text){
        return encrypthexstring(EncryptUtil.getKeyString(context),text);
    }
    public static String decrypthexstring(Context context,String text){
    	try{
    		return decrypthexstring(EncryptUtil.getKeyString(context),text);
    	}catch(Exception ex){
    		ex.printStackTrace();
    		return null;
    	}
    }

    public  static String encrypthexstring(String key,String text){
        byte[] data= DESUtil.encrypt(key,text.getBytes());
        if (data!=null){
        	return byte2hex(data);
        }
        return null;
    }
    public static String decrypthexstring(String key,String text){
        byte[] data=hex2byte(text);
        if (data!=null){
        	byte[] ed=DESUtil.decrypt(key,data);
        	if (ed!=null){
        		return new String(ed);
        	}
        }
        return null;
    }
    public  static String encryptCompresshexstring(String keyString,String text){
        byte[] data= encryptCompress(keyString,text);
        if (data!=null){
        	return byte2hex(data);
        }
        return null;
    }
    public static String decryptUnCompresshexstring(String keyString,String text){
        byte[] data=hex2byte(text);
        if (data!=null){
        	return decryptUnCompress(keyString,data);
        }
        return null;
    }
    public  static byte[] compress(String text) throws IOException{
        return compress(text.getBytes());
    }
    public  static byte[] compress(byte[] data) throws IOException{
        ByteArrayOutputStream out =new ByteArrayOutputStream();
        //建立gzip压缩输出流
        GZIPOutputStream gzout=new GZIPOutputStream(out);
        gzout.write(data);
        gzout.close();//!!!关闭流,必须关闭所有输入输出流.保证输入输出完整和释放系统资源.
       return out.toByteArray(); // .toString("ISO-8859-1");
    }
    public static String uncompress(byte[] data){

        /*ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        //建立gzip压缩输入流
        GZIPInputStream gzin=new GZIPInputStream(in);
        byte[] buffer = new byte[1024];

        int n;
        while ((n = gzin.read(buffer)) >= 0) {
          out.write(buffer, 0, n);
        }
        gzin.close();
        in.close();
        String str=out.toString();
        out.close();
       return str;*/
        try{
            byte[] cd=unzip(data);
            String str=new String(cd);
            return str;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }

    }
    //对数据进行压缩，并且进行加密
    //加密和压缩失败返回null
    public static byte[] encryptCompress(String keyString,String text){
        byte[] dt=null;
        try{
            dt= compress(text);
            if (dt==null){
                return null;
            }
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
       byte[] endata=DESUtil.encrypt(keyString,dt);
       return endata;
    }
    //对数据进行解密，并且进行解压
    //解压和解密失败返回null
    public static String decryptUnCompress(String keyString,byte[] data){
        byte[] dsdata=DESUtil.decrypt(keyString,data);
        if (dsdata==null){
            return null;
        }
        return uncompress(dsdata);
    }
    public static byte[] unzip(byte[] data) throws IOException{

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        //建立gzip压缩输入流
        GZIPInputStream gzin=new GZIPInputStream(in);
        byte[] buffer = new byte[1024];

        int n;
        while ((n = gzin.read(buffer)) >= 0) {
          out.write(buffer, 0, n);
        }
        gzin.close();
        in.close();
        byte[] cd=out.toByteArray();
        out.close();
       return cd;
    }

}
