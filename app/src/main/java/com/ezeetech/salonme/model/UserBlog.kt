/*
 *  Created by Vinay on 22-1-2021 for EzeeTech.
 *  Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserBlog(
    @field:SerializedName("blogId")
    val blogId : String,
    @field:SerializedName("blog_image")
    val blogWriterImage: String,
    @field:SerializedName("blog_title")
    val title: String,
    @field:SerializedName("blog_content")
    val content : String
) : Parcelable