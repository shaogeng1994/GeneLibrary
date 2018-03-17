package com.shao.genelibrary.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shao.genelibrary.widget.AttendanceStatisticsView;
import com.shao.genelibrary.widget.AttendanceView;
import com.shao.genelibrary.widget.CalendarView;


/**
 * Created by Administrator on 2018/3/17.
 */

public class QuickFragment extends Fragment {

    public static final String VIEW_DOUBLE_TEXT_VIEW = "DoubleTextView";
    public static final String VIEW_ATTENDANCE_STATISTICS = "AttendanceStatisticsView";
    public static final String VIEW_ATTENDANCE = "AttendanceView";
    public static final String VIEW_CALENDAR = "CalendarView";
    public static final String LAYOUT_SIDE_SIDE = "SideSlideLayout";


    private String mRootViewTag;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootViewTag = getArguments().getString("tag", "");
        View root = null;
        switch (mRootViewTag) {
            case VIEW_DOUBLE_TEXT_VIEW:
                root = inflater.inflate(R.layout.fragment_double_text_view, container, false);
                break;
            case VIEW_ATTENDANCE_STATISTICS:
                AttendanceStatisticsView view = new AttendanceStatisticsView(getContext());
                view.setAttendance(new AttendanceStatisticsView.Attendance(17, 3, 5, 1));
                root = view;

                break;
            case VIEW_ATTENDANCE:
                root = new AttendanceView(getContext());
                break;
            case VIEW_CALENDAR:
                root = new CalendarView(getContext());
                break;
            case LAYOUT_SIDE_SIDE:
                root = inflater.inflate(R.layout.fragment_side_slide_layout, container, false);
                break;
            default:
                break;
        }
        return root;
    }


    public static QuickFragment newInstance(String tag) {
        Bundle args = new Bundle();
        args.putString("tag", tag);
        QuickFragment fragment = new QuickFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
