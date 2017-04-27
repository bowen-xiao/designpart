package com.bowen.hannengclub.javascript;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.bowen.hannengclub.activity.CommonActivity;
import com.bowen.hannengclub.fragment.CommonFragment;
import com.bowen.hannengclub.util.Constans;

/**
 * Created by 肖稳华 on 2017/4/24.
 */

public class JavaScriptInterface {

	Activity mActivity;

	public JavaScriptInterface(Activity activity) {
		this.mActivity = activity;
	}

	//1)与JS交互的接口
	@JavascriptInterface
	public String app_getVer(){
		return String.valueOf(Constans.VERSION_ID);
	}
	
	//2)设置标题
	@JavascriptInterface
	public void app_setTitle(String title){
		//// TODO: 2017/4/24 设置标题
	}

	//3)下载资源
	@JavascriptInterface
	public void app_downloadResource(String type,String url){
		//// TODO: 2017/4/24 下载内容
		/**
		 * type: 资源类型：0 -> 图片
		 	resourceUrl: 资源url
		 */

	}

	/**
	 * type：享到：0 -> 微信聊天， 1 -> 朋友圈,  2 -> 用户选择
		 title：标题
		 desc: 描述
		 url: 分享的url
		 imageUrl: 图片路径
	 * @param type
	 * @param title
	 * @param desc
	 * @param url
	 * @param imageUrl
	 */
	//4)分享内容
	@JavascriptInterface
	public void app_share(String type,String title,String desc,String url,String imageUrl){
		//// TODO: 2017/4/24 平台分享

	}

	//5)获取地理位置信息
	@JavascriptInterface
	public void app_getCoordinate(){
		//// TODO: 2017/4/24 位置获取
		/**
		 * 需要回调JS
		 * function app_callbackCoordinate(longitude, latitude){
		 }
		 */
	}

	//6)打开新页面
	@JavascriptInterface
	public int app_openUrl(String url){
		////
		Intent intent = new Intent(mActivity, CommonActivity.class);
		intent.putExtra(CommonFragment.COMMON_URL, url);
		mActivity.startActivity(intent);
		return  1;
	}


}
