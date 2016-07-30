package com.canmeizhexue.common.utils;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * <p>hash散列算法的一种，或者说摘要算法的一种</p>
 *<p>将字符串的utf-8字节数组进行摘要，然后将得到的字节数组转换成16进制的字符串</p>
 * @author canmeizhexue<br/>
 * @version 1.0 (16/4/21 下午9:53)<br/>
 */
public class MD5Util {


    /**
     * 将指定byte数组转换成16进制字符串
     *
     * @param b
     * @return
     */
    private static String byteToHexString(byte[] b) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            hexString.append(hex.toUpperCase());
        }
        return hexString.toString();
    }


    /**
     *  生成摘要串
     * @param str 内容
     * @return
     */
    public static String md5(String str) {
        String encodeStr = null;
        try {
            //声明消息摘要对象
            MessageDigest md = null;
            //创建消息摘要
            md = MessageDigest.getInstance("MD5");
            //将口令的数据传给消息摘要对象
            md.update(str.getBytes("UTF-8"));
            //获得消息摘要的字节数组
            byte[] digest = md.digest();

            //将字节数组格式加密后的口令转化为16进制字符串格式的口令
            encodeStr = byteToHexString(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        return encodeStr;
    }


}
