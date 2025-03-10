package com.app.wegatyou;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PlanPagerAdapter extends FragmentPagerAdapter {
    public PlanPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        // Return the appropriate fragment based on the position
        if (position == 0) {
            return new PlanFragment1();
        } else if (position == 1) {
            return new PlanFragment2();
        } else {
            return new PlanFragment3();
        }
    }

    @Override
    public int getCount() {
        // Return the total number of fragments
        return 3;
    }
}
