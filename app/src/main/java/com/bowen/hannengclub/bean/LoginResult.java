package com.bowen.hannengclub.bean;

/**
 * Created by 肖稳华 on 2017/4/28.
 */

public class LoginResult extends BaseReqResult{

	/**
	 * status : 0
	 * errmsg : 签名验证错误，不合法的请求！
	 * uniqueid : 3468721a-04b0-4e71-8ce1-af56c662ef2a
	 */

	private String uniqueid;

	@Override
	public String toString() {
		return "LoginResult{" +
			   "status=" + status +
			   ", errmsg='" + errmsg + '\'' +
			   ", uniqueid='" + uniqueid + '\'' +
			   '}';
	}


	public String getUniqueid() {
		return uniqueid;
	}

	public void setUniqueid(String uniqueid) {
		this.uniqueid = uniqueid;
	}
}
