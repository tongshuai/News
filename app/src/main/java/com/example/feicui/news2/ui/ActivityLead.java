package com.example.feicui.news2.ui;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.example.feicui.news2.R;
import com.example.feicui.news2.adapter.LeadImgAdapter;
import com.example.feicui.news2.base.MyBaseActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/31.
 */

public class ActivityLead extends MyBaseActivity {

    private ViewPager myviewPager;

    private ImageView[] points = new ImageView[4];

    private LeadImgAdapter adapter;
    private SlidingMenu slidingMenu;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead);
        SharedPreferences sharedPreferences=getSharedPreferences("runconfig",Context.MODE_PRIVATE);
        boolean isfirstrun=sharedPreferences.getBoolean("isFirstRun",true);
        if(!isfirstrun){
            openActivity(ActivityLogo.class);
        }
        initView();




    }


    private void initView(){
        List<View> viewList = new ArrayList<View>();
        viewList.add(getLayoutInflater().inflate(R.layout.lead_1, null));
        viewList.add(getLayoutInflater().inflate(R.layout.lead_2, null));
        viewList.add(getLayoutInflater().inflate(R.layout.lead_3, null));
        viewList.add(getLayoutInflater().inflate(R.layout.lead_4, null));

        points[0] = (ImageView) findViewById(R.id.iv_lead1);
        points[1] = (ImageView) findViewById(R.id.iv_lead2);
        points[2] = (ImageView) findViewById(R.id.iv_lead3);
        points[3] = (ImageView) findViewById(R.id.iv_lead4);
        setPoint(0);

        myviewPager = (ViewPager) findViewById(R.id.vp_lead);
        adapter = new LeadImgAdapter(viewList);
        myviewPager.setAdapter(adapter);
        myviewPager.setOnPageChangeListener(listener);
    }
    private void setPoint(int index) {
        for (int i = 0; i < points.length; i++) {
            if (i == index) {
                points[i].setAlpha(225);
            } else {
                points[i].setAlpha(100);
            }
        }
    }

    private ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            setPoint(position);
            if (position >= 3) {
                openActivity(ActivityLogo.class);
                finish();
                SharedPreferences preferences = getSharedPreferences("runconfig", MODE_PRIVATE);
                SharedPreferences.Editor edito = preferences.edit();
                edito.putBoolean("isFirstRun", false);
                edito.commit();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    private void Isfrist(){
        SharedPreferences sharedPreferences=getSharedPreferences("key", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("isfirst",false);
        editor.commit();
    }

}
