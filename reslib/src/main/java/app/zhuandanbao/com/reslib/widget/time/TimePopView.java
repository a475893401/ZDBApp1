package app.zhuandanbao.com.reslib.widget.time;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by BFTECH on 2016/3/29.
 */
public class TimePopView extends LinearLayout {

    private WheelTime wheelTime;

    public TimePopView(Context context) {
        super(context);
        initView(context);
    }

    public TimePopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TimePopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {


    }
}
