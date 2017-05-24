package com.bowen.hannengclub.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.bowen.hannengclub.R;
import com.bowen.hannengclub.dialog.CommonMsgDialog;
import com.bowen.hannengclub.dialog.DialogBean;
import com.bowen.hannengclub.util.ToastUtil;

import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置页面
 */
public class SettingActivity extends BaseActivity {

	//	如果不认证就不显示
	@BindView(R.id.tv_setting_version_name)
	TextView mTvVersionName;

	@Override
	public int getContextViewId() {
		return R.layout.activity_setting;
	}

	private Handler myHandler;
	@Override
	public void initData() {
		mTvVersionName.setText(getVersion());
		mHeaderLine.setVisibility(View.VISIBLE);
		initHandler();
	}

	//初始化
	private void initHandler() {
		myHandler = new Handler();
	}

	@Override
	protected String initTitle() {
		return "设置";
	}

	@OnClick(value = {
		R.id.rl_setting_clear_cache
	})
	public void clearClick(View view){
		switch (view.getId()){
			case R.id.rl_setting_clear_cache:
				jumpToClear();
				break;
		}
	}

	//去应用详细页面
	private void jumpToClear(){
//		Intent intent = new Intent();
//		intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//		intent.setData(Uri.fromParts("package", getPackageName(), null));
//		startActivity(intent);
		DialogBean bean = new DialogBean("确定清理缓存数据？", "", "确定", "取消");
		CommonMsgDialog msgDialog = new CommonMsgDialog(mActivity, bean);
		msgDialog.setLeftClick(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mLoaddingText.setText("缓存清理中");
				mLoaddingRoot.setVisibility(View.VISIBLE);
				Random random=new Random();
				int timer = random.nextInt(1000) + 2000;
				myHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						mLoaddingRoot.setVisibility(View.GONE);
						ToastUtil.showToast(mActivity,"清理成功");
					}
				},timer);
			}
		});
		msgDialog.showDialog();
	}

	/**
	 * 获取版本号
	 * @return 当前应用的版本号
	 */
	public String getVersion() {
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			String version = info.versionName;
			return "" + version;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

}
