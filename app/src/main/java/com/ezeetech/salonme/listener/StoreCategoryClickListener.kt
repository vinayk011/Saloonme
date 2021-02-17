/*
 *  Created by Vinay on 22-1-2021 for EzeeTech.
 *  Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme.listener

import com.ezeetech.salonme.model.StoreCategory

interface StoreCategoryClickListener {
    fun onClicked(category: StoreCategory)
}