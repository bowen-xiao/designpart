package com.bowen.hannengclub.javascript;

import android.Manifest;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.bowen.hannengclub.activity.BaseActivity;
import com.bowen.hannengclub.activity.CommonActivity;
import com.bowen.hannengclub.bean.ShareBean;
import com.bowen.hannengclub.fragment.CommonFragment;
import com.bowen.hannengclub.popuwindow.SharePopupWindow;
import com.bowen.hannengclub.util.Constans;
import com.bowen.hannengclub.util.ToastUtil;
import com.bowen.hannengclub.util.ToolLog;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.tencent.smtt.sdk.WebView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by 肖稳华 on 2017/4/24.
 */

public class JavaScriptInterface {

	BaseActivity mActivity;

	com.tencent.smtt.sdk.WebView mWebView;

	public JavaScriptInterface(BaseActivity activity, WebView webView) {
		this.mActivity = activity;
		this.mWebView = webView;
	}

	//1)与JS交互的接口
	@JavascriptInterface
	public String app_getVer() {
		return String.valueOf(Constans.VERSION_ID);
	}

	//2)设置标题
	@JavascriptInterface
	public void app_setTitle(String title) {
		if (mActivity != null) {
			mActivity.setTitle(title);
		}
	}

	//3)下载资源
	@JavascriptInterface
	public void app_downloadResource(String type, String url) {
		//// TODO: 2017/4/24 下载内容
		/**
		 * type: 资源类型：0 -> 图片
		 resourceUrl: 资源url
		 */

	}

	/**
	 * type：享到：0 -> 微信聊天， 1 -> 朋友圈,  2 -> 用户选择 title：标题 desc: 描述 url: 分享的url imageUrl: 图片路径
	 */
	//4)分享内容
	@JavascriptInterface
	public void app_share(String type, String title, String desc, String url, String imageUrl) {
		UMShareListener umShareListener = new UMShareListener() {
			@Override
			public void onStart(SHARE_MEDIA share_media) {
				//开始分享
			}

			@Override
			public void onResult(SHARE_MEDIA share_media) {
			}

			@Override
			public void onError(SHARE_MEDIA share_media, Throwable throwable) {
				//分享错误
			}

			@Override
			public void onCancel(SHARE_MEDIA share_media) {
				//用户取消分享
				ToastUtil.showToast(mActivity, "用户取消了分享");
			}
		};
		UMImage image = new UMImage(mActivity, imageUrl);//网络图片
		UMWeb web = new UMWeb(url);
		web.setTitle(title);//标题
		web.setThumb(image);  //缩略图
		web.setDescription(desc);//描述
		if (type.equals("1")) {
			new ShareAction(mActivity).withMedia(web).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener).share();
		} else if (type.equals("0")) {
			new ShareAction(mActivity).withMedia(web).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener).share();
		}else if (type.equals("2")) {
			//// TODO: 2017/4/24 平台分享
			/**
			 *   UMImage image = new UMImage(ShareActivity.this, "imageurl");//网络图片
			 UMImage image = new UMImage(ShareActivity.this, file);//本地文件
			 UMImage image = new UMImage(ShareActivity.this, R.drawable.xxx);//资源文件
			 UMImage image = new UMImage(ShareActivity.this, bitmap);//bitmap文件
			 UMImage image = new UMImage(ShareActivity.this, byte[]);//字节流
			 */
			//SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE,
			//		SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
			/*ShareBoardConfig config = new ShareBoardConfig();
			config.setTitleText("分享");
			//不显示指示器
			config.setIndicatorVisibility(false);
			config.setCancelButtonText("取消分享");
			new ShareAction(mActivity).withMedia(web)
									  .setDisplayList(SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.SINA, SHARE_MEDIA.QQ,
													  SHARE_MEDIA.QZONE)
									  .setCallback(umShareListener)
									  .open(config);*/
			ShareBean shareBean = new ShareBean();
			shareBean.setBackurl(url);
			shareBean.setTitle(title);
			shareBean.setContent(desc);
			shareBean.setPic(imageUrl);
			new SharePopupWindow(mActivity, shareBean, umShareListener);
		}

	}

	//5)获取地理位置信息
	@JavascriptInterface
	public void app_getCoordinate() {
		//// TODO: 2017/4/24 位置获取

		//请求授权,权限框架
		new RxPermissions(mActivity).request(
			//					 Manifest.permission.CAMERA,
			Manifest.permission.ACCESS_FINE_LOCATION).subscribe(new Action1<Boolean>() {
			@Override
			public void call(Boolean granted) {
				if (granted) { // 在android 6.0之前会默认返回true
					// 已经获取权限
					startGetLocation();
				} else {
					// 未获取权限
					ToastUtil.showToast(mActivity, "获取授权失败");
				}
			}
		});

	}

	//6)打开新页面
	@JavascriptInterface
	public int app_openUrl(String url) {
		Intent intent = new Intent(mActivity, CommonActivity.class);
		intent.putExtra(CommonFragment.COMMON_URL, url);
		mActivity.startActivity(intent);
		//固定返回值，是APP打开的标识
		return 1;
	}

	//去获取定位，适配6.0以上系统
	private void startGetLocation() {

		if (mLocationClient == null) {
			mLocationClient = new LocationClient(mActivity.getApplicationContext());
			initLocation();
			//声明LocationClient类
			mLocationClient.registerLocationListener(new MyLocationListener());
		}
		mLocationClient.start();
	}

	public LocationClient mLocationClient;

	//处理码,不要与其它值冲突就可以了
	protected final static int LOCATION_CODE = 22;


	//参数配置信息，一般情况下不用改
	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
		//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

		option.setCoorType("bd09ll");
		//可选，默认gcj02，设置返回的定位结果坐标系

		int span = 0;
		option.setScanSpan(span);
		//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

		option.setIsNeedAddress(true);
		//可选，设置是否需要地址信息，默认不需要

		option.setOpenGps(true);
		//可选，默认false,设置是否使用gps

		option.setLocationNotify(true);
		//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

		option.setIsNeedLocationDescribe(true);
		//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

		option.setIsNeedLocationPoiList(true);
		//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

		option.setIgnoreKillProcess(false);
		//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

		option.SetIgnoreCacheException(false);
		//可选，默认false，设置是否收集CRASH信息，默认收集

		option.setEnableSimulateGps(false);
		//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

		mLocationClient.setLocOption(option);
	}

	//停止定位
	private void stopLocation() {
		if (mLocationClient != null) {
			mLocationClient.stop();
		}
	}

	class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {

			//获取定位结果
			StringBuffer sb = new StringBuffer(256);

			sb.append("time : ");
			sb.append(location.getTime());    //获取定位时间

			sb.append("\nerror code : ");
			sb.append(location.getLocType());    //获取类型类型

			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());    //获取纬度信息

			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());    //获取经度信息

			sb.append("\nradius : ");
			sb.append(location.getRadius());    //获取定位精准度

			if (location.getLocType() == BDLocation.TypeGpsLocation) {

				// GPS定位结果
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());    // 单位：公里每小时

				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());    //获取卫星数

				sb.append("\nheight : ");
				sb.append(location.getAltitude());    //获取海拔高度信息，单位米

				sb.append("\ndirection : ");
				sb.append(location.getDirection());    //获取方向信息，单位度

				sb.append("\naddr : ");
				sb.append(location.getAddrStr());    //获取地址信息

				sb.append("\ndescribe : ");
				sb.append("gps定位成功");

			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {

				// 网络定位结果
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());    //获取地址信息

				sb.append("\noperationers : ");
				sb.append(location.getOperators());    //获取运营商信息

				sb.append("\ndescribe : ");
				sb.append("网络定位成功");

			} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {

				// 离线定位结果
				sb.append("\ndescribe : ");
				sb.append("离线定位成功，离线定位结果也是有效的");

			} else if (location.getLocType() == BDLocation.TypeServerError) {

				sb.append("\ndescribe : ");
				sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");

			} else if (location.getLocType() == BDLocation.TypeNetWorkException) {

				sb.append("\ndescribe : ");
				sb.append("网络不同导致定位失败，请检查网络是否通畅");

			} else if (location.getLocType() == BDLocation.TypeCriteriaException) {

				sb.append("\ndescribe : ");
				sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");

			}

			sb.append("\nlocationdescribe : ");
			sb.append(location.getLocationDescribe());    //位置语义化信息

			List<Poi> list = location.getPoiList();    // POI数据
			if (list != null) {
				sb.append("\npoilist size = : ");
				sb.append(list.size());
				for (Poi p : list) {
					sb.append("\npoi= : ");
					sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
				}
			}

			ToolLog.i("BaiduLocationApiDem", sb.toString());

			if (mActivity != null && mWebView != null && location != null) {
				final double longitude = location.getLongitude();
				final double latitude = location.getLatitude();
				mActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						ToolLog.i(longitude + "回调JS" + latitude);
						//回调JS
						mWebView.loadUrl("javascript:app_callbackCoordinate(" + longitude + ", " + latitude + ")");
					}
				});
			}
			//			Message message = myHandler.obtainMessage();
			//			message.what = LOCATION_CODE;
			//			message.obj = sb;
			//			myHandler.sendMessage(message);
			//已经定位成功，
			stopLocation();
		}

		@Override
		public void onConnectHotSpotMessage(String s, int i) {

		}
	}

}
