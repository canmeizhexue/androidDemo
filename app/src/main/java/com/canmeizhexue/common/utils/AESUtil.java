package com.canmeizhexue.common.utils;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加解密,密钥长度有特殊要求
 * @author zengyaping
 *
 */
public class AESUtil {
	private static final String TRANSFORMATION="AES/ECB/PKCS5Padding";
	public static void main(String[]args){
		
		try {
			String plainText="使用AES算法加密";
			byte[] key = calculateKey("123", "6980ttttgg4h45w4h4h433444", "AES");
			System.out.println("key length is "+key.length);
			//AES算法的key的长度有特殊要求,16
			//http://stackoverflow.com/questions/29354133/how-to-fix-invalid-aes-key-length
			String encodedText = aESencrypt(plainText, key);
			System.out.println(encodedText);
			System.out.println(aESdecrypt(encodedText, key));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    /**
     * 使用AES算法加密
     *
     * @param content 待加密内容
     * @param key     加密密匙
     * @return 加密后的字符串
     * @throws Exception 加密异常
     */

    public static String aESencrypt(String content, byte[] key) throws Exception {
        try {
            Cipher aesECB = Cipher.getInstance(TRANSFORMATION);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            aesECB.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] result = aesECB.doFinal(content.getBytes("UTF-8"));
            //return Base64Util.bytesToBase64(result);
            return Base64.encodeBytes(result, Base64.NO_OPTIONS);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 解密
     *
     * @param content 待解密内容,base64编码
     * @param key     解密密钥
     * @return 解密后的字符串
     * @throws Exception 解密异常
     */
    public static String aESdecrypt(String content, byte[] key) throws Exception {
        if (content == null) {
            throw new IllegalArgumentException();
        }

        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);// 创建密码器
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);// 初始化
            //byte[] result = Base64Util.base64ToBytes(content);
            byte[] result = Base64.decode(content, Base64.NO_OPTIONS);
            return new String(cipher.doFinal(result), "UTF-8"); // 解密
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }
    /**
     * 原始密钥生成一个新的特定长度的密钥
     *
     * @author jicexosl
     */
    private static byte[] calculateKey(String token, String aesBaseKey, String cryptoType) {

        // SHA-256算法生成32字节的字节数组
        byte[] shaBytes = SHAMakerUtils.sha256(token.concat(aesBaseKey));

        int keyLength = 0;
        if ("AES".equals(cryptoType)) {
            keyLength = 8;
        } else if ("3DES".equals(cryptoType)) {
            keyLength = 24;
        } else {
            return null;
        }

        byte[] key = new byte[keyLength];
        System.arraycopy(shaBytes, 0, key, 0, keyLength);
        try {
            //转换成十六进制字符串后再取字节数组
            //return Hex.encodeHexString(key).getBytes("UTF-8");
            return HexUtils.encodeHexStr(key).getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
