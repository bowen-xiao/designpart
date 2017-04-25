package com.bowen.zdsjclub.fragment;

import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bowen.zdsjclub.R;
import com.bowen.zdsjclub.javascript.JavaScriptInterface;
import com.bowen.zdsjclub.util.ToastUtil;
import com.bowen.zdsjclub.util.ToolImage;
import com.bowen.zdsjclub.util.ToolLog;
import com.tencent.smtt.sdk.QbSdk;
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
public class HomeFragment extends BaseFragment {

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

	@Override
	protected View initView() {

		View inflate = View.inflate(mActivity, com.bowen.zdsjclub.R.layout.fragment_home_first, null);
		return inflate;
	}

	public final static String COMMON_URL = "common_url";
	String url ;
	@Override
	public void initData() {
		ToolImage.loading(mActivity, mIvLoad);
		url = getArguments().getString(COMMON_URL);
		int index = getArguments().getInt("index", -1);
		mErrBack.setVisibility(index == -1 ? View.GONE : View.GONE);
		ToolLog.e("main",url + "url");
	}

	boolean isLoad;
	@Override
	public void loadDataOnce() {
		if(isLoad){return;}
		isLoad = true;
		mErrPage.setVisibility(View.GONE);
		//可以开始加载数据
		WebSettings webSettings = mWebView.getSettings();
		//可以使用script
		webSettings.setJavaScriptEnabled(true);
		int width = mWebView.getView().getWidth();
		mWebView.addJavascriptInterface(new JavaScriptInterface(mActivity), "android");
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
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onReceivedError (WebView view, int errorCode, String description, String failingUrl) {
				Message msg=handler.obtainMessage();//发送通知，加入线程
				 msg.what=URL_ERR;//通知加载自定义404页面
				 handler.sendMessage(msg);//通知发送！
			}
		});
		//进度条
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				mWebView.setVisibility(View.VISIBLE);
				mProgressBar.setVisibility(View.VISIBLE);
				mProgressBar.setProgress(newProgress);
				if (newProgress == 100) {
					mProgressBar.setVisibility(View.GONE);
					mLoadRoot.setVisibility(View.GONE);
					//tvEnter.setText("刷新");
					return;
				}
			}
		});
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
		mErrPage.setVisibility(View.VISIBLE);
		ToastUtil.showToast(mActivity,"加载错误");
	}
}
