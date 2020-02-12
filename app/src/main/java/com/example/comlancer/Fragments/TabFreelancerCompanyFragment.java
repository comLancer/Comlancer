package com.example.comlancer.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.comlancer.Adapter.TabsPagerAdapter;
import com.example.comlancer.Models.SearchListener;
import com.example.comlancer.Models.User;
import com.example.comlancer.R;
import com.google.android.material.tabs.TabLayout;

import static com.example.comlancer.Models.MyConstants.KEY_ALL_ITEMS;
import static com.example.comlancer.Models.MyConstants.KEY_CATEGORY;

public class TabFreelancerCompanyFragment extends Fragment implements CompaniesFragment.ComapaniesListenerInerface, FreelancerFragment.FreelancerListenerInerface {


    private Context mContext;
    private TabPageListener mListener;
    private SearchListener mSearchListener;
    private String mCategory;

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

    public static TabFreelancerCompanyFragment newInstance(String category) {
        Bundle args = new Bundle();
        TabFreelancerCompanyFragment fragment = new TabFreelancerCompanyFragment();
        args.putString(KEY_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCategory = getArguments().getString(KEY_CATEGORY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View parentView = inflater.inflate(R.layout.fragment_tab_freelancer_company, container, false);


        final EditText et_search = parentView.findViewById(R.id.et_search2);

        TabsPagerAdapter tabsPagerAdapter = new TabsPagerAdapter(TabFreelancerCompanyFragment.this, mContext, getChildFragmentManager());

        TabLayout tabs = parentView.findViewById(R.id.tabLayout);
        final ViewPager viewPager = parentView.findViewById(R.id.viewpager);

        viewPager.setAdapter(tabsPagerAdapter);
        tabs.setupWithViewPager(viewPager);

        if (mCategory != null) {
            et_search.setText(mCategory);
        }


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Fragment myFragment = getCurrentFragmentFromViewPager(viewPager);
                mSearchListener = (SearchListener) myFragment;

                String s = et_search.getText().toString();
                if (s.length() > 0) {
                    mSearchListener.onTextChanged(s);
                } else {
                    mSearchListener.onTextChanged(KEY_ALL_ITEMS);

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Fragment myFragment = getCurrentFragmentFromViewPager(viewPager);
        mSearchListener = (SearchListener) myFragment;


        //this code is used to watch changes on et_search, so if the user write something it will notify .
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (s.length() > 0) {
                    mSearchListener.onTextChanged(s.toString());
                } else {
                    mSearchListener.onTextChanged(KEY_ALL_ITEMS);

                }

            }
        });

        return parentView;
    }

    private Fragment getCurrentFragmentFromViewPager(ViewPager viewPager) {

        if (viewPager.getCurrentItem() == 0) {
            CompaniesFragment frag1 = (CompaniesFragment) viewPager
                    .getAdapter()
                    .instantiateItem(viewPager, viewPager.getCurrentItem());

            return frag1;

        } else if (viewPager.getCurrentItem() == 1) {
            FreelancerFragment frag2 = (FreelancerFragment) viewPager
                    .getAdapter()
                    .instantiateItem(viewPager, viewPager.getCurrentItem());

            return frag2;
        }
        return null;
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
