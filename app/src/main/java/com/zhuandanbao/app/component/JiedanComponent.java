package com.zhuandanbao.app.component;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blog.www.guideview.Component;
import com.zhuandanbao.app.R;

/**
 * Created by BFTECH on 2017/1/19.
 */

public class JiedanComponent implements Component {
    @Override
    public View getView(LayoutInflater inflater) {
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.hint_grab_con_layout, null);
        ImageView hint = (ImageView) ll.findViewById(R.id.hint_img);
        hint.setImageResource(R.mipmap.img_queren_hint);
        return ll;
    }

    @Override
    public int getAnchor() {
        return Component.ANCHOR_TOP;
    }

    @Override
    public int getFitPosition() {
        return Component.FIT_END;
    }

    @Override
    public int getXOffset() {
        return 0;
    }

    @Override
    public int getYOffset() {
        return 0;
    }
}
