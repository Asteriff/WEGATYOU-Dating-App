package com.app.wegatyou;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class DateActivity extends AppCompatActivity {
    private RadioGroup dateRadioGroup;
    private String selectedDateOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);

        ImageButton Cross = findViewById(R.id.cross);
        Cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickcross();
            }
        });

        ImageButton Next = findViewById(R.id.next);
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicknext();
            }
        });

        initializeDateOptions();

        dateRadioGroup = findViewById(R.id.dateRadioGroup);
        dateRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = findViewById(checkedId);
                selectedDateOption = selectedRadioButton.getText().toString();
            }
        });
    }

    private void initializeDateOptions() {
        dateRadioGroup = findViewById(R.id.dateRadioGroup);
        dateRadioGroup.removeAllViews();

        addDateOption("Male");
        addDateOption("Female");
        addDateOption("Nonbinary");
        addDateOption("Everyone");
    }

    private void addDateOption(String option) {
        RadioButton radioButton = new RadioButton(this);
        radioButton.setText(option);
        radioButton.setId(View.generateViewId());
        dateRadioGroup.addView(radioButton);
    }

    private RadioButton findRadioButtonByText(String text) {
        int count = dateRadioGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = dateRadioGroup.getChildAt(i);
            if (view instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) view;
                if (radioButton.getText().toString().equals(text)) {
                    return radioButton;
                }
            }
        }
        return null;
    }

    public void clickcross() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void clicknext() {
        if (selectedDateOption != null) {
            Intent intent = new Intent(this, TermsandConditionsActivity.class);
            intent.putExtra("dateOption", selectedDateOption);
            startActivity(intent);
        } else {
            // Handle the case when no date option is selected
        }
    }
}
