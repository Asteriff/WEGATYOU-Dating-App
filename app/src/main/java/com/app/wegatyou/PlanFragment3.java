package com.app.wegatyou;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PlanFragment3 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan3, container, false);

        // Access and update the views in the fragment layout
        TextView planTitleTextView = view.findViewById(R.id.planTitleTextView);
        planTitleTextView.setText("Plan 3");

        return view;
    }
}