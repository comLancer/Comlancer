package com.example.comlancer.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.comlancer.Adapter.TabsPagerAdapter;
import com.example.comlancer.Models.User;
import com.example.comlancer.R;
import com.google.android.material.tabs.TabLayout;

public class TabFreelancerCompanyFragment extends Fragment implements CompaniesFragment.ComapaniesListenerInerface, FreelancerFragment.FreelancerListenerInerface {


    private Context mContext;
    private TabPageListener mListener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof TabPageListener) {
            mListener = (TabPageListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement TabPageListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View parentView = inflater.inflate(R.layout.fragment_tab_freelancer_company, container, false);


        TabsPagerAdapter tabsPagerAdapter = new TabsPagerAdapter(TabFreelancerCompanyFragment.this, mContext, getChildFragmentManager());

        TabLayout tabs = parentView.findViewById(R.id.tabLayout);
        ViewPager viewPager = parentView.findViewById(R.id.viewpager);

        viewPager.setAdapter(tabsPagerAdapter);
        tabs.setupWithViewPager(viewPager);

        return parentView;
    }


    @Override
    public void onItemClickCompany(User user) {

        onItemPressed(user);

    }


    @Override
    public void onItemClickFreelancer(User user) {
        onItemPressed(user);
    }


    void onItemPressed(User user) {

        if (mListener != null) {
            mListener.onItemClickTap(user);
        }

    }

    public interface TabPageListener {
        // TODO: Update argument type and name
        void onItemClickTap(User user);

    }
}
