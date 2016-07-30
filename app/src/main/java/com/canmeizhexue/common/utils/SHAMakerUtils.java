package com.canmeizhexue.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**散列算法一般都是   字符串---->散列得到的字节数组---->字节数组的字符串形式
 * SHAR摘要
 */

public final class SHAMakerUtils {
    
    private SHAMakerUtils(){}
    public static void main(String[]args){
    	String originalString="获取字符串摘要信息";
    	byte[] array= sha256(originalString);
    	String decodeString = byteToHexString(array);
    	System.out.println(decodeString);
    	System.out.println(decodeString.equalsIgnoreCase("8c891e63fbc28b5fdf5b036f6964e48deb9ab2a334f49c057ee3b0eb6a7b21dc"));
    }

    /**
     * 获取字符串摘要信息
     * @param str 字符串
     * @return
     */
    public static byte[] sha256(String str){

        if (str == null){
            return null;
        }

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(str.getBytes("UTF-8"));
            return hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }
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



}
