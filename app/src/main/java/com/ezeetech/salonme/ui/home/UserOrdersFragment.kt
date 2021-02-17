/*
 * *
 *  * Created by Vinay.
 *  * Copyright (c) 2021  Aivizen . All rights reserved.
 *
 */

package com.ezeetech.salonme.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ezeetech.salonme.R
import com.ezeetech.salonme.databinding.FragmentUserHomeBinding
import com.ezeetech.salonme.databinding.FragmentUserOrdersBinding
import com.salonme.base.BaseFragment
import com.salonme.base.inflateFragment

class UserOrdersFragment : BaseFragment<FragmentUserOrdersBinding>() {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as ActivityUserHome).appBar(show = false, back = false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflateFragment(
            inflater,
            container,
            R.layout.fragment_user_orders
        ) as FragmentUserOrdersBinding
        observeClick(root)
        return root
    }

    override fun init() {

    }


}
