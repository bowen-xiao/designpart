package com.bowen.hannengclub.popuwindow;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bowen.hannengclub.R;
import com.bowen.hannengclub.bean.ShareBean;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

/**
 * Created by 肖稳华 on 2017/5/8.
 */

public class SharePopupWindow extends PopupWindow implements View.OnClickListener {

	private  WindowManager.LayoutParams params;
	private       Activity              mActivity;

	private View view;

	private TextView btn_cancel;

	private UMShareListener mCallBack;
	private UMWeb         mShareWeb;

	public SharePopupWindow(final Activity mContext, ShareBean shareBean, UMShareListener itemsOnClick) {
		mCallBack = itemsOnClick;
		this.mActivity = mContext;
		//虚拟按键挡住
		this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		this.view = LayoutInflater.from(mContext).inflate(R.layout.share_popupwindow, null);

		//        btn_take_photo = (Button) view.findViewById(R.id.btn_take_photo);
		//        btn_pick_photo = (Button) view.findViewById(R.id.btn_pick_photo);
		btn_cancel = (TextView) view.findViewById(R.id.btn_cancel_share);
		// 取消按钮
		btn_cancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// 销毁弹出框
				dismiss();
			}
		});
		// 设置按钮监听
		view.findViewById(R.id.pop_live_share_friends).setOnClickListener(this);
		view.findViewById(R.id.pop_live_share_weixin).setOnClickListener(this);
		view.findViewById(R.id.pop_live_share_sina).setOnClickListener(this);
		view.findViewById(R.id.pop_live_share_qq).setOnClickListener(this);
		view.findViewById(R.id.pop_live_share_qq_zone).setOnClickListener(this);
//		btn_take_photo.setOnClickListener(itemsOnClick);

		// 设置外部可点击
		this.setOutsideTouchable(true);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		this.view.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int height = view.findViewById(R.id.pop_layout).getTop();

				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});

		UMImage image = new UMImage(mContext, shareBean.getPic());//网络图片
		mShareWeb = new UMWeb(shareBean.getBackurl());
		mShareWeb.setTitle(shareBean.getTitle());//标题
		mShareWeb.setThumb(image);  //缩略图
		mShareWeb.setDescription(shareBean.getContent());//描述


    /* 设置弹出窗口特征 */
		// 设置视图
		this.setContentView(this.view);
		// 设置弹出窗体的宽和高
		this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
		this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

		// 设置弹出窗体可点击
		this.setFocusable(true);

		// 实例化一个ColorDrawable颜色为半透明
//		ColorDrawable dw = new ColorDrawable(0xb0000000);
//		// 设置弹出窗体的背景
//		this.setBackgroundDrawable(dw);

		// 设置弹出窗体显示时的动画，从底部向上弹出
		this.setAnimationStyle(R.style.share_popuwindow_anim);

		this.showAtLocation(mContext.findViewById(R.id.ff_common_content), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		params = mContext.getWindow().getAttributes();
		//当弹出Popupwindow时，背景变半透明
		params.alpha=0.7f;
		mContext.getWindow().setAttributes(params);
		//设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
		this.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				params = mContext.getWindow().getAttributes();
				params.alpha=1f;
				mContext.getWindow().setAttributes(params);
			}
		});
	}

	@Override
	public void onClick(View v) {

		SHARE_MEDIA sharePlatform = SHARE_MEDIA.WEIXIN_CIRCLE;
		switch (v.getId()){
			case R.id.pop_live_share_friends:
				break;
			case R.id.pop_live_share_weixin:
				sharePlatform = SHARE_MEDIA.WEIXIN;
				break;
			case R.id.pop_live_share_sina:
				sharePlatform = SHARE_MEDIA.SINA;
				break;
			case R.id.pop_live_share_qq:
				sharePlatform = SHARE_MEDIA.QQ;
				break;
			case R.id.pop_live_share_qq_zone:
				sharePlatform = SHARE_MEDIA.QZONE;
				break;
		}
		new ShareAction(mActivity).setPlatform(sharePlatform)
								  .withMedia(mShareWeb)
								  .setCallback(mCallBack)
								  .share();
		dismiss();
	}
}
