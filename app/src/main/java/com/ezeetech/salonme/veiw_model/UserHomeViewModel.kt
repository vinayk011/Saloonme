/*
 *  Created by Vinay on 3-2-2021 for EzeeTech.
 *  Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme.veiw_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ezeetech.salonme.model.UserBlog

class UserHomeViewModel(val app: Application) : AndroidViewModel(app){
    private val slidersViewModel  = MutableLiveData<Boolean>()

    val _sliders : LiveData<Boolean>
        get() = slidersViewModel
}