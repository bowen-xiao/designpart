package com.bowen.zdsjclub.fragment;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bowen.zdsjclub.R;
import com.bowen.zdsjclub.javascript.JavaScriptInterface;
import com.bowen.zdsjclub.util.ToastUtil;
import com.bowen.zdsjclub.util.ToolImage;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import butterknife.BindView;

import static com.bowen.zdsjclub.R.id.webView;

/**
 * Created by 肖稳华 on 2017/4/20.
 * Web X5 //http://x5.tencent.com/tbs/guide.html
 */

public class HomeFragment extends BaseFragment {

	@BindView(R.id.ll_loading_view_root)
	LinearLayout mLoadRoot;
	@BindView(com.bowen.zdsjclub.R.id.iv_loading_view)
	ImageView    mIvLoad;

	@BindView(webView)
	com.tencent.smtt.sdk.WebView mWebView;
	@BindView(com.bowen.zdsjclub.R.id.tvStatus)
	TextView                     mTvStatus;
	@BindView(com.bowen.zdsjclub.R.id.progressBar)
	ProgressBar                  mProgressBar;

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
	}

	@Override
	public void loadDataOnce() {

		//可以开始加载数据
		WebSettings webSettings = mWebView.getSettings();
		//可以使用script
		webSettings.setJavaScriptEnabled(true);
		int width = mWebView.getView().getWidth();
		mWebView.addJavascriptInterface(new JavaScriptInterface(mActivity), "android");
		int tbsVersion = QbSdk.getTbsVersion(mActivity);
		String TID = QbSdk.getTID();
		String qBVersion = QbSdk.getMiniQBVersion(mActivity);
		mTvStatus.setText("TbsVersion:" + tbsVersion + "\nTID:" + TID + "\nMiniQBVersion:" + qBVersion);
		//去加载网页数据
//		loadUrl(SysConfiguration.BASE_URL + "index.aspx?apptype=1");
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
			public void onReceivedError(WebView var1, int var2, String var3, String var4) {

				ToastUtil.showToast(mActivity,"网页加载失败");
				Message msg=handler.obtainMessage();//发送通知，加入线程
				 msg.what=URL_ERR;//通知加载自定义404页面
				 handler.sendMessage(msg);//通知发送！
			}
		});
		//进度条
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					mProgressBar.setVisibility(View.GONE);
					mLoadRoot.setVisibility(View.GONE);
					//tvEnter.setText("刷新");
					return;
				}
				mProgressBar.setVisibility(View.VISIBLE);
				mProgressBar.setProgress(newProgress);
			}
		});
	}

	protected final int URL_ERR = 404 ;

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case URL_ERR:
					mProgressBar.setVisibility(View.GONE);
					mLoadRoot.setVisibility(View.GONE);
					break;
			}
		}
	};

	//显示错误信息的页面
	private void showErrPage(){

	}
}
