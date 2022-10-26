package com.example.android.demoanimation

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.SeekBar
import androidx.core.graphics.ColorUtils


/**
 * Created by Thinhvh on 04/10/2022.
 * Phone: 0398477967
 * Email: thinhvh.fpt@gmail.com
 */
class EqualizerSeekbar : View {
    private var paint: Paint = Paint()
    private var viewWidth = 0
    private var viewHeight = 0
    private var centerPoint = PointF()
    private var viewRect = Rect()
    private var scale = 1f
    private var onSeekBarChangeListener: OnSeekBarChangeListener? = null
    private var thumbRect = RectF()
    private var viewProgressRect = RectF()
    private var currentProgressRect = RectF()
    private var paddingVertical = 0f
    private var thumbSize = 0f
    private var progressWidth = 0f
    private var mProgress = 0

    companion object {
        const val DEFAULT_WIDTH = 40f //dp
        const val DEFAULT_HEIGHT = 200f //dp
        const val MAX_PROGRESS = 100
        const val MIN_PROGRESS = 0
        const val PROGRESS_STROKE_WIDTH = 7f //dp
        const val DEFAULT_COLOR = "#FF344D"
        const val BACKGROUND_COLOR = "#EAE9E9"
        const val COUNT = 25
        const val PADDING_VERTICAL = 16f//dp
        const val THUMB_SIZE = 24f//dp
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    scaleUpProgress()
                }
                MotionEvent.ACTION_UP -> {
                    performClick()
                    scaleDownProgress()
                }
                MotionEvent.ACTION_CANCEL -> {
                    scaleDownProgress()
                }
                MotionEvent.ACTION_MOVE -> {
                    var touchY = it.y
                    if (touchY < viewProgressRect.top) {
                        touchY = viewProgressRect.top
                    }
                    if (touchY > viewProgressRect.bottom) {
                        touchY = viewProgressRect.bottom
                    }
                    thumbRect.offsetTo(thumbRect.left, touchY)
                    invalidate()
                    mProgress = getProgress()
                    onSeekBarChangeListener?.onProgressChanged(mProgress)
                }
                else -> {}
            }
        }
        return true
    }

    private fun scaleUpProgress() {
        val anim = ValueAnimator.ofFloat(1f, 1.3f);
        anim.addUpdateListener {
            scale = it.animatedValue as Float
            invalidate()
        }
        anim.start()
    }

    private fun scaleDownProgress() {
        val anim = ValueAnimator.ofFloat(scale, 1f);
        anim.addUpdateListener {
            scale = it.animatedValue as Float
            invalidate()
        }
        anim.start()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w != oldw || h != oldh) {
            viewWidth = w
            viewHeight = h
            centerPoint.set((w / 2).toFloat(), (h / 2).toFloat())
            viewRect.set(0, 0, w, h)
            thumbRect.set(
                centerPoint.x - thumbSize / 2,
                paddingVertical,
                centerPoint.x + thumbSize / 2,
                thumbSize
            )

            viewProgressRect = RectF(
                centerPoint.x - progressWidth / 2,
                paddingVertical,
                centerPoint.x + progressWidth / 2,
                viewHeight.toFloat() - paddingVertical
            )


        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    fun dp2px(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }

    private fun init(context: Context) {
        paint.apply {
            flags = Paint.ANTI_ALIAS_FLAG
            color = Color.parseColor(DEFAULT_COLOR)
            paint.style = Paint.Style.FILL_AND_STROKE

            paddingVertical = dp2px(context, PADDING_VERTICAL)
            thumbSize = dp2px(context, THUMB_SIZE)
            progressWidth = dp2px(context, PROGRESS_STROKE_WIDTH)

        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            drawBackground(it)
            drawProgress(it)
        }
    }

    private fun drawProgress(canvas: Canvas) {
        val thumbY =
            ((100f - mProgress) / MAX_PROGRESS * viewProgressRect.height()) + paddingVertical
        thumbRect.offsetTo(thumbRect.left, thumbY)

        paint.color = Color.parseColor(BACKGROUND_COLOR)
        canvas.drawRect(viewProgressRect, paint)


        currentProgressRect = RectF(
            viewProgressRect.left,
            thumbRect.top,
            viewProgressRect.right,
            viewProgressRect.bottom
        )
        paint.color = Color.parseColor(DEFAULT_COLOR)
        canvas.drawRect(currentProgressRect, paint)
        //draw circle
        paint.color = ColorUtils.setAlphaComponent(Color.parseColor(DEFAULT_COLOR), 60)
        canvas.drawCircle(centerPoint.x, thumbRect.top, dp2px(context, 12f * scale), paint)
        paint.color = Color.WHITE
        canvas.drawCircle(centerPoint.x, thumbRect.top, dp2px(context, 9f * scale), paint)
        paint.color = Color.parseColor(DEFAULT_COLOR)
        canvas.drawCircle(centerPoint.x, thumbRect.top, dp2px(context, 3f * scale), paint)
    }

    private fun drawBackground(canvas: Canvas) {
        val size = (viewHeight - paddingVertical * 2 - dp2px(context, 2f)) / COUNT
        val height = dp2px(context, 2f)
        val width = dp2px(context, 12f)
        paint.color = Color.parseColor(BACKGROUND_COLOR)
        for (i in 0..COUNT) {
            val rectLeft = RectF(
                0f,
                (size * i).toFloat() + paddingVertical,
                width,
                size * i + height + paddingVertical
            )
            canvas.drawRect(rectLeft, paint)

            val rectRight =
                RectF(
                    viewWidth - width,
                    (size * i).toFloat() + paddingVertical,
                    viewWidth.toFloat(),
                    size * i + height + paddingVertical
                )
            canvas.drawRect(rectRight, paint)
        }
    }

    fun getProgress(): Int {
        val thumbY = thumbRect.top - paddingVertical
        val progressHeight = viewProgressRect.bottom - paddingVertical
        return 100 - (thumbY / progressHeight * 100).toInt()
    }

    fun setProgress(progress: Int) {
        mProgress = progress
        postInvalidate()
    }

    fun setOnSeekBarChangeListener(callabck: OnSeekBarChangeListener) {
        this.onSeekBarChangeListener = callabck
    }

}