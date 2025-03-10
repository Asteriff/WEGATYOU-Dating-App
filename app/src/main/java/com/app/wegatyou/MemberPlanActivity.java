package com.app.wegatyou;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

public class MemberPlanActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private PlanPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_plan);

        viewPager = findViewById(R.id.viewPager);
        pagerAdapter = new PlanPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        // Set the initial page to the first plan
        viewPager.setCurrentItem(0);
    }
}