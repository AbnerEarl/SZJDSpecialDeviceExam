package com.example.frank.jinding.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by DELL on 2018/1/14.
 */

public class SpinnerAdapter<T> extends ArrayAdapter {
    public SpinnerAdapter(@NonNull Context context, int resource, List<T> objects) {
        super(context,resource,objects);
    }

    @Override
    public int getCount() {
        int count = super.getCount();
        return count > 0 ? count - 1 : count;
    }
}
