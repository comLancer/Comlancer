package com.example.comlancer.Activitys;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.comlancer.Adapter.TabsPagerAdapter;
import com.example.comlancer.Fragments.CompaniesFragment;
import com.example.comlancer.Fragments.FreelancerFragment;
import com.example.comlancer.Models.User;
import com.example.comlancer.R;
import com.google.android.material.tabs.TabLayout;

public class TabFreelancerCompanyActivity extends AppCompatActivity implements CompaniesFragment.ComapaniesListenerInerface, FreelancerFragment.FreelancerListenerInerface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_freelancer_company);

        TabsPagerAdapter tabsPagerAdapter = new TabsPagerAdapter(this, getSupportFragmentManager());

        TabLayout tabs = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.viewpager);

        viewPager.setAdapter(tabsPagerAdapter);
        tabs.setupWithViewPager(viewPager);


    }






    @Override
    public void onItemClick(User user) {

    }


}
