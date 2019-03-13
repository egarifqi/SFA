package com.example.sfa;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardPagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public DashboardPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new HistoricalFragment();
            case 1: return new TodayTaskFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}
