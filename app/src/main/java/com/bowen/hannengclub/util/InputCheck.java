package com.bowen.hannengclub.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 肖稳华 on 2017/4/26.
 */

public class InputCheck {


	//是否是合法的值 6-16位的数字和字母
	public static boolean isPassword(String myInput){
		String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(myInput);
		return (matcher.matches());
	}

	/**
	 * 是否手机号码
	 * @param phone
	 * @return
	 */
	public static boolean isPhoneNumber(String phone){
		String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(phone);
		return m.matches();
	}
}
