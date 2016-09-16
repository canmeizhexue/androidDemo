package com.canmeizhexue.common.utils;

import java.io.UnsupportedEncodingException;

public class ByteUtils {

	/**
	 * floatToByte -- 浮点数（定点小数，2字节整数部分，2字节小数部分）到字节数组的转换
	 * @param   buf 转化后字节数组
	 * @param start  字节数组开始的位置
	 * @param f      浮点数
	 * @return int   返回字节数组结束的位置
	 */		
	public static int floatToByte(byte[] buf, int start, float f){
		f *= 0x10000;
		int li = (int)f;
	
		return intToByte(buf, start, li);
	}
	
	/**
	 * byteToFloat -- 字节数组到浮点数（定点小数，2字节整数部分，2字节小数部分）的转换
	 * @param buf  字节数组
	 * @param start  字节数组开始的位置
	 * @return float 转换出来的浮点数
	 */		
	public static float byteToFloat(byte[] buf, int start){
		float f = byteToInt(buf, start);
		f /= 0x10000;
	
		return f;
	}
	/**
	 * intToByte -- 长整形数到字节数组的转换
	 * @param buf  转化后字节数组
	 * @param start  字节数组开始的位置
	 * @param li     需要转化的整型
	 * @return int   返回字节数组结束的位置
	 */	
	public static int intToByte(byte[] buf, int start, int li){
		boolean flg = false;
		if (li < 0)
		{
			flg = true;
			li = 0 - li;
		}
	
		int len = start;
	
		buf[len++] = (byte)((li >> 24) & 0xff);
		buf[len++] = (byte)((li >> 16) & 0xff);
		buf[len++] = (byte)((li >>  8) & 0xff);
		buf[len++] = (byte)( li        & 0xff);
	
		if (flg)
			buf[start] = (byte)(buf[start] | 0x80);
	
		return len;
	}
	
	/**
	 * byteToInt -- 字节数组到长整形数的转换
	 * @param buf  字节数组
	 * @param start  字节数组开始的位置
	 * @return int   转换出来的无符号长整形数
	 */	
	public static int byteToInt(byte[] buf, int start){
		boolean flg = (buf[start] & 0x80) != 0;
	
		int li = buf[start];
		li = li & 0x7f;
	
		for (int i=1; i<4; i++)
		{
			int k = buf[start+i];
			k = k & 0xff;
	
			li = (li << 8) | k;
		}
	
		if (flg)
			li = 0 - li;
	
		return li;
	}

	/**
	 * uIntToByte -- 无符号长整形数到字节数组的转换
	 * @param buf  转化后字节数组
	 * @param start  字节数组开始的位置
	 * @param li     需要转化的整型
	 * @return int   返回字节数组结束的位置
	 */
	public static int uIntToByte(byte[]buf, int start, int li){
		int len = start;
	
		buf[len++] = (byte)((li >> 24) & 0xff);
		buf[len++] = (byte)((li >> 16) & 0xff);
		buf[len++] = (byte)((li >>  8) & 0xff);
		buf[len++] = (byte)( li        & 0xff);
	
		return len;
	}
	
	/**
	 * byteToUInt -- 字节数组到无符号长整形数的转换
	 * @param buf 字节数组
	 * @param start  字节数组开始的位置
	 * @return int   转换出来的无符号长整形数
	 */
	public static int byteToUInt(byte buf[], int start){
		int li = 0;
	
		for (int i=0; i<4; i++)
		{
			int k = buf[start+i];
			k = k & 0xff;
	
			li = (li << 8) | k;
		}
	
		return li;
	}
	
	
	
	
	// ，start为开始位置，返回值为转换结束后的位置
	/**
	 * shortToByte -- 短整形数到字节数组的转换
	 * @param buf  转化后字节数组
	 * @param start  字节数组开始的位置
	 * @param si     需要转化的短整型
	 * @return int   返回字节数组结束的位置
	 */		
	public static int shortToByte(byte buf[], int start, short si){
		boolean flg = false;
		if (si < 0)
		{
			flg = true;
			si = (short)(0 - si);
		}
	
		int len = start;
	
		buf[len++] = (byte)((si >> 8) & 0xff);
		buf[len++] = (byte)( si       & 0xff);
	
		if (flg)
			buf[start] = (byte)(buf[start] | 0x80);
	
		return len;
	}
	
	/**
	 * byteToShort -- 字节数组到短整形数的转换
	 * @param buf  字节数组
	 * @param start  字节数组开始的位置
	 * @return short 转换出来的短整形数
	 */		
	public static short byteToShort(byte buf[], int start){
		boolean flg = (buf[start] & 0x80) != 0;
	
		int li = buf[start];
		li = li & 0x7f;
	
		int k = buf[start+1];
		k = k & 0xff;
	
		li = (li << 8) | k;
	
		if (flg)
			li = 0 - li;
	
		return (short)li;
	}
	
	
	/**
	 * byteToByte -- 求字节的子字节
	 * @param buf  字节数组
	 * @param start  字节数组开始的位置
	 * @param len    需要子字节的长度
	 * @return byte[]  子字节数组
	 */		
	public static byte[] byteToByte(byte[] buf, int start, int len){
		byte[] temp = new byte[len];
		System.arraycopy(buf, start, temp, 0, len);
		return temp;
	}
	
	
	/**
	 * byte2Float -- 字节数组到浮点数的转换
	 * @param buf  字节数组
	 * @param start  字节数组开始的位置
	 * @return float 转换出来的浮点数
	 */		
	public static float byte2Float(byte buf[], int start){
		int len = start;
	
		byte [] ch = new byte[4];
		for (int i=0; i<4; i++)
			ch[3-i] = buf[len++];
	
		int i = byteToUInt(ch, 0);
	
		return Float.intBitsToFloat(i);
	}
	
	
	/**
	 * byteToIp -- 字节数组到IP字符串的转换，这里按 大端处理的 
	 * @param buf  字节数组
	 * @param start  字节数组开始的位置
	 * @return String 转换出来的ip字符串
	 */		
	public static String byteToIp(byte[] buf, int start){
		String strIp = new String();
		int temp;
		for (int i=0; i<4; i++)
		{
			temp = buf[start+i];
			if (temp < 0)
			{
				temp += 0x100;
			}
			strIp += Integer.toString(temp) ;
			if (i < 3)
			{
				strIp += ".";
			}
		}
		return strIp;
	}
	
	/**
	 * ipToByte -- IP 字符串到字符数组的转换
	 * @param buf  转化后字节数组
	 * @param start  字节数组开始的位置
	 * @param strIp  IP 字符串
	 * @return int   返回字节数组结束的位置
	 */		
	public static int ipToByte(byte[] buf, int start, String strIp){
		int len;
		len = start;
		
		int temp;
		String str[] = strIp.split("\\.");
		for (int i=0; i<4; i++)
		{
			temp = Integer.parseInt(str[i]);
			if (temp >= 0x80)
			{
				temp -= 0x100;
			}
			buf[len++] = (byte)temp;
		}
		
		return len;
	}
	
	/**
	 * stringToCArray -- java字符串按c字符串方式解析
	 *  String 遇到第一个'\0'，只要前面部分的内容
	 *  @param str       java的字符串
	 *  @return String   转化为c的字符串
	 */
	public static String stringToCArray(String str){
		int pos = str.indexOf(0);
		if (pos != -1)
		{
			str = str.substring(0, pos);
		}
		return str;
	}
	
	/**
	 * byteEncoding -- 封装String的一个构造函数，避免重复捕获异常
	 * @param bytes 字节数组
	 * @param offset 字节数组开始的位置
	 * @param length 待处理的字节数组的长度
	 * @return 转化后的字符串
	 */
	public static String byteEncoding(byte[] bytes, int offset, int length){
		String str = new String();
		try {
			str = new String(bytes, offset, length, "GB2312");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
	
	/**
	 * byteDecoding -- 封装String的getBytes()方法，避免重复捕获异常
	 * @param str 待处理的字符串
	 * @return 字节数组
	 */
	public static byte[] byteDecoding(String str){
		byte[] buf = new byte[0];
		try {
			buf = str.getBytes("GB2312");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return buf;
	}
}
