package com.henau.pictureselect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by admin on 2016/4/2.
 */
public class TestFragment extends Fragment {

    private static String TEST_KEY = "test";
    private String mTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if(bundle != null){
            mTitle = bundle.getString(TEST_KEY);
        }
        TextView textView = new TextView(getContext());
        textView.setText(mTitle);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    public static TestFragment newInstance(String title){
        Bundle bundle = new Bundle();
        bundle.putString(TEST_KEY,title);

        TestFragment fragment = new TestFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
