/*
 *  Created by Vinay on 25-2-2021 for EzeeTech.
 *  Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme.listener

interface ItemClickListener<T> {
    fun onClicked(item: T)
}