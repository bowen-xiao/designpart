package com.bowen.hannengclub.bean;

/**
 * Created by 肖稳华 on 2017/4/29.
 */

public class UserInfo {

	@Override
	public String toString() {
		return "UserInfo{" +
			   "id_str='" + id_str + '\'' +
			   ", tipsmsg='" + tipsmsg + '\'' +
			   ", age_str='" + age_str + '\'' +
			   ", sign_status=" + sign_status +
			   ", avatar='" + avatar + '\'' +
			   ", gender=" + gender +
			   ", phone_number='" + phone_number + '\'' +
			   ", author_str='" + author_str + '\'' +
			   ", login_token='" + login_token + '\'' +
			   ", grade_str='" + grade_str + '\'' +
			   ", id='" + id + '\'' +
			   ", avatarstate=" + avatarstate +
			   ", share=" + share +
			   ", name='" + name + '\'' +
			   '}';
	}

	private String    id_str;
	private String    tipsmsg;
	private String    age_str;
	private int       sign_status;
	private String    avatar;
	private int       gender;
	private String    phone_number;
	private String    author_str;
	private String    login_token;
	private String    grade_str;
	private String    id;
	private int       avatarstate;
		/**
		 * title : 郑冬雪
		 * backurl : http://222.171.202.3:7002/Zone/PersonalCase.aspx?ID=100
		 * content : “家·从心出发”是我的人生信条，“年轻”是我的资本，空间利用是我的优势，色彩搭配是我的专业；我是家装设计师XXX。
		 * pic : http://222.171.202.3:7002/Files/MemberPhoto/1704/c6e56cc60c6742e5986b1639bc2cdc5b_Thumb.jpg
		 */

	private ShareBean share;
		private String name;

		public String getId_str() {
			return id_str;
		}

		public void setId_str(String id_str) {
			this.id_str = id_str;
		}

		public String getTipsmsg() {
			return tipsmsg;
		}

		public void setTipsmsg(String tipsmsg) {
			this.tipsmsg = tipsmsg;
		}

		public String getAge_str() {
			return age_str;
		}

		public void setAge_str(String age_str) {
			this.age_str = age_str;
		}

		public int getSign_status() {
			return sign_status;
		}

		public void setSign_status(int sign_status) {
			this.sign_status = sign_status;
		}

		public String getAvatar() {
			return avatar;
		}

		public void setAvatar(String avatar) {
			this.avatar = avatar;
		}

		public int getGender() {
			return gender;
		}

		public void setGender(int gender) {
			this.gender = gender;
		}

		public String getPhone_number() {
			return phone_number;
		}

		public void setPhone_number(String phone_number) {
			this.phone_number = phone_number;
		}

		public String getAuthor_str() {
			return author_str;
		}

		public void setAuthor_str(String author_str) {
			this.author_str = author_str;
		}

		public String getLogin_token() {
			return login_token;
		}

		public void setLogin_token(String login_token) {
			this.login_token = login_token;
		}

		public String getGrade_str() {
			return grade_str;
		}

		public void setGrade_str(String grade_str) {
			this.grade_str = grade_str;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public int getAvatarstate() {
			return avatarstate;
		}

		public void setAvatarstate(int avatarstate) {
			this.avatarstate = avatarstate;
		}

		public ShareBean getShare() {
			return share;
		}

		public void setShare(ShareBean share) {
			this.share = share;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}
