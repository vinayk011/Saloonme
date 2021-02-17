/*
 * *
 *  * Created by Vinay.
 *  * Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme.ui.login

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import com.ezeetech.salonme.R
import com.ezeetech.salonme.databinding.ActivityUserAccountBinding
import com.ezeetech.salonme.util.BlurBuilder
import com.salonme.base.BaseActivity
import com.salonme.base.inflateActivity
import kotlinx.android.synthetic.main.activity_user_account.*


class ActivityUserAccount : BaseActivity<ActivityUserAccountBinding>() {

    val navHostFragment: NavHostFragment by lazy {
        user_account_fragment as NavHostFragment
    }
    private val navInflater by lazy {
        navHostFragment.navController.navInflater
    }
    private val navGraph by lazy { navInflater.inflate(R.navigation.user_account) }

    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        binding =
            inflateActivity(this, R.layout.activity_user_account) as ActivityUserAccountBinding
        init()
    }

    private fun init() {
        window.apply {
            setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            statusBarColor = Color.TRANSPARENT
        }
        //set blur background image for login screen
        var bcgBitmap = BitmapFactory.decodeResource(resources, R.drawable.saloon_login)
        bcgBitmap = BlurBuilder.blur(this, bcgBitmap)
        binding.layoutUserAccount.background = BitmapDrawable(resources, bcgBitmap)
    }

    override fun onSupportNavigateUp(): Boolean{
      return  navHostFragment.navController.navigateUp() || super.onSupportNavigateUp()
    }
}

