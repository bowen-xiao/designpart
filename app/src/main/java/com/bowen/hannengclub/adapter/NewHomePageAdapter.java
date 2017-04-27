package com.bowen.hannengclub.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bowen.hannengclub.R;
import com.bowen.hannengclub.fragment.BaseFragment;


/**
 * Created by 肖稳华 on 2016/11/10.
 */

public class NewHomePageAdapter extends FragmentPagerAdapter {

    Context mContext;

    String[] mTitles;

    public NewHomePageAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
        mTitles = mContext.getResources().getStringArray(R.array.home_page_titles);
    }


    @Override
    public BaseFragment getItem(int position) {
        return NewHomeFragmentFactory.getFragment(position);
    }

    @Override
    public int getCount() {
        if(mTitles != null && mTitles.length != 0){
            return mTitles.length;
        }
        return 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(mTitles != null && mTitles.length != 0){
            return mTitles[position];
        }
        return super.getPageTitle(position);
    }
}
