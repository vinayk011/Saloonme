/*
 *  Created by Vinay on 14-2-2021 for EzeeTech.
 *  Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme.ui.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.ezeetech.salonme.R
import com.ezeetech.salonme.databinding.FragmentUserStoresBinding
import com.ezeetech.salonme.ui.login.ActivityUserAccount
import com.salonme.base.BaseFragment
import com.salonme.base.CATEGORY
import com.salonme.base.Trace
import com.salonme.base.inflateFragment

class UserStoresFragment : BaseFragment<FragmentUserStoresBinding>() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as ActivityUserStores).toolBar(show = true, back = true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            it.getString(CATEGORY)?.let { it1 ->
                Trace.i("Title:$it1")
            }
        }
        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    if (!(activity as ActivityUserStores).navHostFragment.navController.popBackStack())
                        (activity as ActivityUserStores).finish()
                    else
                        (activity as ActivityUserStores).navHostFragment.navController.navigateUp()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflateFragment(
            inflater,
            container,
            R.layout.fragment_user_stores
        ) as FragmentUserStoresBinding
        observeClick(root)
        return root
    }

    override fun init() {

    }
}