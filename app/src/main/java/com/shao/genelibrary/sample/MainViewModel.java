package com.shao.genelibrary.sample;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/16.
 */

public class MainViewModel extends BaseObservable {

    private List<MainItemViewModel> mItemViewModelList = new ArrayList<>();

    public MainViewModel() {
        mItemViewModelList.add(new MainItemViewModel("DoubleTextView", QuickFragment.VIEW_DOUBLE_TEXT_VIEW));
        mItemViewModelList.add(new MainItemViewModel(QuickFragment.VIEW_ATTENDANCE_STATISTICS, QuickFragment.VIEW_ATTENDANCE_STATISTICS));
        mItemViewModelList.add(new MainItemViewModel(QuickFragment.VIEW_ATTENDANCE, QuickFragment.VIEW_ATTENDANCE));
        mItemViewModelList.add(new MainItemViewModel(QuickFragment.VIEW_CALENDAR, QuickFragment.VIEW_CALENDAR));
        mItemViewModelList.add(new MainItemViewModel(QuickFragment.LAYOUT_SIDE_SIDE, QuickFragment.LAYOUT_SIDE_SIDE));
    }

    @Bindable
    public List<MainItemViewModel> getItemViewModelList() {
        return mItemViewModelList;
    }


}
