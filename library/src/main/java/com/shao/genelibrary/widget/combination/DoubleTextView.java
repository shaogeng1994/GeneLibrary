package com.shao.genelibrary.widget.combination;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shao.genelibrary.R;


/**
 * Created by Administrator on 2018/3/6.
 */

public class DoubleTextView extends LinearLayout {

    private TextView firstTextView;
    private TextView secondTextView;


    public DoubleTextView(Context context) {
        this(context, null, 0);
    }

    public DoubleTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DoubleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }


    private void initView(Context context, AttributeSet attrs) {

        float firstTextSize = 0;
        float secondTextSize = 0;
        float space = 0;
        int firstTextColor = 0;
        int secondTextColor = 0;
        CharSequence firstText = "";
        CharSequence secondText = "";

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DoubleTextView);
            try {
                firstTextSize = ta.getDimensionPixelSize(R.styleable.DoubleTextView_dtv_first_text_size, sp2px(context, 14));
                secondTextSize = ta.getDimensionPixelSize(R.styleable.DoubleTextView_dtv_second_text_size, sp2px(context, 14));
                space = ta.getDimension(R.styleable.DoubleTextView_dtv_text_space, dip2px(context, 14));
                firstTextColor = ta.getColor(R.styleable.DoubleTextView_dtv_first_text_color, 0xff000000);
                secondTextColor = ta.getColor(R.styleable.DoubleTextView_dtv_second_text_color, 0xff000000);
                firstText = ta.getText(R.styleable.DoubleTextView_dtv_first_text);
                secondText = ta.getText(R.styleable.DoubleTextView_dtv_second_text);
            } finally {
                ta.recycle();
            }
        }

        firstTextView = new TextView(context);
        firstTextView.setText(firstText);
        firstTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, firstTextSize);
        firstTextView.setTextColor(firstTextColor);

        secondTextView = new TextView(context);
        secondTextView.setText(secondText);
        secondTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, secondTextSize);
        secondTextView.setTextColor(secondTextColor);


        addView(firstTextView);
        addView(secondTextView);

        LayoutParams layoutParams = (LayoutParams) firstTextView.getLayoutParams();
        if (getOrientation() == VERTICAL) {
            layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin, layoutParams.rightMargin, (int) space);
        } else if (getOrientation() == HORIZONTAL) {
            layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin, (int) space, layoutParams.bottomMargin);
        }
    }


    public void setFirstText(CharSequence text) {
        if (firstTextView != null) {
            firstTextView.setText(text);
        }
    }

    public void setSecondText(CharSequence text) {
        if (secondTextView != null) {
            secondTextView.setText(text);
        }
    }

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }
}
