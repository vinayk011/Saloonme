/*
 *  Created by Vinay on 2-2-2021 for EzeeTech.
 *  Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme.veiw_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ezeetech.salonme.model.Saloon
import java.util.ArrayList

class SaloonViewModel(val app: Application) : AndroidViewModel(app) {
    private val saloonDataViewModel  = MutableLiveData<List<Saloon>>()
    val _saloonViewModel : LiveData<List<Saloon>>
        get() = saloonDataViewModel

    init {
        // init api network call
    }
    fun getUserBlogList(){
        saloonDataViewModel.value = setDefaultData()
    }

    private fun setDefaultData() : List<Saloon>{
        val saloonList = ArrayList<Saloon>()
        saloonList.add(
            Saloon(
                "Style Expert Unisex Saloon",
                "https://picsum.photos/id/1037/400/250",
                4.2f,35, "Miyapur", "Unisex","Open. until 9:00 pm "
            )
        )
        saloonList.add(
            Saloon(
                "Om Style Saloon",
                "https://picsum.photos/id/1037/400/250",
                5.0f,2, "Hafeezpet", "Unisex","Closed. Opens at 7:00 am"
            )
        )
        saloonList.add(
            Saloon(
                "Blowdry Saloon",
                "https://picsum.photos/id/1037/400/250",
                2.5f,20, "Hafeexpet", "Unisex","Closed. Opens at 7:00 am"
            )
        )

        return saloonList
    }
}