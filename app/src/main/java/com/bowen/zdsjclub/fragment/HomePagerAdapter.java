package com.bowen.zdsjclub.fragment;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by 肖稳华 on 2016/11/10.
 */

public class HomePagerAdapter extends FragmentPagerAdapter {

    Context mContext;


    public HomePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }


    @Override
    public BaseFragment getItem(int position) {
        return FragmentFactory.getFragment(position);
    }

    @Override
    public int getCount() {
        return 5;
    }

}
