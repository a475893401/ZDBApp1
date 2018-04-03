package com.zhuandanbao.app.mvp;


import com.baidu.mobstat.StatService;
import com.zhuandanbao.app.mvp.presenter.FragmentPresenter;
import com.zhuandanbao.app.mvp.view.IDelegate;
import com.zhuandanbao.app.utils.MLog;

/**
 * Created by BFTECH on 2017/1/25.
 */

public abstract class BaseFragment<T extends IDelegate> extends FragmentPresenter<T> {

    @Override
    protected void lazyLoad() {
        MLog.e("isVisible=" + isVisible + ";isInitView=" + isInitView + ";isFristAddData=" + isFristAddData);
        if (!isVisible || !isInitView || !isFristAddData) {
            return;
        }
        isFristAddData = false;
        initD();
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onPageStart(getActivity(),getClass().getName());
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPageEnd(getActivity(),getClass().getName());
    }
}
