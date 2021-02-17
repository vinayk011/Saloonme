/*
 *  Created by Vinay on 26-1-2021 for EzeeTech.
 *  Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme.util

import android.content.Context
import android.graphics.Bitmap
import androidx.renderscript.Allocation
import androidx.renderscript.Element
import androidx.renderscript.RenderScript
import androidx.renderscript.ScriptIntrinsicBlur
import kotlin.math.roundToInt

object BlurBuilder {
    private const val BITMAP_SCALE = 0.6f
    private const val BLUR_RADIUS = 7.5f
    fun blur(context: Context?, image: Bitmap): Bitmap {
        val width = (image.width * BITMAP_SCALE).roundToInt()
        val height = (image.height * BITMAP_SCALE).roundToInt()
        val inputBitmap = Bitmap.createScaledBitmap(image, width, height, false)
        val outputBitmap = Bitmap.createBitmap(inputBitmap)
        val rs =
            RenderScript.create(context)
        val theIntrinsic =
            ScriptIntrinsicBlur.create(
                rs,
                Element.U8_4(rs)
            )
        val tmpIn =
            Allocation.createFromBitmap(rs, inputBitmap)
        val tmpOut =
            Allocation.createFromBitmap(rs, outputBitmap)
        theIntrinsic.setRadius(BLUR_RADIUS)
        theIntrinsic.setInput(tmpIn)
        theIntrinsic.forEach(tmpOut)
        tmpOut.copyTo(outputBitmap)
        return outputBitmap
    }
}

