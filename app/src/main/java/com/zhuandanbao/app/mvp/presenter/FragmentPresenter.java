/*
 * Copyright (c) 2015, 张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zhuandanbao.app.mvp.presenter;

import android.app.Activity;
import android.content.Context;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhuandanbao.app.R;
import com.zhuandanbao.app.mvp.view.IDelegate;


/**
 * Presenter base class for Fragment
 * Presenter层的实现基类
 *
 * @param <T> View delegate class type
 * @author kymjs (http://www.kymjs.com/) on 10/23/15.
 */
public abstract class FragmentPresenter<T extends IDelegate> extends Fragment {
    public T viewDelegate;
    /**
     * 是否初始化完成
     */
    protected boolean isInitView = false;
    /**
     * 是否视第一次加载数据
     */
    protected boolean isFristAddData = false;
    /**
     * Fragment当前状态是否可见
     */
    protected boolean isVisible;
    protected Activity activity;
    protected Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        activity = (Activity) context;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            viewDelegate = getDelegateClass().newInstance();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        createBinding(inflater, container, savedInstanceState);
        isInitView = true;
        isFristAddData = true;
        initToolbar();
        lazyLoad();
        return viewDelegate.getRootView();
    }

    protected void initToolbar() {
        Toolbar toolbar = viewDelegate.getToolbar();
        if (null != toolbar) {
            toolbar.setVisibility(View.GONE);
            toolbar.setTitleTextColor(getResources().getColor(R.color.withe));
            toolbar.setTitle("");
        }
    }

    protected <D extends ViewDataBinding> D createBinding(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return viewDelegate.create(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewDelegate.initWidget();
        bindEvenListener();
    }

    protected void bindEvenListener() {
        initView();
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (viewDelegate == null) {
            try {
                viewDelegate = getDelegateClass().newInstance();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 可见
     */
    protected void onVisible() {
        lazyLoad();
    }

    /**
     * 不可见
     */
    protected void onInvisible() {
    }

    /**
     * 延迟加载 子类必须重写此方法
     */
    protected abstract void lazyLoad();

    protected abstract void initD();

    /**
     * 初始化数据
     */
    public abstract void initView();

    protected abstract Class<T> getDelegateClass();
}
