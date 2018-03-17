package com.shao.genelibrary.widget

import android.annotation.SuppressLint
import android.content.Context
import android.os.CountDownTimer
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout

/**
 * Created by Administrator on 2017/12/2.
 */
class SideSlideLayout(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0):
        FrameLayout(context, attributeSet, defStyleAttr), Runnable {
    constructor(context: Context): this(context, null, 0)

    constructor(context: Context, attributeSet: AttributeSet): this(context, attributeSet, 0)

    var mState = State.CLOSE
    var isFirstMeasure = false
    var frontLayout: View? = null
    var backLayout: View? = null
    var originalFrontLm = 0
    var originalFrontRm = 0
    var originalBackLm = 0
    var originalBackRm = 0

    var bWidth = 0
    var bHeight = 0
    var bParams: MarginLayoutParams? = null
    var fWidth = 0
    var fHeight = 0
    var fParams: MarginLayoutParams? = null

    var timer: CountDownTimer? = null

    var mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when(msg?.what) {
                1 -> offsetOnceByAuto()
            }
        }
    }


    private fun measureChildren() {
        if (childCount < 2)
            throw IllegalStateException("SideSlideLayout must have 2 children at least")
//        if (getChildAt(0) !is FrameLayout || getChildAt(1) !is FrameLayout)
//            throw IllegalStateException("SideSlideLayout must extend FrameLayout")
        backLayout = getChildAt(0)
        frontLayout = getChildAt(1)
        if (backLayout == null || frontLayout == null) throw IllegalStateException("")
        bParams = backLayout?.layoutParams as MarginLayoutParams
        fParams = frontLayout?.layoutParams as MarginLayoutParams
        bWidth = backLayout?.measuredWidth!!
        bHeight = backLayout?.measuredHeight!!

        fWidth = frontLayout?.measuredWidth!!
        fHeight = frontLayout?.measuredHeight!!

        originalFrontLm = fParams!!.leftMargin
        originalFrontRm = fParams!!.rightMargin
        originalBackLm = bParams!!.leftMargin
        originalBackRm = bParams!!.rightMargin
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (!isFirstMeasure) {
            measureChildren()
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {


        var fl = fParams!!.leftMargin
        var fr = fWidth - fParams!!.rightMargin
        var ft = fParams!!.topMargin
        var fb = fHeight - fParams!!.bottomMargin


        var bl = bParams!!.leftMargin + fr
        var br = fr + bWidth + bParams!!.rightMargin
        var bt = bParams!!.topMargin
        var bb = bHeight - bParams!!.bottomMargin


        frontLayout?.layout(fl, ft, fr, fb)
        backLayout?.layout(bl, bt, br, bb)
    }


    var startX: Float = 0f
    var lastX: Float = 0f

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        var x = event?.x

        if (event?.action == MotionEvent.ACTION_DOWN) {
            startX = event?.x
            lastX = startX
            isFirstMeasure = true
            return true
        } else if (event?.action == MotionEvent.ACTION_MOVE) {
            var offsetX = (x?.minus(lastX))?.toInt()?: 0
            offset(offsetX)
            lastX = event?.x

            return true

        } else if (event?.action == MotionEvent.ACTION_UP || event?.action == MotionEvent.ACTION_CANCEL) {
            if (mState == State.OPENING) {
                timer = object : CountDownTimer(5000, 10) {
                    override fun onFinish() {

                    }

                    override fun onTick(millisUntilFinished: Long) {
                        mHandler.sendEmptyMessage(1)
                    }

                }.let { it.start() }
            } else if (mState == State.CLOSING) {
//                mHandler.post(this)
                timer = object : CountDownTimer(5000, 10) {
                    override fun onFinish() {

                    }

                    override fun onTick(millisUntilFinished: Long) {
                        mHandler.sendEmptyMessage(1)
                    }

                }.let { it.start() }
            }
        }

        return super.onTouchEvent(event)
    }

    private fun offset(o: Int) {

        //加上偏移
        (frontLayout?.layoutParams as MarginLayoutParams).leftMargin += o
        (frontLayout?.layoutParams as MarginLayoutParams).rightMargin -= o

        //是否超出范围
        when {
            originalFrontLm - (frontLayout?.layoutParams as MarginLayoutParams).leftMargin > bWidth -> setOpen()
            (frontLayout?.layoutParams as MarginLayoutParams).leftMargin - originalFrontLm > 0 -> setClose()
            else -> requestLayout()
        }

        //状态处理
        if (o > 4) {
            mState = State.CLOSING
        } else if (o < -4) {
            mState = State.OPENING
        }
    }


    fun setClose() {
        (frontLayout?.layoutParams as MarginLayoutParams).leftMargin = originalFrontLm
        (frontLayout?.layoutParams as MarginLayoutParams).rightMargin = originalFrontRm
        requestLayout()
        mState = State.CLOSE
        timer?.cancel()
    }

    fun setOpen() {
        (frontLayout?.layoutParams as MarginLayoutParams).leftMargin = originalFrontLm - bWidth
        (frontLayout?.layoutParams as MarginLayoutParams).rightMargin = originalFrontRm + bWidth
        requestLayout()
        mState = State.OPEN
        timer?.cancel()
    }

    override fun run() {
        while (mState == State.OPENING || mState == State.CLOSING) {
            if (mState == State.OPENING) {
                offset(-2)
            } else if (mState == State.CLOSING) {
                offset(2)
            }
        }
    }

    private fun offsetOnceByAuto() {
        if (mState == State.OPENING || mState == State.CLOSING) {
            if (mState == State.OPENING) {
                offset(-10)
            } else if (mState == State.CLOSING) {
                offset(10)
            }
        }
    }


    enum class State{
        OPEN, OPENING, CLOSING, CLOSE
    }
}