package com.example.comlancer.Adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.comlancer.Fragments.CompaniesFragment;
import com.example.comlancer.Fragments.FreelancerFragment;
import com.example.comlancer.Fragments.TabFreelancerCompanyFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;
    private final TabFreelancerCompanyFragment mFragment;
    private CharSequence[] tabTitles = {"Companies", "Freelancers"};

    public TabsPagerAdapter(TabFreelancerCompanyFragment fragment, Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        mFragment = fragment;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                CompaniesFragment companiesFragment = new CompaniesFragment();
                companiesFragment.setListener(mFragment);
                return companiesFragment;
            case 1:
                FreelancerFragment freelancerFragment = new FreelancerFragment();
                freelancerFragment.setListener(mFragment);
                return freelancerFragment;


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