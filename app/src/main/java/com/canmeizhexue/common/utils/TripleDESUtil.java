package com.canmeizhexue.common.utils;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class TripleDESUtil {

	private static final String Algorithm = "DESede"; // 定义 加密算法,可用
														// DES,DESede,Blowfish

	// keybyte为加密密钥，长度为24字节
	// src为被加密的数据缓冲区（源）
	public static byte[] encrypt(String keyString, byte[] src) {
		try {
			byte[] keybyte = keyString.getBytes();
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			// 加密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	// keybyte为加密密钥，长度为24字节
	// src为加密后的缓冲区
	public static byte[] decrypt(String keyString, byte[] src) {
		try {
			byte[] keybyte = keyString.getBytes();
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			// 解密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.DECRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	public static byte[] deOrderBytes(byte[] data) {
		if (data == null || data.length == 0) {
			return data;
		}
		int len = data.length;
		short[] xor = new short[] { 0xD2, 0x6C, 0x37, 0x9C };
		int cpos = 5;
		int index = 0, opos = cpos;
		for (int i = 0; i < len; i++) {
			if (i % opos == 0) {
				data[i] = (byte) (data[i] ^ xor[index]);
				index++;
				if (index >= 4) {
					index = 0;
				}
				opos = cpos + index;
			}
		}
		return data;
	}

	/**
	 * 
	 * ECB加密,不要IV
	 * 
	 * @param key
	 *            密钥
	 * 
	 * @param data
	 *            明文
	 * 
	 * @return Base64编码的密文
	 * 
	 * @throws Exception
	 */

	public static byte[] des3EncodeECB(byte[] key, byte[] data)

	throws Exception {

		Key deskey = null;

		DESedeKeySpec spec = new DESedeKeySpec(key);

		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");

		deskey = keyfactory.generateSecret(spec);

		Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");

		cipher.init(Cipher.ENCRYPT_MODE, deskey);

		byte[] bOut = cipher.doFinal(data);

		return bOut;

	}

	/**
	 * 
	 * ECB解密,不要IV
	 * 
	 * @param key
	 *            密钥
	 * 
	 * @param data
	 *            Base64编码的密文
	 * 
	 * @return 明文
	 * 
	 * @throws Exception
	 */

	public static byte[] des3DecodeECB(byte[] key, byte[] data)

	throws Exception {

		Key deskey = null;

		DESedeKeySpec spec = new DESedeKeySpec(key);

		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");

		deskey = keyfactory.generateSecret(spec);

		Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");

		cipher.init(Cipher.DECRYPT_MODE, deskey);

		byte[] bOut = cipher.doFinal(data);

		return bOut;

	}

	/**
	 * 
	 * CBC加密
	 * 
	 * @param key
	 *            密钥
	 * 
	 * @param keyiv
	 *            IV
	 * 
	 * @param data
	 *            明文
	 * 
	 * @return Base64编码的密文
	 * 
	 * @throws Exception
	 */

	public static byte[] des3EncodeCBC(byte[] key, byte[] keyiv, byte[] data)

	throws Exception {

		Key deskey = null;

		DESedeKeySpec spec = new DESedeKeySpec(key);

		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");

		deskey = keyfactory.generateSecret(spec);

		Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");

		IvParameterSpec ips = new IvParameterSpec(keyiv);

		cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);

		byte[] bOut = cipher.doFinal(data);

		return bOut;

	}

	/**
	 * 
	 * CBC解密
	 * 
	 * @param key
	 *            密钥
	 * 
	 * @param keyiv
	 *            IV
	 * 
	 * @param data
	 *            Base64编码的密文
	 * 
	 * @return 明文
	 * 
	 * @throws Exception
	 */

	public static byte[] des3DecodeCBC(byte[] key, byte[] keyiv, byte[] data)

	throws Exception {

		Key deskey = null;

		DESedeKeySpec spec = new DESedeKeySpec(key);

		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");

		deskey = keyfactory.generateSecret(spec);

		Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");

		IvParameterSpec ips = new IvParameterSpec(keyiv);

		cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

		byte[] bOut = cipher.doFinal(data);

		return bOut;

	}

	/**
	 * 把16进制字符串转换成字节数组
	 * 
	 * @param hex
	 * @return
	 */
	public static byte[] hexStringToByte(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}

	private static byte toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}

	/**
	 * 把字节数组转换成16进制字符串
	 * 
	 * @param bArray
	 * @return
	 */
	public static final String bytesToHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}

}
