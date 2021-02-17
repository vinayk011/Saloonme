/*
 * *
 *  * Created by Vinay.
 *  * Copyright (c) 2021  Aivizen . All rights reserved.
 *
 */

package com.ezeetech.salonme.ui.action

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.NonNull
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavigationBehavior :
    CoordinatorLayout.Behavior<BottomNavigationView> {

    constructor(): super()

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    fun layoutDependsOn(
        parent: CoordinatorLayout?,
        child: BottomNavigationView?,
        dependency: View?
    ): Boolean {
        return dependency is FrameLayout
    }

    fun onStartNestedScroll(
        @NonNull coordinatorLayout: CoordinatorLayout?,
        @NonNull child: BottomNavigationView?,
        @NonNull directTargetChild: View?,
        @NonNull target: View?,
        axes: Int,
        type: Int
    ): Boolean {
        Log.d("BNV", "axes$axes, type$type")
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    fun onNestedPreScroll(
        @NonNull coordinatorLayout: CoordinatorLayout?,
        @NonNull child: BottomNavigationView,
        @NonNull target: View?,
        dx: Int,
        dy: Int,
        @NonNull consumed: IntArray?,
        type: Int
    ) {
        if (dy < 0) {
            showBottomNavigationView(child)
        } else if (dy > 0) {
            hideBottomNavigationView(child)
        }
    }

    private fun hideBottomNavigationView(view: BottomNavigationView) {
        view.animate().translationY(view.height.toFloat())
    }

    private fun showBottomNavigationView(view: BottomNavigationView) {
        view.animate().translationY(0F)
    }
}