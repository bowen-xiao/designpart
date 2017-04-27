package com.bowen.hannengclub.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bowen.hannengclub.R;

/**
 * Created by 肖稳华 on 2017/4/20.
 */

public class LineItemView extends LinearLayout {

	public LineItemView(Context context) {
		this(context,null);
	}

	public LineItemView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public LineItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(attrs);
	}


	//初始化操作
	private void init(AttributeSet attrs) {
		View view = View.inflate(getContext(), R.layout.view_line_mune, this);
		TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.LineItemView);
		String title = array.getString(R.styleable.LineItemView_my_title);
		int logo = array.getResourceId(R.styleable.LineItemView_my_logo,-1);

		array.recycle();
		ImageView ivLogo = (ImageView) view.findViewById(R.id.iv_line_mune);
		TextView tvTitle = (TextView) view.findViewById(R.id.tv_line_title);
		//没有输入就不显示
		if(logo != -1){
			ivLogo.setImageResource(logo);
			ivLogo.setVisibility(View.VISIBLE);
		}else{
			ivLogo.setVisibility(View.GONE);
		}
		tvTitle.setText(title);
		view.setClickable(true);
	}

}
