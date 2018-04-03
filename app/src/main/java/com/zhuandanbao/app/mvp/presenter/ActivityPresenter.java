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

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.zhuandanbao.app.mvp.view.IDelegate;
import com.zhuandanbao.app.utils.MLog;


/**
 * Presenter base class for Activity
 * Presenter层的实现基类
 *
 * @param <T> View delegate class type
 * @author kymjs (http://www.kymjs.com/) on 10/23/15.
 */
public abstract class ActivityPresenter<T extends IDelegate> extends AppCompatActivity {
    protected T viewDelegate;

    public ActivityPresenter() {
        try {
            viewDelegate = getDelegateClass().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("create IDelegate error");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("create IDelegate error");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createBinding(getLayoutInflater(), null, savedInstanceState);
        initToolbar();
        setContentView(viewDelegate.getRootView());
        viewDelegate.initWidget();
        bindEvenListener();
    }

    protected <D extends ViewDataBinding> D createBinding(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return viewDelegate.create(inflater, container, savedInstanceState);
    }

    protected void bindEvenListener() {
    }

    protected void initToolbar() {
        Toolbar toolbar = viewDelegate.getToolbar();
        MLog.e("===============1");
        if (null != toolbar) {
            toolbar.setVisibility(View.GONE);
            MLog.e("===============2");
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
        }
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (viewDelegate == null) {
            try {
                viewDelegate = getDelegateClass().newInstance();
                createBinding(getLayoutInflater(), null, savedInstanceState);
            } catch (InstantiationException e) {
                throw new RuntimeException("create IDelegate error");
            } catch (IllegalAccessException e) {
                throw new RuntimeException("create IDelegate error");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewDelegate = null;
    }

    protected abstract Class<T> getDelegateClass();
}