/*
 *  Created by Vinay on 2-2-2021 for EzeeTech.
 *  Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme.model

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide


@BindingAdapter("app:load_Image_uri")
fun loadPoster(view: ImageView, url: String?) {
    Glide.with(view.context)
        .load(url)
        .into(view);
}
