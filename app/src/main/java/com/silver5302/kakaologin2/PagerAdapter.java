package com.silver5302.kakaologin2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by alfo06-19 on 2017-09-13.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    Fragment[] fragments = new Fragment[2];
    String[] tabnames = new String[]{"용병모집", "용병지원"};

    public PagerAdapter(FragmentManager fm) {
        super(fm);
        fragments[0] = new RecruitFragment();
        fragments[1] = new SupportFragment();

    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabnames[position];
    }
}
