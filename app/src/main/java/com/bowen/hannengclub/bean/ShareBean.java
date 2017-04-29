package com.bowen.hannengclub.bean;

/**
 * Created by 肖稳华 on 2017/4/29.
 */
public  class ShareBean {

	private String title;
	private String backurl;
	private String content;
	private String pic;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBackurl() {
		return backurl;
	}

	public void setBackurl(String backurl) {
		this.backurl = backurl;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	@Override
	public String toString() {
		return "ShareBean{" +
			   "title='" + title + '\'' +
			   ", backurl='" + backurl + '\'' +
			   ", content='" + content + '\'' +
			   ", pic='" + pic + '\'' +
			   '}';
	}
}
