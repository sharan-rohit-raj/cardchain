package com.example.cardchain;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OnBoardingActitivity extends AppCompatActivity {
    ViewPager onBoardingSlides;
    LinearLayout dotsLayout;
    OnboardingAdapter onboardingAdapter;
    public static String onBoardingCompleted = "ONBOARDING_PROCESS";
    public int[] onboarding_images;
    public String[] onboarding_headings;
    public String[] onboarding_desc;
    private TextView[] mDots;
    Button nextBtn, prevBtn;
    int currPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding_actitivity);
        onBoardingSlides = findViewById(R.id.onboarding_slides);
        nextBtn = findViewById(R.id.nxt_btn);
        prevBtn = findViewById(R.id.prev_btn);
        prevBtn.setEnabled(false);
        prevBtn.setVisibility(View.INVISIBLE);
        dotsLayout = findViewById(R.id.linear_dots);
        Context context = this.getApplicationContext();
        onboarding_images = new int[]{
                R.drawable.welcome_amico,
                R.drawable.sharing_ideas_pana,
                R.drawable.launching_amico
        };

        onboarding_headings = new String[]{
                getString(R.string.os_title_1),
                getString(R.string.os_title_2),
                getString(R.string.os_title_3)
        };

        onboarding_desc = new String[]{
                getString(R.string.os_desc_1),
                getString(R.string.os_desc_2),
                getString(R.string.os_desc_3)
        };
        onboardingAdapter = new OnboardingAdapter(context, onboarding_images, onboarding_headings, onboarding_desc);
        onBoardingSlides.setAdapter(onboardingAdapter);
        addDots(0);
        onBoardingSlides.addOnPageChangeListener(viewListener);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mDots.length-1 == currPage){
                    OnBoardingActitivity.this.setResult(11);
                    finish();
                }
                onBoardingSlides.setCurrentItem(currPage + 1);
            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBoardingSlides.setCurrentItem(currPage - 1);
            }
        });
    }

    public void addDots(int pos){
        mDots = new TextView[3];
        dotsLayout.removeAllViews();

        for(int i =0; i < mDots.length; i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getColor(R.color.colorPrimaryDark));
            dotsLayout.addView(mDots[i]);
        }

        if(mDots.length > 0){
            mDots[pos].setTextColor(getColor(R.color.dot_color));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            currPage = position;

            if(position == 0){
                prevBtn.setVisibility(View.INVISIBLE);
                prevBtn.setEnabled(false);
                nextBtn.setEnabled(true);
                nextBtn.setText(getText(R.string.next));
            }else if(position == mDots.length-1){
                nextBtn.setText(getText(R.string.begin));
                nextBtn.setEnabled(true);
            }
            else{
                prevBtn.setVisibility(View.VISIBLE);
                prevBtn.setEnabled(true);
                nextBtn.setEnabled(true);
                nextBtn.setText(getText(R.string.next));
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}