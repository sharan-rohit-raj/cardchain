package com.example.cardchain;

import android.content.Context;
import android.content.res.Resources;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

public class OnboardingAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;
    public int[] onboarding_images;
    public String[] onboarding_headings;
    public String[] onboarding_desc;

    public OnboardingAdapter(Context context, int[] onboarding_images, String[] onboarding_headings, String[] onboarding_desc){
        this.context = context;
        this.onboarding_images = onboarding_images;
        this.onboarding_headings = onboarding_headings;
        this.onboarding_desc = onboarding_desc;
    }



    @Override
    public int getCount() {
        return onboarding_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.onboarding_screen, container, false);
        ImageView slideImage = view.findViewById(R.id.onboard_img);
        TextView slideTitle = view.findViewById(R.id.onboard_title);
        TextView slideDesc = view.findViewById(R.id.onboard_desc);

        slideImage.setImageResource(onboarding_images[position]);
        slideTitle.setText(onboarding_headings[position]);
        slideDesc.setText(onboarding_desc[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
