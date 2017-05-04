package com.bowen.hannengclub.bean;

/**
 * Created by 肖稳华 on 2017/5/3.
 */

public class VersionResult extends BaseReqResult {

	@Override
	public String toString() {
		return "VersionResult{" +
			   "uniqueid='" + uniqueid + '\'' +
			   ", item=" + item +
			   '}';
	}

	/**
	 * uniqueid : e7bc401a-21e0-443f-b3e8-1a3328d01ab7
	 * item : {"cancel":"不更新了","down_url":"http://m.hannengclub.com/appupd/100.apk","update":"我要更新","title":"APP更新标题100","version":100,"message":"更新内容如下：\\r\\n1.AAA\\r\\n2.BBB\\r\\n3.CCC"}
	 */

	private String   uniqueid;
	/**
	 * cancel : 不更新了
	 * down_url : http://m.hannengclub.com/appupd/100.apk
	 * update : 我要更新
	 * title : APP更新标题100
	 * version : 100
	 * message : 更新内容如下：\r\n1.AAA\r\n2.BBB\r\n3.CCC
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

		private String cancel;
		private String down_url;
		private String update;
		private String title;
		private int    version;
		private String message;

		public String getCancel() {
			return cancel;
		}

		public void setCancel(String cancel) {
			this.cancel = cancel;
		}

		public String getDown_url() {
			return down_url;
		}

		public void setDown_url(String down_url) {
			this.down_url = down_url;
		}

		public String getUpdate() {
			return update;
		}

		public void setUpdate(String update) {
			this.update = update;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public int getVersion() {
			return version;
		}

		public void setVersion(int version) {
			this.version = version;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		@Override
		public String toString() {
			return "ItemBean{" +
				   "cancel='" + cancel + '\'' +
				   ", down_url='" + down_url + '\'' +
				   ", update='" + update + '\'' +
				   ", title='" + title + '\'' +
				   ", version=" + version +
				   ", message='" + message + '\'' +
				   '}';
		}
	}
}
