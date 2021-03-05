package com.ezeetech.salonme.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.ezeetech.salonme.R
import com.ezeetech.salonme.ViewHolderBinding
import com.ezeetech.salonme.databinding.AdapterCategoryBinding
import com.ezeetech.salonme.listener.ItemClickListener
import com.ezeetech.salonme.model.StoreCategory
import java.util.*

/**
 * Created by Vinay on 22-01-2021 for EzeeTech.
 * Copyright (c) 2021  EzeeTech . All rights reserved.
 */

    class AdapterCategories(
    private val mContext: Context,
    private val arrayList: ArrayList<StoreCategory>,
    private val listener: ItemClickListener<StoreCategory>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewDataBinding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(mContext),
            R.layout.adapter_category,
            parent,
            false
        )
        return ViewHolderBinding(viewDataBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val storeCategory = arrayList[holder.adapterPosition]
        val adapterCategoryBinding =
            (holder as ViewHolderBinding).binding as AdapterCategoryBinding
        adapterCategoryBinding.layout.setOnClickListener { listener.onClicked(storeCategory) }
        adapterCategoryBinding.model = storeCategory
        adapterCategoryBinding.executePendingBindings()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}