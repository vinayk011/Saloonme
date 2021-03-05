/*
 *  Created by Vinay on 25-2-2021 for EzeeTech.
 *  Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme.model

data class Saloon (
    val name:String,
    val image:String,
    val rating :Float,
    val reviewsCount:Int,
    val place:String,
    val category:String,
    val openStatus:String
)