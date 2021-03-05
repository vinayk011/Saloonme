/*
 *  Created by Vinay on 2-2-2021 for EzeeTech.
 *  Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme.model

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import org.w3c.dom.Text
import java.text.DecimalFormat
import java.util.*


@BindingAdapter("app:load_Image_uri")
fun loadPoster(view: ImageView, url: String?) {
    Glide.with(view.context)
        .load(url)
        .into(view);
}

@BindingAdapter("app:reviews")
fun setReviews(view:TextView, reviews:Int){
    view.text = "($reviews)"
}

@BindingAdapter("app:place","app:category")
fun setPlaceAndCategory(view:TextView, place:String, category:String){
    view.text = "$place | $category"
}