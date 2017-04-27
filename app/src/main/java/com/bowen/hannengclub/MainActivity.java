package com.bowen.hannengclub;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.bowen.hannengclub.alert.LoadView;
import com.bowen.hannengclub.network.DataEngine2;
import com.bowen.hannengclub.network.RxNetWorkService;
import com.bowen.hannengclub.util.ToolLog;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

	public LocationClient     mLocationClient = null;
	@BindView(R.id.tv_location_info)
	TextView mLocationInfo;
	@BindView(R.id.btn_main_crash)
	Button   mBtnClick;
	protected Handler myHandler;

	private LoadView mLoadView;

	//处理码,不要与其它值冲突就可以了
	protected final static int LOCATION_CODE = 22;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		initMyHandler();
		if (mLocationClient == null) {
			mLocationClient = new LocationClient(getApplicationContext());
			initLocation();
			//声明LocationClient类
			mLocationClient.registerLocationListener(new MyLocationListener());
		}
		mLocationClient.start();

		initData();

	}

	//去网络获取数据
	private void initData() {

		RxNetWorkService service = DataEngine2.getServiceApiByClass(RxNetWorkService.class);
//		service.getBaiDuInfo(SysConfiguration.BASE_URL)
		HashMap<String, Object> map = new HashMap<>();
		map.put("just","just");
		service.getBaiDuInfo(map)
				 .subscribeOn(Schedulers.io())
				 .observeOn(AndroidSchedulers.mainThread())
				 .subscribe(new Subscriber<String>() {
					 @Override
					 public void onCompleted() {
						 ToolLog.e("main","加载完成 !");
					 }

					 @Override
					 public void onError(Throwable e) {
						 ToolLog.e("main",e.getMessage() + "加载完成 !");
					 }

					 @Override
					 public void onNext(String model) {
						 if(TextUtils.isEmpty(model)){return;}
						 mLocationInfo.setText(model);
					 }
				 });
	}

	@OnClick({
		R.id.btn_main_crash
	})
	public void onClick(View view){
		switch (view.getId()){
			case R.id.btn_main_crash:

				mLoadView = new LoadView(this);

				break;
		}
	}

	//初始化操作
	private void initMyHandler() {
		myHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case LOCATION_CODE:
						StringBuffer sb = (StringBuffer) msg.obj;
						mLocationInfo.setText(sb.toString());
						//定位成功后，停止定位
						stopLocation();
						break;
				}
			}
		};
	}

	@Override
	public void onBackPressed() {
		if(mLoadView!=null && mLoadView.isShowing()){
			mLoadView.dismiss();
			return;
		}
		super.onBackPressed();
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

			Log.i("BaiduLocationApiDem", sb.toString());

			Message message = myHandler.obtainMessage();
			message.what = LOCATION_CODE;
			message.obj = sb;
			myHandler.sendMessage(message);


		}

		@Override
		public void onConnectHotSpotMessage(String s, int i) {

		}
	}

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
	private void stopLocation(){
		if(mLocationClient != null){
			mLocationClient.stop();
		}
	}

	@Override
	protected void onDestroy() {
		stopLocation();
		super.onDestroy();
	}
}
