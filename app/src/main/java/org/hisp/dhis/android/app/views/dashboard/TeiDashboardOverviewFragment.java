package org.hisp.dhis.android.app.views.dashboard;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.hisp.dhis.android.app.R;

public class TeiDashboardOverviewFragment extends Fragment {


    private TeiEventFragment.OnFragmentInteractionListener onFragmentInteractionListener;

    public TeiDashboardOverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard_overview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewPager(view);
    }

    private void initViewPager(View view) {
        final ViewPager viewPager = ((ViewPager) view.findViewById(R.id.view_pager));
        viewPager.setAdapter(new DashboardPageAdapter(
                getActivity().getSupportFragmentManager()));
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(
                (tabLayout)));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void setOnFragmentInteractionListener(TeiEventFragment.OnFragmentInteractionListener onFragmentInteractionListener) {
        this.onFragmentInteractionListener = onFragmentInteractionListener;
    }

    private class DashboardPageAdapter extends FragmentPagerAdapter {

        public DashboardPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    TeiEventFragment teiEventFragment = new TeiEventFragment();
                    teiEventFragment.setOnFragmentInteractionListener(onFragmentInteractionListener);
                    return teiEventFragment;
                case 1:
                    return new TeiProfileFragment();
                case 2:
                    return new TeiWidgetFragment();
                default:
                    return new TeiEventFragment();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

    }
}
