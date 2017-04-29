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
	/**
	 * id_str : 00010000
	 * tipsmsg :
	 * age_str : 38
	 * sign_status : 0
	 * avatar : http://222.171.202.3:7002/Files/MemberPhoto/1704/c6e56cc60c6742e5986b1639bc2cdc5b_Thumb.jpg
	 * gender : 0
	 * phone_number : 13945686941
	 * author_str : 已认证
	 * login_token : a068b25614254ff88a5ea7491a878185
	 * grade_str : LV1
	 * id : 00010000
	 * avatarstate : 2
	 * share : {"title":"郑冬雪","backurl":"http://222.171.202.3:7002/Zone/PersonalCase.aspx?ID=100","content":"\u201c家·从心出发\u201d是我的人生信条，\u201c年轻\u201d是我的资本，空间利用是我的优势，色彩搭配是我的专业；我是家装设计师XXX。","pic":"http://222.171.202.3:7002/Files/MemberPhoto/1704/c6e56cc60c6742e5986b1639bc2cdc5b_Thumb.jpg"}
	 * name : 郑冬雪
	 */

	private UserInfo
		item;

	public String getUniqueid() {
		return uniqueid;
	}

	public void setUniqueid(String uniqueid) {
		this.uniqueid = uniqueid;
	}

	public UserInfo getItem() {
		return item;
	}

	public void setItem(UserInfo item) {
		this.item = item;
	}

	@Override
	public String toString() {
		return "LoginResult{" +
			   "uniqueid='" + uniqueid + '\'' +
			   ", item=" + item +
			   '}';
	}
}
