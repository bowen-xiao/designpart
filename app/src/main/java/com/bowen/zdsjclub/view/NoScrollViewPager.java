package com.bowen.zdsjclub.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @类名: NoScrollViewPager
 * @作者: 肖稳华
 * 
 * @描述: 不可以滚动的viewPager,且不加载左右的页面
 * 
 * @当前版本号: $Rev: 21 $
 * @更新人: $Author: xq $
 * @更新的时间: $Date: 2015-06-01 09:52:04 +0800 (周一, 01 六月 2015) $
 * @更新的描述:
 * 
 */
public class NoScrollViewPager extends LazyViewPager
{

	public NoScrollViewPager(Context context) {
		super(context);
	}

	public NoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		// 不拦截
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		// 不消费
		return false;
	}
}

