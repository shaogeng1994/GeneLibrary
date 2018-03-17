package com.shao.genelibrary.sample;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by Administrator on 2018/3/16.
 */

public class MainItemViewModel extends BaseObservable {

    private String mTitle;
    private String mViewTag;

    public MainItemViewModel(String title, String viewTag) {
        mTitle = title;
        mViewTag = viewTag;
    }

    @Bindable
    public String getTitle() {
        return mTitle;
    }

    public void onClick() {
        App.getInstance().startActivity(QuickActivity.newIntent(mViewTag));
    }

}
