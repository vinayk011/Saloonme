/*
 * *
 *  * Created by Vinay.
 *  * Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ViewFlipper

class CustomViewFlipper1 : ViewFlipper {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
    }

    override fun onDetachedFromWindow() {
        try {
            super.onDetachedFromWindow()
        } catch (e: IllegalArgumentException) {
            stopFlipping()
        }
    }
}
