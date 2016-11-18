package com.example.feicui.news2.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2016/10/31.
 */

public class LeadImgAdapter extends PagerAdapter{

    private List<View> list;

    public LeadImgAdapter(List<View> list) {

        this.list = list;
    }

    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(list.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view=list.get(position);
        container.addView(view);
        return view;
    }
}
