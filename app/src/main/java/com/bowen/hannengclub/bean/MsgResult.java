package com.bowen.hannengclub.bean;

/**
 * Created by 肖稳华 on 2017/5/26.
 */

public class MsgResult extends BaseReqResult {

	/**
	 * uniqueid : f8012169-b754-4eb0-beb1-03ebe5a04851
	 * item : {"hasread":0}
	 */

	private String   uniqueid;
	/**
	 * hasread : 0
	 */

	private ItemBean item;

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

		private int hasread;

		public int getHasread() {
			return hasread;
		}

		public void setHasread(int hasread) {
			this.hasread = hasread;
		}
	}
}
