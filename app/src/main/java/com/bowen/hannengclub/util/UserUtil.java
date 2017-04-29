package com.bowen.hannengclub.util;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.bowen.hannengclub.SysConfiguration;
import com.bowen.hannengclub.bean.UserInfo;

/**
 * Created by 肖稳华 on 2017/4/29.
 */

public class UserUtil {

		public static UserInfo getUserInfo(Context context){
			UserInfo result = null;
			try {
				String user = CacheUtils.getString(context, SysConfiguration.USER_INFO);
				if(TextUtils.isEmpty(user)){
					result = null;
				}else{
					result = JSON.parseObject(user,UserInfo.class);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

	public static String getToken(Context context){
		String result = null;
		UserInfo info = getUserInfo(context);
		if(info!= null){
			result = info.getLogin_token();
		}
		return result;
	}
}
