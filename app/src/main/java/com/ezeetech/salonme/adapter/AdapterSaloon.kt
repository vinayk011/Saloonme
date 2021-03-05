/*
 *  Created by Vinay on 25-2-2021 for EzeeTech.
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
import com.ezeetech.salonme.R
import com.ezeetech.salonme.ViewHolderBinding
import com.ezeetech.salonme.databinding.AdapterSaloonBinding
import com.ezeetech.salonme.listener.ItemClickListener
import com.ezeetech.salonme.model.Saloon
import java.util.ArrayList

class AdapterSaloon(
    private val mContext: Context,
    private val arrayList: ArrayList<Saloon>,
    private val listener: ItemClickListener<Saloon>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewDataBinding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(mContext),
            R.layout.adapter_saloon,
            parent,
            false
        )
        return ViewHolderBinding(viewDataBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val saloon = arrayList[holder.adapterPosition]
        val adapterSaloonBinding =
            (holder as ViewHolderBinding).binding as AdapterSaloonBinding
        adapterSaloonBinding.model = saloon
        adapterSaloonBinding.saloonCard.setOnClickListener {
            listener.onClicked(saloon)
        }
        adapterSaloonBinding.executePendingBindings()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}