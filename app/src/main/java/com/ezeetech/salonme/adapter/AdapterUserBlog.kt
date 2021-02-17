/*
 *  Created by Vinay on 22-1-2021 for EzeeTech.
 *  Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ezeetech.salonme.R
import com.ezeetech.salonme.ViewHolderBinding
import com.ezeetech.salonme.databinding.AdapterUserBlogBinding
import com.ezeetech.salonme.listener.UserBlogActionListener
import com.ezeetech.salonme.model.UserBlog
import kotlinx.android.synthetic.main.adapter_user_blog.view.*
import java.util.ArrayList

class AdapterUserBlog(
    private val mContext: Context,
    private val arrayList: ArrayList<UserBlog>,
    private val onClick: UserBlogActionListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewDataBinding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(mContext),
            R.layout.adapter_user_blog,
            parent,
            false
        )
        return ViewHolderBinding(viewDataBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val userBlog = arrayList[holder.adapterPosition]
        val adapterUserBlogBinding =
            (holder as ViewHolderBinding).binding as AdapterUserBlogBinding
        adapterUserBlogBinding.model = userBlog
        adapterUserBlogBinding.actionReadMore.setOnClickListener {
            onClick.onReadMoreClicked(
                userBlog, adapterUserBlogBinding.blogImage, adapterUserBlogBinding.blogTitle
            )
        }
        adapterUserBlogBinding.executePendingBindings()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}