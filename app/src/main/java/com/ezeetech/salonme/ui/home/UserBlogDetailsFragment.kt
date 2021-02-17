/*
 *  Created by Vinay on 2-2-2021 for EzeeTech.
 *  Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.transition.TransitionInflater
import com.ezeetech.salonme.R
import com.ezeetech.salonme.databinding.FragmentUserBlogDetailsBinding
import com.ezeetech.salonme.veiw_model.UserBlogDetailsViewModel
import com.salonme.base.BaseFragment
import com.salonme.base.inflateFragment

class UserBlogDetailsFragment : BaseFragment<FragmentUserBlogDetailsBinding>() {
    private lateinit var viewModel: UserBlogDetailsViewModel
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as ActivityUserHome).appBar(show = false, back = true)
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
            R.layout.fragment_user_blog_details
        ) as FragmentUserBlogDetailsBinding
        viewModel = ViewModelProvider(this).get(UserBlogDetailsViewModel::class.java)
        observeClick(root)
        return root
    }

    override fun init() {
        sharedElementEnterTransition = android.transition.TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
        initViewModel()
        binding.executePendingBindings()
    }

    private fun initViewModel() {
        binding.viewmodel = viewModel
        viewModel.userBlog.set(arguments?.getParcelable("userBlog"))
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onBackPressed() {
        (activity as ActivityUserHome).navHostFragment.navController.navigateUp();
    }
}