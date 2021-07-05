package com.applocum.connecttomyhealth

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable


class SepratedProgress(
    private val mForeground: Int,
    private val mBackground: Int,
    var context: Context
) :
    Drawable() {
    private val mPaint = Paint()
    private val mSegment = RectF()
    override fun onLevelChange(level: Int): Boolean {
        invalidateSelf()
        return true
    }

    override fun draw(canvas: Canvas) {
        val level = level / 1000f
        val b = bounds
        val gapWidth = b.height() / 10f
        val segmentWidth = (b.width() - (NUM_SEGMENTS - 1) * gapWidth) / NUM_SEGMENTS
        mSegment[0f, 0f, segmentWidth] = b.height().toFloat()
        mPaint.color = mForeground
        for (i in 0 until NUM_SEGMENTS) {
            val loLevel = i / NUM_SEGMENTS.toFloat()
            val hiLevel = (i+1) / NUM_SEGMENTS.toFloat()
            if (level in loLevel..hiLevel) {
                //val middle = mSegment.left + NUM_SEGMENTS * segmentWidth * (level - loLevel)
                //canvas.drawRect(mSegment.left, mSegment.top, middle, mSegment.bottom, mPaint)
                val rectF= RectF(mSegment.left,mSegment.top,mSegment.right,mSegment.bottom)
                canvas.drawRoundRect(rectF,6f,6f,mPaint)
                mPaint.color = mBackground
                //canvas.drawRect(middle, mSegment.top, mSegment.right, mSegment.bottom, mPaint)
                val rectF2= RectF(mSegment.left,mSegment.top,mSegment.right,mSegment.bottom)
                canvas.drawRoundRect(rectF2,6f,6f,mPaint)
            } else {
               // canvas.drawRect(mSegment, mPaint)
                val rectF2= RectF(mSegment.left,mSegment.top,mSegment.right,mSegment.bottom)
                canvas.drawRoundRect(rectF2,6f,6f,mPaint)
            }
            mSegment.offset(mSegment.width() + gapWidth, 0f)
        }
    }

    override fun setAlpha(alpha: Int) {}
    override fun setColorFilter(cf: ColorFilter?) {}
    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    companion object {
        private const val NUM_SEGMENTS = 35
    }
}