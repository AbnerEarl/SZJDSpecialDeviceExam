package com.example.frank.jinding.Adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * PROJECT_NAME:CommemorativeBook
 * PACKAGE_NAME:com.example.frank.commemorativebook
 * USER:Frank
 * DATE:2018/6/18
 * TIME:15:45
 * DAY_NAME_FULL:星期一
 * DESCRIPTION:On the description and function of the document
 **/
public class ViewPagerAdapter extends PagerAdapter {

    private List<View> listView;

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(listView.get(position));
        return listView.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(listView.get(position));
    }

    public ViewPagerAdapter(List<View> listView){super();
        this.listView=listView;
    }

    @Override
    public int getCount() {
        return listView.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}