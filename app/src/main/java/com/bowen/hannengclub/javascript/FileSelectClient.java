/**
 * FileSelectClient.java
 * 2014-11-21
 * 深圳市五月高球信息咨询有限公司
 * 欧阳丰
 */
package com.bowen.hannengclub.javascript;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.webkit.ValueCallback;
import android.webkit.WebView;

/**
 * @author bowen-xiao
 * 
 */
public class FileSelectClient extends com.tencent.smtt.sdk.WebChromeClient  {

	private MayGolfChromeClient chromeClient;

	private ValueCallback<Uri> mUploadMessage;

	public static final int FILECHOOSER_RESULTCODE = 1;

	private Activity activity;

	public FileSelectClient() {
	}

	/**
	 * @param chromeClient
	 */
	public FileSelectClient(Activity activity,
							MayGolfChromeClient chromeClient) {
		super();
		this.activity = activity;
		this.chromeClient = chromeClient;
	}

	/**
	 * @param chromeClient
	 *            the chromeClient to set
	 */
	public void setChromeClient(MayGolfChromeClient chromeClient) {
		this.chromeClient = chromeClient;
	}

	//文件选择
	@Override
	public void openFileChooser(com.tencent.smtt.sdk.ValueCallback<Uri> valueCallback, String s, String s1) {
		super.openFileChooser(valueCallback, s, s1);
		mUploadMessage = valueCallback;
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("*/*");
		activity.startActivityForResult(
			Intent.createChooser(intent, "完成操作需要使用"),
			FILECHOOSER_RESULTCODE);
	}



	// Android > 4.1.1 调用这个方法
	public void openFileChooser(ValueCallback<Uri> uploadMsg,
								String acceptType, String capture) {
		mUploadMessage = uploadMsg;
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("*/*");
		activity.startActivityForResult(
			Intent.createChooser(intent, "完成操作需要使用"),
			FILECHOOSER_RESULTCODE);
	}

	// 3.0 + 调用这个方法
	public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
		mUploadMessage = uploadMsg;
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("*/*");
		activity.startActivityForResult(
			Intent.createChooser(intent, "完成操作需要使用"),
			FILECHOOSER_RESULTCODE);
	}

	// Android < 3.0 调用这个方法
	public void openFileChooser(ValueCallback<Uri> uploadMsg) {
		mUploadMessage = uploadMsg;
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("*/*");
		activity.startActivityForResult(
			Intent.createChooser(intent, "完成操作需要使用"),
			FILECHOOSER_RESULTCODE);

	}

	/**
	 * 返回文件选择
	 */
	public void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (requestCode == FILECHOOSER_RESULTCODE) {
			if (null == mUploadMessage)
				return;
			Uri result = intent == null || resultCode != Activity.RESULT_OK ? null
																			: intent.getData();
			mUploadMessage.onReceiveValue(result);
			mUploadMessage = null;
		}
	}
	

	/**
	 * 
	 */
	public interface MayGolfChromeClient {

		void onReceivedTitle(WebView view, String title);

		void onProgressChanged(WebView view, int newProgress);
	}

}
