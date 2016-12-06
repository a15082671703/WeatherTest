package com.example.a76568.weathertest;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by 76568 on 2016/11/29 0029.
 */
public class ViewPagerAdapter extends PagerAdapter {
    private List<View> views;
    private Context context;
    public ViewPagerAdapter(List<View> views,Context context){
        this.context = context;
        this.views = views;
    }
    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position));
        return views.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }
}
