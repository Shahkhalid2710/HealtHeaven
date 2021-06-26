package com.applocum.connecttomyhealth.ui

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class SwipeDisableViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {
    var checkIsEnabled:Boolean=true

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (checkIsEnabled) {
            super.onTouchEvent(event)
        } else false
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (checkIsEnabled) {
            super.onInterceptTouchEvent(event)
        } else false
    }

    fun setPagingEnabled(enabledd: Boolean) {
        this.checkIsEnabled = enabledd
    }

}