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
import com.ezeetech.salonme.R
import com.ezeetech.salonme.ViewHolderBinding
import com.ezeetech.salonme.databinding.AdapterBestDealBinding
import com.ezeetech.salonme.listener.BestDealClickListener
import com.ezeetech.salonme.model.BestDeal
import java.util.ArrayList

class AdapterBestDeal(private val mContext: Context,
                      private val arrayList: ArrayList<BestDeal>,
                      private val onClick: BestDealClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewDataBinding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(mContext),
            R.layout.adapter_best_deal,
            parent,
            false
        )
        return ViewHolderBinding(viewDataBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val bestDeal = arrayList[holder.adapterPosition]
        val adapterBestDealBinding =
            (holder as ViewHolderBinding).binding as AdapterBestDealBinding
        adapterBestDealBinding.cardView.setOnClickListener { onClick.onClicked(bestDeal) }
        adapterBestDealBinding.model = bestDeal
        adapterBestDealBinding.executePendingBindings()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}