package com.shao.genelibrary.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * Created by Administrator on 2017/11/4.
 */
class CalendarView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int)
    : View(context, attrs, defStyleAttr) {


    companion object {
        val FLAG_NORMAL = 1
        val FLAG_LATE = 2
        val FLAG_ABSENCE = 3
        val FLAG_REPAIR = 4
    }

    var mCalendar = Calendar.getInstance()
    var currentDay = Calendar.getInstance()
    private val textSize = sp2px(context, 14f).toFloat()
    private val per: Float = (resources.displayMetrics.widthPixels.div(750f))
    private val calWidth = 670 * per
    private val dayWidth = calWidth / 7f
    private val dayHeight = 64 * per
    private val days = ArrayList<Map<String, Any>>()
    private val tipMap = HashMap<Int, Int>()
    lateinit var mPaint: Paint


    constructor(context: Context): this(context, null, 0) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet): this(context, attrs, 0) {
        initView()
    }

    init {
        tipMap.put(1, FLAG_LATE)
        tipMap.put(2, FLAG_LATE)
        tipMap.put(3, FLAG_LATE)
        tipMap.put(4, FLAG_LATE)
        tipMap.put(5, FLAG_NORMAL)
        tipMap.put(6, FLAG_NORMAL)
        tipMap.put(7, FLAG_NORMAL)
        tipMap.put(8, FLAG_NORMAL)
        tipMap.put(9, FLAG_NORMAL)
        tipMap.put(10, FLAG_NORMAL)
        mCalendar.add(Calendar.MONTH, 2)
        initView()
    }

    fun putTipMap(map: Map<Int, Int>) {
        tipMap.putAll(map)
        invalidate()
    }

    fun setCalender(calendar: Calendar) {
        mCalendar = calendar.clone() as Calendar
        invalidate()
    }

    fun initView() {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.textSize = textSize.toFloat()

    }


    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        canvas?.drawColor(Color.WHITE)
        drawWeeks(canvas)
        drawDays(canvas)
    }


    private fun drawWeeks(canvas: Canvas?) {
        var x = 0f
        var y = 0f
        val weekHeight = textSize + 40 * per
        var text: String = ""
        val textStart = (calWidth / 7 - textSize * 2) / 2
        mPaint.color = 0xff333333.toInt()
        mPaint.textAlign = Paint.Align.LEFT
        for (i in 0..6) {
            x = i * calWidth / 7 + textStart
            y = weekHeight - 20 * per
            when(i) {
                0 -> text = "周日"
                1 -> text = "周一"
                2 -> text = "周二"
                3 -> text = "周三"
                4 -> text = "周四"
                5 -> text = "周五"
                6 -> text = "周六"
            }
            canvas?.drawText(text, x, y, mPaint)
        }
        mPaint.color = 0xFFE5E5E5.toInt()
        mPaint.strokeWidth = per
        canvas?.drawLine(textStart, weekHeight, calWidth - textStart, weekHeight, mPaint)
    }


    private fun drawDays(canvas: Canvas?) {
        val startY = textSize + 40 * per + 42 * per
        var startDay = Calendar.getInstance()
        var endDay = Calendar.getInstance()
        var x = 0
        var y = 0
        var weekOfMonth = 0
        var dayOfWeek = 0
        for (i in 1..mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {

            mCalendar.set(Calendar.DAY_OF_MONTH, i)
            dayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK) - 1
            val p = RectF(dayOfWeek * dayWidth,
                    startY + weekOfMonth * dayHeight,
                    dayOfWeek * dayWidth + dayWidth,
                    startY + (weekOfMonth + 1) * dayHeight)
            val map = HashMap<String, Any>()
            map.put("rectF", p)
            val cal = Calendar.getInstance()
            cal.set(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DATE))
            map.put("cal", cal)
            days.add(map)
            if (i == 1) {
                startDay = cal
            } else if (i == mCalendar.getActualMaximum(Calendar.DATE)) {
                endDay = cal
            }
            drawDayText(canvas, cal, p)

            if (mCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                weekOfMonth++
            }
        }


        //补全后面的日期
        if (endDay.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
            val dayNum = 7 - endDay.get(Calendar.DAY_OF_WEEK)
            nextMonth(mCalendar)
            for (i in 1..dayNum) {
                mCalendar.set(Calendar.DAY_OF_MONTH, i)

                dayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK) - 1
                val p = RectF(dayOfWeek * dayWidth,
                        startY + weekOfMonth * dayHeight,
                        dayOfWeek * dayWidth + dayWidth,
                        startY + (weekOfMonth + 1) * dayHeight)
                val map = HashMap<String, Any>()
                map.put("rectF", p)
                val cal = Calendar.getInstance()
                cal.set(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DATE))
                map.put("cal", cal)
                days.add(map)

                drawOtherDayText(canvas, cal, p)
            }
            previousMonth(mCalendar)
        }


        //补全前面的日期
        if (startDay.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            val dayNum = startDay.get(Calendar.DAY_OF_WEEK) - 1
            previousMonth(mCalendar)
            for (i in 1..dayNum) {
                mCalendar.set(Calendar.DAY_OF_MONTH, mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) - (dayNum - i))

                weekOfMonth = 0
                dayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK) - 1
                val p = RectF(dayOfWeek * dayWidth,
                        startY + weekOfMonth * dayHeight,
                        dayOfWeek * dayWidth + dayWidth,
                        startY + (weekOfMonth + 1) * dayHeight)
                val map = HashMap<String, Any>()
                map.put("rectF", p)
                val cal = Calendar.getInstance()
                cal.set(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DATE))
                map.put("cal", cal)
                days.add(0, map)

                drawOtherDayText(canvas, cal, p)
            }
            nextMonth(mCalendar)
        }


    }


    fun nextMonth(calendar: Calendar) {
        calendar.add(Calendar.MONTH, 1)
    }

    fun previousMonth(calendar: Calendar) {
        calendar.add(Calendar.MONTH, -1)
    }


    private fun drawDayText(canvas: Canvas?, calendar: Calendar, rectF: RectF) {

        val center = getRectCenter(rectF)
        val text = calendar.get(Calendar.DAY_OF_MONTH).toString()
        val textBgR = 22
        val currentBgR = 35
        mPaint.textAlign = Paint.Align.CENTER

        val fontMetrics = mPaint.fontMetrics
        val fontTotalHeight = fontMetrics.bottom - fontMetrics.top
        if (tipMap[calendar.get(Calendar.DATE)] != null) {
            when(tipMap[calendar.get(Calendar.DATE)]) {
                FLAG_NORMAL -> mPaint.color = 0xFF49B8FF.toInt()
                FLAG_ABSENCE -> mPaint.color = 0xFFFFCC66.toInt()
                FLAG_LATE -> mPaint.color = 0xFFFF6666.toInt()
                FLAG_REPAIR -> mPaint.color = 0xFF99CC00.toInt()
            }



            //画背景圆
            mPaint.style = Paint.Style.FILL
            if (currentDay?.getYear() == calendar.getYear()
                    && currentDay?.getMonth() == calendar.getMonth()
                    && currentDay?.getData() == calendar.getData()) {
                mPaint.color = mPaint.color - 0x99000000.toInt()
                canvas?.drawCircle(center[0], center[1], currentBgR * per, mPaint)
                mPaint.color = mPaint.color + 0x99000000.toInt()
                canvas?.drawCircle(center[0], center[1], (currentBgR - 8) * per, mPaint)
            } else {
                canvas?.drawCircle(center[0], center[1], textBgR * per, mPaint)
            }

            //数字
            mPaint.color = 0xffffffff.toInt()
            canvas?.drawText(text, center[0], center[1] +  fontTotalHeight / 4, mPaint)
        } else {
            mPaint.color = 0xff666666.toInt()
            canvas?.drawText(text, center[0], center[1] +  fontTotalHeight / 4, mPaint)
        }
    }

    private fun drawOtherDayText(canvas: Canvas?, calendar: Calendar, rectF: RectF) {
        val center = getRectCenter(rectF)
        val text = calendar.get(Calendar.DAY_OF_MONTH).toString()
        mPaint.textAlign = Paint.Align.CENTER

        val fontMetrics = mPaint.fontMetrics
        val fontTotalHeight = fontMetrics.bottom - fontMetrics.top
        mPaint.color = 0xffcccccc.toInt()
        canvas?.drawText(text, center[0], center[1] +  fontTotalHeight / 4, mPaint)
    }


    private fun getRectCenter(rectF: RectF): Array<Float> {
        val x = rectF.left + rectF.width() / 2f
        val y = rectF.top + rectF.height() / 2f
        return arrayOf(x, y)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event?.x
        val y = event?.y
        if (x == null || y == null) return super.onTouchEvent(event)

        if (event.action == MotionEvent.ACTION_UP) {
            val clickP = RectF(x, y, x, y)
            for (day in days) {
                val rectF = day["rectF"] as RectF
                val cal = day["cal"] as Calendar
                if (rectF.contains(clickP)) {
                    currentDay = cal
                    invalidate()
                }
            }
        }

        return true
    }



    fun Calendar.getData() = get(Calendar.DATE)

    fun Calendar.getYear() = get(Calendar.YEAR)

    fun Calendar.getMonth() = get(Calendar.MONTH)

    private fun sp2px(context: Context, spVal: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.resources.displayMetrics).toInt()
    }

}