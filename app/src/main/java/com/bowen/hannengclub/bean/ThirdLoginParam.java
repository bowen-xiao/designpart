package com.bowen.hannengclub.bean;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by 肖稳华 on 2017/5/8.
 * 第三方登录参数传递
 */
public class ThirdLoginParam implements Serializable {
	Map<String, Object> params;

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
}
