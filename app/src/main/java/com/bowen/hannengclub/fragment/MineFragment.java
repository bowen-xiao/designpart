package com.bowen.hannengclub.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bowen.hannengclub.R;
import com.bowen.hannengclub.SysConfiguration;
import com.bowen.hannengclub.activity.CommonActivity;
import com.bowen.hannengclub.activity.LoginActivity;
import com.bowen.hannengclub.network.UpLoadFile;
import com.bowen.hannengclub.util.GlideImageLoader;
import com.bowen.hannengclub.util.ToastUtil;
import com.bowen.hannengclub.util.ToolLog;
import com.bowen.hannengclub.util.ToolPhone;
import com.bowen.hannengclub.view.LineItemView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by 肖稳华 on 2017/4/20.
 */

public class MineFragment extends BaseFragment {

	//	如果不认证就不显示
	@BindView(R.id.fragment_iv_header)
	CircleImageView mHeadImage;
	@BindView(R.id.iv_mine_sex_type)
	ImageView       mSexType;
	@BindView(R.id.tv_mine_id_number)
	TextView        mIdNumber;
	@BindView(R.id.li_mine_fans)
	LineItemView    mMyFans;
	@BindView(R.id.li_mine_example)
	LineItemView    mExmaple;
	@BindView(R.id.li_mine_material)
	LineItemView    mMaterial;
	@BindView(R.id.ll_mine_note_root)
	LinearLayout    mAuthorNote;

	//认证等级
	@BindView(R.id.tv_mine_lv)
	TextView mAuthorLv;
	//认证状态
	@BindView(R.id.tv_mine_author_status)
	TextView mAuthorStatus;

	//图片选择器 参考 https://github.com/jeasonlzy/ImagePicker
	private ImagePicker imagePicker;

	@Override
	protected View initView() {
		View inflate = View.inflate(mActivity, R.layout.fragment_home_mine, null);
		return inflate;
	}

	@Override
	public void loadDataOnce() {
		//更新显示的状态,数据加载完成需要重新更新状态
		upViewState();

		//用于选择图片
		imagePicker = ImagePicker.getInstance();
		//初始化图片加载器
		imagePicker.setImageLoader(new GlideImageLoader());
	}

	boolean isAuthor;

	//是否通过验证的显示状态
	private void upViewState() {
		int visible = isAuthor ? View.VISIBLE : View.GONE;
		mMyFans.setVisibility(visible);
		mExmaple.setVisibility(visible);
		mMaterial.setVisibility(visible);
		mAuthorLv.setVisibility(visible);
		mAuthorStatus.setVisibility(visible);
		mAuthorNote.setVisibility(isAuthor ? View.GONE : View.VISIBLE);
	}


	@OnClick({R.id.iv_mine_share,
			  R.id.iv_mine_msg,
			  R.id.li_seen_mine_people,
			  R.id.li_mine_fans,
			  R.id.li_mine_percent,
			  R.id.li_mine_example,
			  R.id.li_mine_material,
			  R.id.li_mine_collect,
			  R.id.li_mine_comment_manager,
			  R.id.tv_mine_exit_login,
			  R.id.ll_mine_head_info_root,
			  R.id.fragment_iv_header,
			  R.id.li_mine_change_password
	})
	public void onClick(View view) {
		String url = "";
		/**
		 * 我的积分	/account/point.aspx
		 */
		switch (view.getId()) {
			case R.id.iv_mine_share:
				//Todo 分享我的名片
				break;
			case R.id.fragment_iv_header:
				//Todo 选择上传头像
				selectPic();
				break;
			case R.id.ll_mine_head_info_root:
				//个人信息	/account/view.aspx
				url = "account/view.aspx";
				break;
			case R.id.iv_mine_msg:
				// 消息	/account/message.aspx
				url = "account/message.aspx";
				break;
			case R.id.li_seen_mine_people:
				//谁看过我
				break;
			case R.id.li_mine_fans:
				//我的粉丝

				break;
			case R.id.li_mine_percent:
				//我的积分
				url = "account/point.aspx";
				break;
			case R.id.li_mine_example:
				//我的案例
				break;
			case R.id.li_mine_material:
				//我的素材
				break;
			case R.id.li_mine_collect:
				// 我的收藏	/account/favorite_case.aspx
				url = "account/favorite_case.aspx";
				break;
			case R.id.li_mine_comment_manager:
				//评论管理
				break;
			case R.id.li_mine_change_password:
//				修改密码	/account/password.aspx
				url = "account/password.aspx";
				break;
			case R.id.tv_mine_exit_login:
				//退出登录，登录按钮
				jumpToLogin();
				break;
		}
		//如果URL为空直接返回
		if(TextUtils.isEmpty(url)){return;}
		Intent intent = new Intent(mActivity, CommonActivity.class);
		intent.putExtra(CommonFragment.COMMON_URL, SysConfiguration.BASE_URL + url);
		startActivity(intent);
	}

	//跳转到登录页面
	private void jumpToLogin() {
		Intent intent = new Intent(mActivity, LoginActivity.class);
		startActivity(intent);
	}


	/**
	 * 去选择图片,设置一些参数信息
	 */
	private void selectPic(){
		int width = (int) (ToolPhone.getScreenWidth(mActivity) / 1.5f + 0.5f);
		//需要考虑苹果手机,输出相素
		int outPutX = 1024;
		imagePicker.setMultiMode(false); //表示单选
		imagePicker.setShowCamera(true);  //显示拍照按钮
		imagePicker.setCrop(true);        //允许裁剪（单选才有效）
		imagePicker.setSaveRectangle(true); //是否按矩形区域保存
		imagePicker.setSelectLimit(1);    //选中数量限制
		imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状(矩形)
		imagePicker.setFocusWidth(width );   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
		imagePicker.setFocusHeight(width);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
		imagePicker.setOutPutX(outPutX);//保存文件的宽度。单位像素
		imagePicker.setOutPutY(outPutX);//保存文件的高度。单位像素

		Intent intent = new Intent(mActivity, ImageGridActivity.class);
		startActivityForResult(intent, 100);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** attention to this below ,must add this**/
		// mShareAPI.onActivityResult(requestCode, resultCode, data);
		Log.d("result", "onActivityResult");

		switch (resultCode) {

			case ImagePicker.RESULT_CODE_ITEMS: //表示从相册选择
				if (data != null && requestCode == 100) {
					ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
					if(images != null && images.size() != 0){
						final String path = images.get(0).path;
						ToolLog.e("main",path + " ::: path");
						File file = new File(path);
						if(file != null && file.exists()){
							String uploadUrl = SysConfiguration.BASE_URL + "user/profile/avatar";
							UpLoadFile.upFile(file, uploadUrl, new Callback() {
								@Override
								public void onFailure(Call call, IOException e) {
									ToastUtil.showToast(mActivity,"头像上传失败");
								}

								@Override
								public void onResponse(Call call, Response response) throws IOException {
									//有返回值
//									response.body();
								}
							});
							//ToolImage.displayLocalPic(mActivity,mHeadImage,path);

						}
					}
				}
				break;
		}
	}



}
