/*
 *  Created by Vinay on 1-2-2021 for EzeeTech.
 *  Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme.handler

import com.salonme.base.permission.Permission
import com.salonme.base.permission.PermissionCallback

interface PermissionHandler {
    fun request(permission: Permission, permissionCallback: PermissionCallback)
    fun permissionGranted(permission: Permission)
}