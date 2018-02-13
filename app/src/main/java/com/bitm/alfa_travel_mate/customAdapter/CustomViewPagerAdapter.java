package com.bitm.alfa_travel_mate.customAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Jibunnisa on 5/20/2017.
 */

public class CustomViewPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    ArrayList<String> titles = new ArrayList<String>();

    public CustomViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void getFragments(Fragment fragment, String title) {

        this.fragments.add(fragment);
        this.titles.add(title);

    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
