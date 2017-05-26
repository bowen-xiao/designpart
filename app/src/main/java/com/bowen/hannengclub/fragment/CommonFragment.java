package com.bowen.hannengclub.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bowen.hannengclub.R;
import com.bowen.hannengclub.activity.HomeActivity;
import com.bowen.hannengclub.javascript.JavaScriptInterface;
import com.bowen.hannengclub.util.Constans;
import com.bowen.hannengclub.util.ToolImage;
import com.bowen.hannengclub.util.ToolLog;
import com.bowen.hannengclub.util.UserUtil;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by 肖稳华 on 2017/4/20.
 * Web X5 //http://x5.tencent.com/tbs/guide.html
 */
public class CommonFragment extends BaseFragment {

	@BindView(R.id.ll_loading_view_root)
	LinearLayout mLoadRoot;
	@BindView(R.id.ll_err_page)
	LinearLayout mErrPage;
	@BindView(R.id.iv_loading_view)
	ImageView    mIvLoad;

	@BindView(R.id.webView)
	com.tencent.smtt.sdk.WebView mWebView;

	@BindView(R.id.progressBar)
	ProgressBar                  mProgressBar;

	@BindView(R.id.ll_err_back)
	View                  mErrBack;

	//一级页面的子标题
	@BindView(R.id.common_second_title)
	TextView mSecondTitle;

	//一级页面的子标题下面的线
	@BindView(R.id.common_second_title_underline)
	View mSecondTitleUnderLine;

	private ValueCallback<Uri[]> mUploadMessage;
	public static final int FILECHOOSER_RESULTCODE = 1023;

	//参数名称
	public static final String COMMON_SECOND_TITLE = "common_second_title";

	@Override
	protected View initView() {
		View inflate = View.inflate(mActivity, com.bowen.hannengclub.R.layout.fragment_home_first, null);
		return inflate;
	}

	//登录状态改变
	public final static String LOGIN_STATUS_CHANGE = "LOGIN_STATUS_CHANGE";
	/**
	 * 注册广播,刷新头像等用户信息
	 */
	public void registerBroadcast() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(LOGIN_STATUS_CHANGE);
		filter.addAction(HomeActivity.LOGIN_OUT);
		mActivity.registerReceiver(receiver, filter);
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			if (LOGIN_STATUS_CHANGE.equals(intent.getAction())
				|| HomeActivity.LOGIN_OUT.equals(intent.getAction())) {
				if(mWebView != null){
					//去设置token
					String token = UserUtil.getToken(mActivity);
					String url = "javascript:h5_changeToken(\"%s\")";
					url = String.format(url,token);
					ToolLog.d("token","login info :: " + token);
					ToolLog.d("token","login url :: " + url);
					//							 h5_changeToken
					mWebView.loadUrl(url);
				}
			}
		}};

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroy() {
		if(receiver != null){
			mActivity.unregisterReceiver(receiver);
		}
		super.onDestroy();
	}

	public final static String COMMON_URL = "common_url";
	String url ;
	@Override
	public void initData() {
		//可去取消注册
		registerBroadcast();
		ToolImage.loading(mActivity, mIvLoad);
		url = getArguments().getString(COMMON_URL);
		//添加公共参数
		addCommonParam();

		int index = getArguments().getInt("index", -1);
		mErrBack.setVisibility(index == -1 ? View.GONE : View.GONE);
		ToolLog.e("main",url + "url");
		if(index == 0){
			loadDataOnce();
		}
		String secondTitle = getArguments().getString(COMMON_SECOND_TITLE);
		if(!TextUtils.isEmpty(secondTitle)){
			mSecondTitle.setText(secondTitle);
		}
		//是否显示二级子标题，用于一级页面
		mSecondTitle.setVisibility(TextUtils.isEmpty(secondTitle) ? View.GONE : View.VISIBLE);
		mSecondTitleUnderLine.setVisibility(TextUtils.isEmpty(secondTitle) ? View.GONE : View.VISIBLE);
	}

	//添加公共参数
	private void addCommonParam(){
		if(!url.contains("http")){
			//			url = SysConfiguration.BASE_URL + url;
			//这是测试地址 todo 正式需要修改
			url = "http://222.171.202.3:7002/" + url;
		}
		/**
		 * 	说明
		 token	用户token
		 appver	app版本号
		 */
		String commonParam = "token=%s&appver=%s";
		String token = UserUtil.getToken(mActivity);
		commonParam = String.format(commonParam, token, Constans.VERSION_ID);
		if(url.contains("?")){
			commonParam = "&"+commonParam;
		}else{
			commonParam = "?" + commonParam;
		}
		url = url + commonParam;
	}

	boolean isLoad;
	@Override
	public void loadDataOnce() {
		if(isLoad){return;}
		isLoad = true;
		mErrPage.setVisibility(View.GONE);
		//加载中的显示
		mLoadRoot.setVisibility(View.VISIBLE);
		//可以开始加载数据
		WebSettings webSettings = mWebView.getSettings();
		//可以使用script
		//设置可以访问文件
		mWebView.getSettings().setAllowFileAccess(true);
		String userAgent = webSettings.getUserAgentString()+ " android：hnclubandroid("+Constans.VERSION_ID+")";
		webSettings.setUserAgent(userAgent);
		mWebView.getSettings().setJavaScriptEnabled(true);//可以使用JS
		mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);//可以使用插件
		mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		int width = mWebView.getView().getWidth();
		mWebView.addJavascriptInterface(new JavaScriptInterface(mActivity,mWebView), "android");

		int tbsVersion = QbSdk.getTbsVersion(mActivity);
		String TID = QbSdk.getTID();
		String qBVersion = QbSdk.getMiniQBVersion(mActivity);
//		mTvStatus.setText("TbsVersion:" + tbsVersion + "\nTID:" + TID + "\nMiniQBVersion:" + qBVersion);
		//去加载网页数据
//		loadUrl(SysConfiguration.BASE_URL + "index.aspx?apptype=1");
		//禁用缓存
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		loadUrl(url);
	}


	private void loadUrl(String url) {
		mWebView.loadUrl(url);
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.startsWith("mailto:") || url.startsWith("geo:")
					|| url.startsWith("tel:")||url.startsWith("wtai:")
					) {
					try {
						Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
						view.getContext().startActivity(intent);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return true;
				}
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onReceivedError (WebView view, int errorCode, String description, String failingUrl) {
				sendErrMsg();
//				ToolLog.e("url" , "onReceivedError code : " + errorCode);
			}

			@Override
			public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse)
			{
				int statusCode = errorResponse.getStatusCode();
//				Message msg=handler.obtainMessage();//发送通知，加入线程
//				msg.what=URL_ERR;//通知加载自定义404页面
//				handler.sendMessage(msg);//通知发送！
				//如果错误码大于这个值就是要显示错误页面
				if(statusCode > 399){
					sendErrMsg();
				}
//				ToolLog.e("url" , "onReceivedHttpError code : " + statusCode);
			}
		});
		//进度条
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				mProgressBar.setVisibility(View.VISIBLE);
				//mLoadRoot.setVisibility(View.VISIBLE);
				mProgressBar.setProgress(newProgress);
				ToolLog.e("main","main :: newProgress :: " + newProgress);
				if (newProgress == 100) {
					mWebView.setVisibility(View.VISIBLE);
					mProgressBar.setVisibility(View.GONE);
					mLoadRoot.setVisibility(View.GONE);
					//tvEnter.setText("刷新");
					return;
				}
			}

			//文件选择
			@Override
			public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
				mUploadMessage = valueCallback;
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("*/*");
				startActivityForResult(
						Intent.createChooser(intent, "完成操作需要使用"),
						FILECHOOSER_RESULTCODE);
				return true;
				//return super.onShowFileChooser(webView, valueCallback, fileChooserParams);
			}
		}

		);
	}

	//发送错误的消息
	private void sendErrMsg(){
		Message msg=handler.obtainMessage();//发送通知，加入线程
		msg.what=URL_ERR;//通知加载自定义404页面
		handler.sendMessage(msg);//通知发送！
	}

	@OnClick(value={
		 R.id.ll_err_refresh
		,R.id.ll_err_back
	})
	public void handClick(View view){
		switch (view.getId()){
			case  R.id.ll_err_refresh:
				mErrPage.setVisibility(View.GONE);
				mWebView.setVisibility(View.GONE);
				mLoadRoot.setVisibility(View.VISIBLE);
				mWebView.reload();
				break;
			case  R.id.ll_err_back:
				//关闭页面
				mActivity.finish();
				break;
		}
	}

	// err代码
	protected final int URL_ERR = 404 ;

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case URL_ERR:
					showErrPage();
					break;
			}
		}
	};

	//显示错误信息的页面
	private void showErrPage(){
		mProgressBar.setVisibility(View.GONE);
		mLoadRoot.setVisibility(View.GONE);
		mWebView.setVisibility(View.GONE);
		mErrPage.setVisibility(View.VISIBLE);
		//ToastUtil.showToast(mActivity,"加载错误");
	}


	public boolean canBack(){
		boolean result =  false;
		if(mWebView != null){
			if(mWebView.canGoBack()){
				mWebView.goBack();
				result = true;
			}
		}
		return result;
	}



	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == FILECHOOSER_RESULTCODE) {
			if (null == mUploadMessage)
				return;
			Uri result = data == null || resultCode != Activity.RESULT_OK ? null
					: data.getData();
			mUploadMessage.onReceiveValue(new Uri[]{result});
			mUploadMessage = null;
		}
	}
}
