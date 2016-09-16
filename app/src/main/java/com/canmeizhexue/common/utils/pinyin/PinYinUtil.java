package com.canmeizhexue.common.utils.pinyin;


import com.canmeizhexue.common.utils.pinyin.HanziToPinyin.Token;

import java.util.ArrayList;

/**
 * 拼音处理类
 * @author canmeizhexue
 *
 */
public class PinYinUtil {
	/**
	 * 汉字返回拼音，字母原样返回，如张三 返回ZHANGSAN  张三Abc则返回ZHANGSANAbc
	 * @param input
	 * @return
	 */
	public static String getPinYin(String input) {
		ArrayList<HanziToPinyin.Token> tokens = HanziToPinyin.getInstance().get(input);
		StringBuilder sb = new StringBuilder();
		if (tokens != null && tokens.size() > 0) {
			for (Token token : tokens) {
				if (Token.PINYIN == token.type) {
					//Log.v("PinYinUtil", token.target);
					sb.append(token.target);
				} else {
					//Log.v("PinYinUtil", token.source);
					sb.append(token.source);
				}
			}
		}
		return sb.toString();
	}
	/**
	 * 汉字返回拼音的前缀，字母原样返回,如张三 返回ZS  张三Abc则返回ZSAbc
	 * @param input
	 * @return
	 */
	public static String getPinYinPrefix(String input) {
		ArrayList<Token> tokens = HanziToPinyin.getInstance().get(input);
		StringBuilder sb = new StringBuilder();
		if (tokens != null && tokens.size() > 0) {
			for (Token token : tokens) {
				if (Token.PINYIN == token.type) {
					//Log.v("PinYinUtil", token.target);
					sb.append(token.target.substring(0, 1));
				} else {
					//Log.v("PinYinUtil", token.source);
					sb.append(token.source);
				}
			}
		}
		return sb.toString().toLowerCase();
	}
}
