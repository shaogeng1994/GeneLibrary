package com.shao.genelibrary.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.SurfaceHolder
import android.view.SurfaceView

/**
 * Created by Administrator on 2017/11/1.
 */
class AttendanceStatisticsView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int)
    : SurfaceView(context, attrs, defStyleAttr), SurfaceHolder.Callback, Runnable {

    constructor(context: Context): this(context, null, 0) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet): this(context, attrs, 0) {
        initView()
    }


    val COLOR_NORMAL = 0xFF49B8FF
    val COLOR_ABSENCE = 0xFFFFBF64
    val COLOR_LATE = 0xFFEF7373

    lateinit var mHolder: SurfaceHolder
    var mCanvas: Canvas? = null
    var mIsDrawing: Boolean = false
    var x = 360
    var y = 0
    var mPath: Path? = null
    var mPaint: Paint? = null
    val ovalWidth = 320
    val ovalStroke = 30
    val per: Float = (resources.displayMetrics.widthPixels.div(750f))
    var mAttendance: Attendance? = null

    init {
        mAttendance = Attendance(17, 3, 6)
        initView()
    }

    fun initView() {
        mHolder = holder
        mHolder.addCallback(this)
        isFocusable = true
        isFocusableInTouchMode = true
        keepScreenOn = true
        mPath = Path()
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint?.color = Color.RED
    }


    override fun surfaceCreated(p0: SurfaceHolder?) {
        mIsDrawing = true
        Thread(this).start()
    }

    fun setAttendance(attendance: Attendance) {
        mAttendance = attendance
        x = 0
        mIsDrawing = true
    }

    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {

    }

    override fun surfaceDestroyed(p0: SurfaceHolder?) {
        mIsDrawing = false
    }


    override fun run() {
        while (mIsDrawing) {
            draw()
            x += 3
        }
    }

    fun draw() {
        try {
            mCanvas = mHolder.lockCanvas()
            // draw sth
            mCanvas?.drawColor(Color.WHITE)
            val oval2 = RectF((width/2 -  ovalWidth*per/2),
                    (height/2 - ovalWidth*per/2),
                    (width/2 +  ovalWidth*per/2),
                    (height/2 + ovalWidth*per/2))
//
//            mPaint?.color = Color.BLUE
//            mCanvas?.drawRect(oval2, mPaint)

            val oval1 = RectF((width/2 -  ovalWidth*per/2 + ovalStroke*per),
                    (height/2 - ovalWidth*per/2 + ovalStroke*per),
                    (width/2 +  ovalWidth*per/2 - ovalStroke*per),
                    (height/2 + ovalWidth*per/2 - ovalStroke*per))



            val p1 = mAttendance?.getNormalPec()?.times(360)?: 0f
            val p2 = mAttendance?.getAbsencePec()?.times(360)?: 0f
            val p3 = mAttendance?.getLatePec()?.times(360)?: 0f


            //正常
            mPaint?.color = COLOR_NORMAL.toInt()
            if (x.toFloat() < p1) {
                mCanvas?.drawArc(oval2, -90f, x.toFloat(), true, mPaint)
            } else {
                mCanvas?.drawArc(oval2, -90f, p1, true, mPaint)

                //缺勤
                mPaint?.color = COLOR_ABSENCE.toInt()
                if (x.toFloat() in (p1 .. p1.plus(p2))) {
                    mCanvas?.drawArc(oval2, p1 - 90f, x.toFloat() - p1, true, mPaint)
                } else {
                    mCanvas?.drawArc(oval2, p1 - 90f, p2, true, mPaint)


                    //迟到
                    mPaint?.color = COLOR_LATE.toInt()
                    if (x.toFloat() in (p1.plus(p2)..p1.plus(p2).plus(p3))) {
                        mCanvas?.drawArc(oval2, p1 + p2 - 90f, x.toFloat() - p1 - p2, true, mPaint)
                    } else {
                        mCanvas?.drawArc(oval2, p1 + p2 - 90f, p3, true, mPaint)
                    }
                }
            }





            //去除中心
            mPaint?.color = Color.WHITE
            mCanvas?.drawArc(oval1, 0f, 360f, true, mPaint)

            if (x.toFloat() >= 360) {
                drawTips()
                mIsDrawing = false
            }
        } catch (e: Exception ) {

        } finally {
            if (mCanvas != null)
                mHolder.unlockCanvasAndPost(mCanvas)
        }
    }


    fun drawTips() {
        //求出弧度
        val p1 = mAttendance?.getNormalPec()?.times(Math.PI.times(2))?: 0.0
        val p2 = mAttendance?.getAbsencePec()?.times(Math.PI.times(2))?: 0.0
        val p3 = mAttendance?.getLatePec()?.times(Math.PI.times(2))?: 0.0


        val r = ovalWidth*per/2 + 40 * per

        //求出点的坐标
        val pTip1 = getTipPosition(p1/2.0, r.toDouble())
        val pTip2 = getTipPosition(p1 + p2/2.0, r.toDouble())
        val pTip3 = getTipPosition(p1 + p2 + p3/2.0, r.toDouble())


        drawTip(pTip1, COLOR_NORMAL.toInt(), "${mAttendance?.normal}天")
        drawTip(pTip2, COLOR_ABSENCE.toInt(), "${mAttendance?.absence}天")
        drawTip(pTip3, COLOR_LATE.toInt(), "${mAttendance?.late}天")


    }

    fun drawTip(p: Array<Float>, color: Int, string: String) {
        val centerX = width/2
        val centerY = height/2
        val pointR = 5 * per //小圆半径
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val lineLong1 = 60 * per//横直线长度
        val lineLong2 = 20 * per //斜直线长度
        val theta = Math.PI / 3 //斜线倾斜角度
        var startX = 0f
        var endX = 0f
        var startY = 0f
        var endY = 0f
        var textStartX = 0f
        var textStartY = 0f

        paint.color = color
        mCanvas?.drawCircle(p[0], p[1], pointR, paint)
        paint.strokeWidth = 1 * per
        paint.style = Paint.Style.STROKE
        paint.textSize = sp2px(context, 12f).toFloat()
        mCanvas?.drawCircle(p[0], p[1], pointR + 5 * per, paint)

        if (p[0] > centerX) {
            mCanvas?.drawLine(p[0], p[1], p[0] + lineLong1, p[1], paint)

            if (p[1] > centerY) {
                startX = p[0] + lineLong1
                startY = p[1]
                endX = p[0] + lineLong1 + lineLong2 * Math.cos(theta).toFloat()
                endY = p[1] - lineLong2 * Math.sin(theta).toFloat()
                textStartX = endX
                textStartY = endY
            } else {
                startX = p[0] + lineLong1
                startY = p[1]
                endX = p[0] + lineLong1 + lineLong2 * Math.cos(theta).toFloat()
                endY = p[1] + lineLong2 * Math.sin(theta).toFloat()
                textStartX = endX
                textStartY = endY + paint.textSize
            }
        } else {
            mCanvas?.drawLine(p[0], p[1], p[0] - lineLong1, p[1], paint)

            if (p[1] > centerY) {
                startX = p[0] - lineLong1
                startY = p[1]
                endX = p[0] - lineLong1  - lineLong2 * Math.cos(theta).toFloat()
                endY = p[1] - lineLong2 * Math.sin(theta).toFloat()
                textStartX = endX - string.length * paint.textSize
                textStartY = endY
            } else {
                startX = p[0] - lineLong1
                startY = p[1]
                endX = p[0] - lineLong1 - lineLong2 * Math.cos(theta).toFloat()
                endY = p[1] + lineLong2 * Math.sin(theta).toFloat()
                textStartX = endX - string.length * paint.textSize
                textStartY = endY + paint.textSize
            }
        }

        mCanvas?.drawLine(startX, startY, endX, endY, paint)



        paint.color = 0xff333333.toInt()
        paint.style = Paint.Style.FILL
        mCanvas?.drawText(string, textStartX, textStartY, paint)

    }


    fun getTipPosition(p: Double, r: Double): Array<Float> {
        val centerX = width/2
        val centerY = height/2
        val x = r.times(Math.sin(p))
        val y = r.times(Math.cos(p))

        return arrayOf((centerX + x).toFloat(), (centerY - y).toFloat())
    }


    data class Attendance(var normal: Int = 0, var absence: Int = 0, var late: Int = 0, var month: Int = 1) {

        fun getCount() = normal + absence + late

        fun getNormalPec(): Float {
            if (getCount() == 0) return 0f
            else return (normal.div(getCount().toFloat()))
        }

        fun getAbsencePec(): Float {
            if (getCount() == 0) return 0f
            else return (absence.div(getCount().toFloat()))
        }

        fun getLatePec(): Float {
            if (getCount() == 0) return 0f
            else return (late.div(getCount().toFloat()))
        }
    }

    private fun sp2px(context: Context, spVal: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.resources.displayMetrics).toInt()
    }

}