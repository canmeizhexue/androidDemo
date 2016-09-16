package com.canmeizhexue.common.utils;

import android.content.Context;

import java.util.concurrent.atomic.AtomicReference;

public class EncryptUtil {	
	private static final String TAG="EncryptUtil";
	private static AtomicReference<String> keyStringReference=new AtomicReference<String>();
	public static String getKeyString(Context context){		
		String key=keyStringReference.get();
		if (key!=null){
			return key;
		}

		synchronized(keyStringReference){
			key=keyStringReference.get();
			if (key!=null){
				return key;
			}
			
			key="canmeizhexue android test key";
			
			keyStringReference.set(key);
			return key;
		}
	}
	public static String getKeyString(Context context,String name){		
		String key=getKeyString(context);
		int len=name.length();
		int length=key.length();
		String text= key.substring(0, 10) + name;
		if (text.length()==length){
			return text;
		}else if (text.length()>length){
			return text.substring(0,length);
		}else{
			return text + key.substring(text.length());
		}
	}
}
