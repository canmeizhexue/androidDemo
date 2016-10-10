package com.canmeizhexue.common.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.canmeizhexue.common.R;
import com.canmeizhexue.common.base.BaseFragment;
import com.canmeizhexue.common.utils.LogUtils;

/**
 * Created by silence on 2016-10-10.
 */
public class TextFragment extends BaseFragment{
    TextView textView;

    public TextFragment() {
        LogUtils.d("silence",getClass().getSimpleName()+"---"+this+"----TextFragment");
    }

    public static Fragment getInstance(String content){
        Fragment fragment = new TextFragment();
        Bundle args = new Bundle();
        args.putString("content",content);
        fragment.setArguments(args);
        return  fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtils.d("silence",getClass().getSimpleName()+"---"+this+"----onCreateView");
        View contentView = inflater.inflate(R.layout.fragment_text,null);
        textView = (TextView) contentView.findViewById(R.id.text);
        Bundle args = getArguments();
        if(args!=null){
            textView.setText(args.getString("content"));
        }

        return contentView;
    }

}
