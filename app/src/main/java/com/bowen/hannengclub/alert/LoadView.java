package com.bowen.hannengclub.alert;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.bowen.hannengclub.R;
import com.bowen.hannengclub.util.ToolImage;


/**
 * 公共的popupView
 * 
 * @author bowen-xiao
 * @Date 2016-11-23
 * 
 */
@SuppressLint("NewApi")
public class LoadView extends PopupWindow
{

	/**
	 * <Button android:layout_width="fill_parent"
	 * android:layout_height="wrap_content" android:layout_margin="5dp"
	 * android:background="@drawable/gray_btn" android:text="@string/formalbums"
	 * android:textColor="@color/blue2" android:textSize="21sp"
	 * android:visibility="visible" />
	 */

	OnClickListener closeListener = new OnClickListener() {
		@Override
		public void onClick(View v)
		{
//			关闭弹出窗口,数据有效性检查
			//暂时不处理
		}
	};

	public LoadView(Activity context) {


		LinearLayout myView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.new_popup_common_view, null);
		mPopupWindow = new PopupWindow(myView, ViewGroup.LayoutParams.MATCH_PARENT,
									   ViewGroup.LayoutParams.MATCH_PARENT);


//		点击窗口就去关闭
		myView.setOnClickListener(closeListener);

		//解决5.0以上系统底部无法显示的问题,动态计算虚拟按键的高度
		Rect rect = new Rect();
		context.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
		int winHeight =context. getWindow().getDecorView().getHeight();

//		Log.e("main",rect.bottom + ":bottom");
//		Log.e("main",winHeight + ":winHeight");

		mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


		mPopupWindow.setAnimationStyle(R.style.popuStyle); // 设置 popupWindow 动画样式
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.showAtLocation(myView, Gravity.BOTTOM, 0, 0);

		mPopupWindow.setFocusable(false);// 点击窗体外就自动消失
		mPopupWindow.setTouchable(true); // 设置可以点击

		ImageView ivLoading = (ImageView) myView.findViewById(R.id.iv_loading_view);
		LinearLayout loadingRoot = (LinearLayout) myView.findViewById(R.id.ll_loading_view);
		LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) loadingRoot.getLayoutParams();
		layoutParams.width = (int) (rect.width() * 0.5f + 0.5f);
		layoutParams.height = (int) ( layoutParams.width  * 0.2667f + 0.5f);
		loadingRoot.setLayoutParams(layoutParams);
		ToolImage.loading(context,ivLoading);
	}

	private PopupWindow mPopupWindow;

	@Override
	public boolean isShowing()
	{
		if(mPopupWindow == null){ return super.isShowing();}
		return mPopupWindow.isShowing();
	}

	@Override
	public void dismiss()
	{
		if(mPopupWindow == null){ super.dismiss(); return;}
		mPopupWindow.dismiss();
	}
	

	
	public boolean close(){
		if( mPopupWindow != null && mPopupWindow.isShowing()){
			mPopupWindow.dismiss();
			mPopupWindow = null;
			return true;
		}
		return false;
	}
	
}
