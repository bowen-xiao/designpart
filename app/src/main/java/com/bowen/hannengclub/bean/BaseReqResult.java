package com.bowen.hannengclub.bean;

/**
 * Created by 肖稳华 on 2017/4/28.
 */

public class BaseReqResult {
//
//	status	int	请求状态
//	errmsg	string	错误信息


	protected int status;
	protected String errmsg;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
}
