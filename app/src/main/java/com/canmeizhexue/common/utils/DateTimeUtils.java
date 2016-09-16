package com.canmeizhexue.common.utils;


import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期时间工具类
 * @author silence
 *
 */
public class DateTimeUtils {
	private final static String TAG=DateTimeUtils.class.getName();
	private final static SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final static SimpleDateFormat dayFormat=new SimpleDateFormat("yyyyMMdd");
	private final static SimpleDateFormat dateFormat1=new SimpleDateFormat("yyyyMMddHHmm");
	private final static SimpleDateFormat dateFormat2= new SimpleDateFormat("yyyy年M月d日  HH:mm");
	private final static SimpleDateFormat dateFormat3=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private final static SimpleDateFormat dataFormat4=new SimpleDateFormat("yyyy/MM/dd");
	private final static SimpleDateFormat fullTimeStampFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private final static SimpleDateFormat fulldateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private final static SimpleDateFormat fulltimeFormat=new SimpleDateFormat("hh:mm:ss");
	
	private final static SimpleDateFormat chinesedateFormat=new SimpleDateFormat("yyyy年M月d日");
	 // 短日期格式
	public static String DATE_FORMAT = "yyyy-MM-dd";
	private final static SimpleDateFormat shortTimeFormat=new SimpleDateFormat(DATE_FORMAT);
	 
	 // 长日期格式
	public static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	// 设置时间格式
	private final static SimpleDateFormat formatDurationTime = new SimpleDateFormat("mm:ss");
	
	private final static SimpleDateFormat todayFormat=new SimpleDateFormat("HH:mm");
	private final static SimpleDateFormat yesterdayFormat=new SimpleDateFormat("昨天 HH:mm");
	private final static SimpleDateFormat chineseDateFormat=new SimpleDateFormat("M月d日 HH:mm");
	// 设置时间格式
	private final static SimpleDateFormat formatAdvDurationTime = new SimpleDateFormat("HH:mm");
	/**
	 * "yyyy年M月d日  HH:mm"
	 * @param date
	 * @return
	 */
	public static String getFormat2String(Date date){
		if(date==null){
			date = new Date();
		}
		return dateFormat2.format(date);
	}
	/**
	 * "yyyy/MM/dd HH:mm:ss"
	 * @param date
	 * @return
	 */
	public static String getFormat3String(Date date){
		if(date==null){
			date = new Date();
		}
		return dateFormat3.format(date);
	}
	public static String getfileTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");// 
		return df.format(new Date());// 
	}
	
	/**
	 * 比较两个日期时间大小，date1大于date2时返回为true
	 * */
	public static boolean compareDate(String date1,String date2){
		java.text.DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c1= Calendar.getInstance();
		Calendar c2= Calendar.getInstance();
		try
		{
		c1.setTime(df.parse(date1));
		c2.setTime(df.parse(date2));
		}catch(ParseException e){
		 Log.i(TAG,"error data format");
		}
		int result=c1.compareTo(c2);
		if(result==0){
			Log.i(TAG,"c1=c2");
			return false;
		}
		else if(result<0){
			Log.i(TAG,"c1<c2");
			return false;
		}
		else{
			Log.i(TAG,"c1>c2");
			return true;
		}
		
	}
	
	/**
	  * 将把时和分转全分，11:44-->704
	  * 
	  * @param month
	  * @param day
	  * @return
	  */
	public static int getTimetoMinutes(int month,int day,int hour,int minute){
		return (hour*60+minute);
	}
	
	
	/**
	  * 将日期格式的字符串转换为长整型
	  * 
	  * @param date
	  * @param date
	  * @return
	  */
	 public static long convert2long(String date) {
	    try {
		    SimpleDateFormat sf = new SimpleDateFormat(TIME_FORMAT);
			return sf.parse(date).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	  return 0;
	 }


	 private static final ParsePosition zero_pos = new ParsePosition(0);
	 /**
	  * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
	  * 
	  * @param strDate
	  * @return
	  */
	public static Date strToDateLong(String strDate) {
	  //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  Date strtodate = fulldateFormat.parse(strDate,zero_pos);
	  return strtodate;
	}
	 
	/**
	 * 检查记录是否已经过期
	 * @return 是否已经过期
	 */
	public static boolean checkExpire(Date date,int minute){
		
		if (date==null)
			return true;
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(date);
		c1.add(Calendar.MINUTE, minute); 
		if (c1.compareTo(c2)<=0)
			return true;
		return false;
	}
	/**
	 * 检查记录是否已经过期
	 * @return 是否已经过期
	 */
	public static boolean checkExpireDay(Date date,int day){
		
		if (date==null)
			return true;
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(date);
		c1.add(Calendar.DAY_OF_YEAR, day); 
		if (c1.compareTo(c2)<=0)
			return true;
		return false;
	}
	/**
	 * 检查记录是否已经过期
	 * @return 是否已经过期
	 */
	public static boolean checkSecondExpire(Date date,int second){
		if (date==null)
			return true;
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(date);
		c1.add(Calendar.SECOND, second); 
		if (c1.compareTo(c2)<=0)
			return true;
		return false;
	}

	/**
	 * 获取当前时间已经过了多少秒
	 * @return 过了多少秒
	 */
	public static long getExpireSecond(Date date){
		if (date==null)
			return 0;
		Date d=new Date();
		long t=d.getTime()-date.getTime();
		
		return t/1000;
	}
	/**
	 * 转换为yyyy/MM/dd的格式
	 * @param date
	 * @return
	 */
	public static String convertFormat4String(Date date){
		if (date==null){
			return null;
		}
		return dataFormat4.format(date);
	}
	/**
	 * 转换为yyyy-MM-dd HH:mm的格式
	 * @param date
	 * @return
	 */
	public static String convertDateToString(Date date){
		if (date==null){
			return null;
		}
		return dateFormat.format(date);
	}
	/**
	 * 转换为yyyy-MM-dd HH:mm的格式
	 * @param date
	 * @return
	 */
	public static String convertDateToFullDateString(Date date){
		if (date==null){
			return null;
		}
		return fulldateFormat.format(date);
	}

	/**
	 * 转换为yyyy-MM-dd HH:mm的Date
	 * @param date
	 * @return
	 */
	public static Date convertDateWithStandardTime(Date date){
		if (date==null){
			return null;
		}
		String dateTime=fulldateFormat.format(date);
		return parseFullDateString(dateTime);
	}
	/**
	 * 从yyyy-MM-dd HH:mm转换
	 * @param dateTime
	 * @return
	 */
	public static Date parseDateString(String dateTime){
		if (dateTime==null){
			return null;
		}
		try {
			return dateFormat.parse(dateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 从yyyy-MM-dd HH:mm转换
	 * @param dateTime
	 * @return
	 */
	public static Date parseFullDateString(String dateTime){
		if (dateTime==null){
			return null;
		}
		try {
			return fulldateFormat.parse(dateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	/** 
	 * 
	 * @param dateTime 如果为null，默认是当天
	 * @return yyyy-MM-dd 格式字符串
	 */
	public static String convertShortTime(Date dateTime){
		if(dateTime==null){
			dateTime = Calendar.getInstance().getTime();
		}
		return shortTimeFormat.format(dateTime);
	}
	/**
	 * 
	 * @param dateTime yyyy-MM-dd 格式字符串
	 * @return 
	 */
	public static Date parseShortTimeString(String dateTime){
		try {
			if(!TextUtils.isEmpty(dateTime)){
				return  shortTimeFormat.parse(dateTime);
			}
		} catch (Exception e) {
			
		}

		return Calendar.getInstance().getTime();
	}
	/**
	 * 转换成hh:mm:ss格式
	 * @param dateTime
	 * @return
	 */
	public static String convertToFullTime(Date dateTime){
		if (dateTime==null){
			return null;
		}
		return fulltimeFormat.format(dateTime);
	}
	
	public static String getNow(){
		return convertDateToString(new Date());
	}
	public static String getDayValue(){
		return dayFormat.format(new Date());
	}
	/**
	 * 获取day
	 * @param days 偏移的天数，可为负数
	 * @return
	 */
	public static String getDayValue(int days){
		if (days==0){
			return getDayValue();
		}
		Calendar cal = Calendar.getInstance();//使用默认时区和语言环境获得一个日历。   
		cal.add(Calendar.DAY_OF_YEAR, days);
		return dayFormat.format(cal.getTime());
	}
	public static String getTimeFileName(){
		return dateFormat1.format(new Date());
	}
	public static String getFullTimeStamp(){
		return fullTimeStampFormat.format(new Date());
	}
	public static String getDurationTime(Date date){
		return formatDurationTime.format(date);
	}
	
	public static String getAdvDurationTime(Date date){
		return formatAdvDurationTime.format(date);
	}
	
	/**
	 * 
	 * @return HH:mm
	 */
	public static String getNowTime(){
		return todayFormat.format(new Date());
	}
	
	/**
	 * 转换成年月日这样的中文时间
	 * @param date 要转换的Date对象
	 * @return 年月日这样的中文时间
	 */
	public static String convertToChineseString(Date date){
		if (date==null){
			return null;
		}
			return chinesedateFormat.format(date);		
	}
	
	 /** 
     * 长整型转换为日期类型    
     * @param longTime longTime 长整型时间
     * @param dataFormat dataFormat 时间格式
     * @return String 长整型对应的格式的时间 
     */  
    public static String long2String(long longTime,String dataFormat)  
    {  
        Date d = new Date(longTime);  
        SimpleDateFormat s = new SimpleDateFormat(dataFormat);  
        String str = s.format(d);  
        return str;  
  
    }  
	
	
	/**转换成中文显示时间
       a、当天 “HH:mm”，如： 17：15
       b、昨天 “昨天 hh:mm” ，如：昨天 17：15
       c、今年内 “月-日”，如：8月8日
       e、更久 “年-月-日” 2012年7月10日
	 */
	public static String getChineseDateText(Date date){
		//Log.v(TAG, "year1=" + calendar.get(Calendar.YEAR) + ";year2=" + .getYear() +
		//		";month1=" + calendar.get(Calendar.MONTH) + ";month2=" + date.getMonth() +
		//		";day1=" + calendar.get(Calendar.DAY_OF_MONTH) + ";day2=" + date.getDate());
		//
		Calendar mCalendarToday = Calendar.getInstance();
		Calendar mCalendarYesterday= Calendar.getInstance();
		mCalendarYesterday.add(Calendar.DATE, -1);
		if (mCalendarToday.get(Calendar.YEAR)==(date.getYear() +1900)&&
				mCalendarToday.get(Calendar.MONTH)==date.getMonth()&&
						mCalendarToday.get(Calendar.DAY_OF_MONTH)==date.getDate()){
			return todayFormat.format(date);
		}
		if (mCalendarYesterday.get(Calendar.YEAR)==(date.getYear() +1900)&&
				mCalendarYesterday.get(Calendar.MONTH)==date.getMonth()&&
						mCalendarYesterday.get(Calendar.DAY_OF_MONTH)==date.getDate()){
			return yesterdayFormat.format(date);
		}
		if (mCalendarToday.get(Calendar.YEAR)==(date.getYear() +1900)){
			return chineseDateFormat.format(date);
		}
		return DateTimeUtils.convertDateToString(date);
	}
	
}
