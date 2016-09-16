package com.canmeizhexue.common.utils;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class Utils {
	private   final   static   DecimalFormat   nf   =   new   DecimalFormat( "###,##0.00 "); 
	
	  public static String getLocalMacAddress(Context context) {  
	    WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);  
	    WifiInfo info = wifi.getConnectionInfo();  
	    return info.getMacAddress();  
	  }
	  
		public static String getDeviceImei(Context context) {
			TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			if (telephonyManager == null ||TextUtils.isEmpty(telephonyManager.getDeviceId())) {
				 return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);   
			} else {
				return telephonyManager.getDeviceId();
			}
		}
	  public static String formatDecimal(float value){
		  return nf.format(value);
	  }
	  

		public static String getParamsValue(String params, String name) {
			if (params==null||name==null){
				return null;
			}
			char splitChar='\t';
			String findStr=name + "=";
			if (params.startsWith(findStr)){
				int valueStart = findStr.length();
				if (valueStart >= params.length()) {
					return null;
				}
				int endIndex = params.indexOf(splitChar, valueStart);
				if (endIndex > 0) {
					return params.substring(valueStart, endIndex);
				} else {
					return params.substring(valueStart);
				}
			}else{
				findStr=splitChar + findStr;
				int index = params.indexOf(findStr);
				if (index >= 0) {
					int valueStart = index + findStr.length();
					if (valueStart >= params.length()) {
						return null;
					}
					int endIndex = params.indexOf(splitChar, valueStart);
					if (endIndex > 0) {
						return params.substring(valueStart, endIndex);
					} else {
						return params.substring(valueStart);
					}
				}
			}
			return null;
		}
		public static String getParamsValue(String params, String name,
				char splitChar) {
			if (params==null||name==null){
				return null;
			}
			int index = params.indexOf(name);
			if (index >= 0) {
				int valueStart = index + name.length() + 1;
				if (valueStart >= params.length()) {
					return null;
				}
				int endIndex = params.indexOf(splitChar, valueStart);
				if (endIndex > 0) {
					return params.substring(valueStart, endIndex);
				} else {
					return params.substring(valueStart);
				}
			}
			return null;
		}
		public static String getParamsValue(String params, String name,
				String splitString) {
			if (params==null||name==null){
				return null;
			}
			int index = params.indexOf(name);
			if (index >= 0) {
				int valueStart = index + name.length() + 1;
				if (valueStart >= params.length()) {
					return null;
				}
				int endIndex = params.indexOf(splitString, valueStart);
				if (endIndex > 0) {
					return params.substring(valueStart, endIndex).trim();
				} else {
					return params.substring(valueStart).trim();
				}
			}
			return null;
		}
		public static int getParamsIntValue(String params, String name,String splitString,
				int defaultValue) {
			String val = getParamsValue(params, name,splitString);
			if (val == null) {
				return defaultValue;
			}
			try {
				return Integer.parseInt(val);
			} catch (Exception e) {
				return defaultValue;
			}
		}
		public static int getParamsIntValue(String params, String name,
				int defaultValue) {
			String val = getParamsValue(params, name);
			if (val == null) {
				return defaultValue;
			}
			try {
				return Integer.parseInt(val);
			} catch (Exception e) {
				return defaultValue;
			}
		}
		
		public static boolean isEmailAddress(String strEmail) {
			String strPattern = "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?$";
			Pattern p = Pattern.compile(strPattern);
			Matcher m = p.matcher(strEmail);
			if(m.matches()){
				return true;
			}
			return false;
		}
		
		/**
		 *  
		 * @param 字符串
		 * @return 字符串中所含中文字的数目
		 */
		public static int getChineseCharNumber(String text){
			int count = 0;
			String ChineseRegEx = "[\\u4e00-\\u9fa5]";
			Pattern p = Pattern.compile(ChineseRegEx);
			Matcher m = p.matcher(text);
			while (m.find()) {
				for (int i = 0; i <= m.groupCount(); i++) {
					count = count + 1;
				}
			}
			return count;
		}
		
		/**
		 * @param 字符串
		 * @return 字符串中所含英文字符或数字的数目
		 */
		public static int getEngllishOrDigitNumber(String text){
			int count = 0;
			String regEx = "[A-Za-z0-9]";
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(text);
			while (m.find()) {
				for (int i = 0; i <= m.groupCount(); i++) {
					count = count + 1;
				}
			}
			return count;
		}
		
		public static void main(String[] argc){
			//String text="errorcode:0";
			//System.out.println(getParamsIntValue(text,"errorcode",'\n',-1));
		}
}
