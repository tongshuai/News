package com.example.feicui.news2.ui;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.feicui.news2.R;
import com.example.feicui.news2.base.MyBaseActivity;

/**
 * Created by Administrator on 2016/10/31.
 */

public class ActivityLogo extends MyBaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        ImageView logo= (ImageView) findViewById(R.id.iv_logo);
        Animation animation= AnimationUtils.loadAnimation(this,R.anim.logo);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener(){

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                openActivity(MainActivity.class);
                ActivityLogo.this.finish();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        logo.setAnimation(animation);
    }
}
