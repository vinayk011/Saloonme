package com.salonme.base.permission

/**
 * Created by Vinay
 * Copyright (c) 2021 EzeeTech. All rights reserved.
 */

interface PermissionCallback {
    fun onPermissionResult(granted: Boolean, neverAsk: Boolean)
}