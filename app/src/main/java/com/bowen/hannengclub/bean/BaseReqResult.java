package com.bowen.hannengclub.bean;

import java.io.Serializable;

/**
 * Created by 肖稳华 on 2017/4/28.
 */

public class BaseReqResult implements Serializable{
//
//	status	int	请求状态
//	errmsg	string	错误信息


	@Override
	public String toString() {
		return "BaseReqResult{" +
			   "status=" + status +
			   ", errmsg='" + errmsg + '\'' +
			   '}';
	}

	protected int    status;
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
