/*
 *  Created by Vinay on 26-1-2021 for EzeeTech.
 *  Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.ezeetech.salonme.databinding.ViewProgressBinding
import com.salonme.base.BaseDialog
import com.salonme.base.inflateDialog

class LoadingView(context: Context) :
    BaseDialog<ViewProgressBinding>(context) {

    init {
        this.setCancelable(false)
        this.setCanceledOnTouchOutside(false)
    }

    public override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = inflateDialog(context, R.layout.view_progress) as ViewProgressBinding
        setContentView(binding.root)
        binding.message = context.getString(R.string.processing)
    }

    fun showWithMessage(message: String): Dialog {
        this.show()
        binding.message = message
        return this
    }
}