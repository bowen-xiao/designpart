package com.bowen.hannengclub.fragment;

import android.os.Bundle;
import android.support.v4.util.SparseArrayCompat;

/**
 * @author bowen
 */
public class FragmentFactory
{

	 // 散列的
	private static SparseArrayCompat<BaseFragment> mCaches = new SparseArrayCompat<BaseFragment>();

	public static BaseFragment getFragment(int position)
	{
		// 获得缓存
		BaseFragment fragment = mCaches.get(position);
		if (fragment != null)
		{
			return fragment;
		}

		//传递的参数信息
		Bundle args = new Bundle();
		String url = "";
		switch (position)
		{
			case 0:
				//"http://lytest.fengjing.com/wap/guideInfo/g_comment_list.html?id=383"
				// 首页
				url = ("http://lytest.fengjing.com/wap/guideInfo/g_comment_list.html?id=383");
				fragment = new HomeFragment();
				break;
			case 1:
				// 案例
				url =  "case/index.aspx?apptype=2";
				fragment = new CommonFragment();
				break;
			case 2:
				// 关注
				fragment = new FocusFragment();
				break;
			case 3:
				// 设计师
				url = "/designer/index.aspx?apptype=2";
				fragment = new CommonFragment();
				break;
			case 4:
				// 我的
				fragment = new MineFragment();
				break;
		}
		args.putString(CommonFragment.COMMON_URL, url);
		fragment.setArguments(args);
		args.putInt("index",position);
		// 存储到缓存
		mCaches.put(position, fragment);
		return fragment;
	}
	
//	将数据进行清理,退出的时候
	public static void clearCaches(){
		mCaches.clear();
	}
}
