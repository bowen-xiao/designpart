package com.bowen.hannengclub.adapter;

import android.os.Bundle;
import android.support.v4.util.SparseArrayCompat;

import com.bowen.hannengclub.fragment.BaseFragment;
import com.bowen.hannengclub.fragment.CommonFragment;

/**
 * @author bowen
 */
public class NewHomeFragmentFactory
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
			/**
			 * 	url
			 首页
			 热点	/index.aspx?apptype=1
			 资讯	/news/index.aspx?apptype=1
			 视频	/visit/index.aspx?apptype=1
			 活动	/active/index.aspx?apptype=1
			 专题	/subject/index.aspx?apptype=1
			 案例	/case/index.aspx?apptype=2
			 关注
			 */
			case 0:
//				"http://lytest.fengjing.com/wap/guideInfo/g_comment_list.html?id=383"
				//  热点
				url = ("index.aspx?apptype=1");
//				url = ("http://lytest.fengjing.com/wap/guideInfo/g_comment_list.html?id=383");
				fragment = new CommonFragment();
				break;
			case 1:
				// 资讯
				url =  "news/index.aspx?apptype=1";
				fragment = new CommonFragment();
				break;
			case 2:
				// 视频
				url = "visit/index.aspx?apptype=1";
				fragment = new CommonFragment();
				break;
			case 3:
				// 活动
				url =  "active/index.aspx?apptype=1";
				fragment = new CommonFragment();
				break;
			case 4:
				//  专题
				url =  "subject/index.aspx?apptype=1";
				fragment = new CommonFragment();
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
