/*
 *  Created by Vinay on 5-2-2021 for EzeeTech.
 *  Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme.util

import java.util.*

class Sample {
    var count: Int = 0
        get(){
            return mutableList.size
        }

    var evenBig:Int = 0
    get() = field
    set(value:Int){
        if (value % 2 == 0 && value > evenBig) {
            field =  value
        }
    }
    private val mutableList = mutableListOf<Int>()

    fun insert(value: Int): Int {
        mutableList.add(value)

        return Collections.frequency(mutableList, value)
    }

    fun delete(value: Int): Int{
        mutableList.remove(value)
        return Collections.frequency(mutableList, value)
    }

    fun count():Int{
        return mutableList.size
    }
}