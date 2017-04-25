package com.bowen.zdsjclub.dialog;

/**
 * Created by 肖稳华 on 2017/4/25.
 */

public class DialogBean {

	private String msg;
	private String title;
	private String leftBtn;
	private String rightBtn;

	public DialogBean(String msg, String title, String leftBtn, String rightBtn) {
		this.msg = msg;
		this.title = title;
		this.leftBtn = leftBtn;
		this.rightBtn = rightBtn;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLeftBtn() {
		return leftBtn;
	}

	public void setLeftBtn(String leftBtn) {
		this.leftBtn = leftBtn;
	}

	public String getRightBtn() {
		return rightBtn;
	}

	public void setRightBtn(String rightBtn) {
		this.rightBtn = rightBtn;
	}
}
