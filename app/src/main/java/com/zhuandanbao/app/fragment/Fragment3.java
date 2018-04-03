package com.zhuandanbao.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhuandanbao.app.MainActivity;
import com.zhuandanbao.app.R;


public class Fragment3 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_3, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView textView = (TextView) getView().findViewById(R.id.tvInNew);
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 保存数据到SharePreference中以此判断是不是第一次登陆
                Intent helpIntent = new Intent(getActivity(), MainActivity.class);
                startActivity(helpIntent);
                getActivity().finish();
            }
        });
    }
}
