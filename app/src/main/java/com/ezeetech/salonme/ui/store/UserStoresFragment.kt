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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ezeetech.salonme.R
import com.ezeetech.salonme.adapter.AdapterSaloon
import com.ezeetech.salonme.databinding.FragmentUserStoresBinding
import com.ezeetech.salonme.listener.ItemClickListener
import com.ezeetech.salonme.model.Saloon
import com.ezeetech.salonme.veiw_model.SaloonViewModel
import com.salonme.base.BaseFragment
import com.salonme.base.CATEGORY
import com.salonme.base.Trace
import com.salonme.base.inflateFragment
import java.util.ArrayList

class UserStoresFragment : BaseFragment<FragmentUserStoresBinding>() {
    private var saloonList = ArrayList<Saloon>()
    private lateinit var viewModel: SaloonViewModel
    private val adapterSaloon by lazy {
        context?.let { AdapterSaloon(it, saloonList, saloonClickListener) }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as ActivityUserStores).toolBar(show = true, back = true)
        viewModel = ViewModelProvider(this).get(SaloonViewModel::class.java)
        initViewModel()
        viewModel.getUserBlogList()
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
        context?.let {
            binding.recyclerSaloon.layoutManager =
                LinearLayoutManager(context)
        }
    }

    private fun initViewModel() {
        viewModel._saloonViewModel.observe(viewLifecycleOwner, Observer {
            saloonList = it as ArrayList<Saloon>
            binding.recyclerSaloon.adapter = adapterSaloon
        })
    }

    private val saloonClickListener = object : ItemClickListener<Saloon> {
        override fun onClicked(item: Saloon) {
            Trace.i("Selected saloon: "+item.name)
        }
    }
}