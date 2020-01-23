package com.example.comlancer.Adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.comlancer.Fragments.CompaniesFragment;
import com.example.comlancer.Fragments.FreelancerFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;
    private CharSequence[] tabTitles = {"Companies", "Freelancers"};

    public TabsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CompaniesFragment();
            case 1:
                return new FreelancerFragment();


            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}