package app.zhuandanbao.com.reslib.widget;
/**
 * @author harvic
 * @address blog.csdn.net/harvic880925
 * @date 2014-12-17
 */

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class MTimeTextView extends TextView implements Runnable{
	
	private Context cn;
	public MTimeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.cn = context;
	}
    private String startStr,endStr;
    private long mday, mhour, mmin, msecond;//天，小时，分钟，秒
    private boolean run=false; //是否启动了

    public void setTimes(long[] times,String startStr,String endStr) {
        mday = times[0];
        mhour = times[1];
        mmin = times[2];
        msecond = times[3];
        this.startStr = startStr;
        this.endStr = endStr;
    }

    /**
     * 倒计时计算
     */
	private void ComputeTime() {
	    msecond--;
	    if (msecond < 0) {
	        mmin--;
	        msecond = 59;
	        if (mmin < 0) {
	            mmin = 59;
	            mhour--;
	            if (mhour < 0) {
	                // 倒计时结束，一天有24个小时
	                mhour = 23;
	                mday--;
	            }
	        }
	    }
	}
    public boolean isFlagStart() {
        return run;
    }
    public void isStart() {
        this.run = true;
        run();
    }
    
    public void isStop(){
    	this.run = false;
    }
    @Override
    public void run() {
        //标示已经启动
        if(run){
        	if(mday==0 && mhour ==0 && mmin ==0 && msecond ==0) {
        		this.run =false;
        		this.setVisibility(View.GONE);
        	}else {
				setShowText();
        		postDelayed(this, 1000);
        	}
        }else {
        	removeCallbacks(this);
        }
    }

	public void setShowText(){
		ComputeTime();
		StringBuffer  strTime =new StringBuffer();
		if(startStr != null){
			strTime.append(startStr);
		}
		strTime.append("<font color='red'>"+mmin+ "</font>:<font color='red'>"+msecond+"</font>"+this.endStr);

//		strTime.append("<font color='red'>"+mday+"</font>天<font color='red'>"+mhour+
//				"</font>时<font color='red'>"+mmin+
//				"</font>分<font color='red'>"+msecond+"</font>秒"+this.endStr);
		this.setText(Html.fromHtml(strTime.toString()));
	}
}
