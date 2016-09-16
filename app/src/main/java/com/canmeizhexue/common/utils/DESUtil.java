package com.canmeizhexue.common.utils;

import android.text.TextUtils;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DESUtil {
    private static final String Algorithm = "AES"; //定义 加密算法,可用 DES,DESede,Blowfish  

    public static byte[] encrypt(String password, byte[] bytes) {
    	try{
	        byte[] rawKey = getRawKey(password.getBytes());
	        byte[] result = encrypt(rawKey, bytes);
	        return result;
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    	return null;
	}
	
	public static byte[] decrypt(String password, byte[] bytes) {
    	try{
	        byte[] rawKey = getRawKey(password.getBytes());
	        byte[] result = decrypt(rawKey, bytes);
	        return result;
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    	return null;
	}
	
	private static byte[] getRawKey(byte[] seed) {
    	try{
	        KeyGenerator kgen = KeyGenerator.getInstance("AES");
	        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");  
	        sr.setSeed(seed);
		    kgen.init(128, sr); // 192 and 256 bits may not be available
		    SecretKey skey = kgen.generateKey();
		    byte[] raw = skey.getEncoded();
		    return raw;
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    	return null;
	}
	
	
	private static byte[] encrypt(byte[] raw, byte[] clear) {
    	try{
    		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
	        Cipher cipher = Cipher.getInstance("AES");
		    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		    byte[] encrypted = cipher.doFinal(clear);
	        return encrypted;
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    	return null;
	}
	
	private static byte[] decrypt(byte[] raw, byte[] encrypted) {
    	try{
    		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
	        Cipher cipher = Cipher.getInstance("AES");
		    cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		    byte[] decrypted = cipher.doFinal(encrypted);
	        return decrypted;
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    	return null;
	}
	
	public static String toHex(String txt) {
	        return toHex(txt.getBytes());
	}
	public static String fromHex(String hex) {
	        return new String(toByte(hex));
	}
	
	public static byte[] toByte(String hexString) {
	        int len = hexString.length()/2;
	        byte[] result = new byte[len];
	        for (int i = 0; i < len; i++)
	                result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();
	        return result;
	}
	
	public static String toHex(byte[] buf) {
	        if (buf == null)
	                return "";
	        StringBuffer result = new StringBuffer(2*buf.length);
	        for (int i = 0; i < buf.length; i++) {
	                appendHex(result, buf[i]);
	        }
	        return result.toString();
	}
	private final static String HEX = "0123456789ABCDEF";
	private static void appendHex(StringBuffer sb, byte b) {
	        sb.append(HEX.charAt((b>>4)&0x0f)).append(HEX.charAt(b&0x0f));
	}

    public static String encrypthexstring(String keyString, String text) {
		try {
			 byte[] data = encrypt(keyString, text.getBytes());
	        if (data != null) {
	            return byte2hex(data);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }

    public static String decrypthexstring(String keyString, String text) {
        byte[] data = hex2byte(text);
        if (data != null) {
			try {
				 byte[] ed = decrypt(keyString, data);
		            if (ed != null) {
		                return new String(ed);
		            }
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        return null;
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }

    public static byte[] hex2byte(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        byte[] data = new byte[str.length() / 2];
        for (int n = 0; n < data.length; n++) {
            String c = "" + str.charAt(n * 2);
            c += str.charAt(n * 2 + 1);
            data[n] = (byte) Integer.parseInt(c, 16);
        }
        return data;
    }

	public static String encrptString(String text){
		if (TextUtils.isEmpty(text)){
			return text;
		}
		String str = getUserCheckString();
		//System.out.println("getUserCheckString:"+str);
		//System.out.println("text:"+text);
		String str2 = encrypthexstring(str, text);
		//System.out.println("encrypthexstring:"+str2);
		return str2;
	}
	public static String decryString(String text){
		if (TextUtils.isEmpty(text)){
			return text;
		}
		String str = getUserCheckString();
		//System.out.println("getUserCheckString:"+str);
		//System.out.println("text:"+text);
		String c= decrypthexstring(str, text);
		//System.out.println("decryString:"+c);
		return c;
	}

	public static String getUserCheckString(){
		StringBuilder sb=new StringBuilder("azclk");
		int index=2;
		sb.append(index);
		index++;
		sb.append(index);
		index++;
		sb.append(index);
		index=0;
		sb.append(index);
		sb.append("rszzw");
		index+=9;
		sb.append(index);
		sb.append(' ');
		index-=7;
		sb.append(index);
		char p='h';
		sb.append(p);
		sb.append(p);
		index+=59;
		sb.append(index);
		sb.append("yz");
		index=8;
		sb.append(index);		
		return sb.toString();
	}

}
