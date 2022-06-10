package com.bangkit.cemil.tools

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

//Custom RecyclerView that prevents touch events (to prevent manual scrolling)
class NoTouchScrollRecyclerView : RecyclerView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun onInterceptTouchEvent(e : MotionEvent) : Boolean {
        if (layoutManager?.isSmoothScrolling == true || scrollState == SCROLL_STATE_SETTLING) {
            return false
        }
        return super.onInterceptTouchEvent(e)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(e : MotionEvent) : Boolean {
        if (layoutManager?.isSmoothScrolling == true || scrollState == SCROLL_STATE_SETTLING) {
            return false
        }
        return super.onTouchEvent(e)
    }
}