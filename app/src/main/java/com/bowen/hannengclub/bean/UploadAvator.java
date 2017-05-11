package com.bowen.hannengclub.bean;

/**
 * Created by 肖稳华 on 2017/4/29.
 */

public class UploadAvator extends BaseReqResult {

	/**
	 * avatar : http://222.171.202.3:7002/Files/MemberPhoto/1704/98035aff8bc14afaa0d75bd6dfcee870_Thumb.jpg
	 * uniqueid : 8c8598ee-c7bc-4040-b23f-8ab6c9757c46
	 */

	private String avatar;
	private String uniqueid;
	/**
	 * avatar : http://222.171.202.3:7002/Files/MemberPhoto/1705/e5a2d5634e67420bb66e3ce92b647411_Thumb.jpg
	 */

	private ItemBean
		item;


	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getUniqueid() {
		return uniqueid;
	}

	public void setUniqueid(String uniqueid) {
		this.uniqueid = uniqueid;
	}

	public ItemBean getItem() {
		return item;
	}

	public void setItem(ItemBean item) {
		this.item = item;
	}

	public static class ItemBean {

		private String avatar;

		public String getAvatar() {
			return avatar;
		}

		public void setAvatar(String avatar) {
			this.avatar = avatar;
		}
	}
}
