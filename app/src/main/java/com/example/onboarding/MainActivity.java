package com.example.onboarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.onboarding.adapter.ViewPagerAdapter;
import com.example.onboarding.authentication.SignUpActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    ViewPager slideViewPager;
    LinearLayout dotLayout;

    Button backButton, nextButton , skipButton;

    TextView[] dots;

    ViewPagerAdapter viewPagerAdapter;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        backButton = findViewById(R.id.backButton);
        nextButton = findViewById(R.id.nextButton);
        skipButton = findViewById(R.id.skipButton);
        slideViewPager = findViewById(R.id.slideviewpager);
        dotLayout = findViewById(R.id.indicator_layout);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getItem(0) > 0 ){
                    slideViewPager.setCurrentItem(getItem(-1),true);
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(getItem(0) < 3){
                    slideViewPager.setCurrentItem(getItem(1),true);
                }else{
                    Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        viewPagerAdapter = new ViewPagerAdapter(MainActivity.this.getApplicationContext());

        slideViewPager.setAdapter(viewPagerAdapter);

        setUpIndicator(0);

        slideViewPager.addOnPageChangeListener(viewListener);
    }

    public void setUpIndicator(int position) {

        //this method is use to add dots (Copied from stack over flow)
        dots = new TextView[4];
         dotLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {

            dots[i] = new TextView(MainActivity.this.getApplicationContext());
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);

            dots[i].setTextColor(getResources().getColor(R.color.inactive, getApplicationContext().getTheme()));
            dotLayout.addView(dots[i]);

        }

        dots[position].setTextColor(getResources().getColor(R.color.active, getApplicationContext().getTheme()));


    }
        ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                setUpIndicator(position);

                if (position > 0) {
                    backButton.setVisibility(View.VISIBLE);
                } else {
                    backButton.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        };

    private int getItem(int i){
        return slideViewPager.getCurrentItem() + i;

    }

    @Override
    protected void onStart() {


        if(mAuth.getCurrentUser() != null){

            Intent intent = new Intent(MainActivity.this,WorkingActivity.class);
            startActivity(intent);
            finish();



        }
        super.onStart();
    }
}