/*
 * *
 *  * Created by Vinay.
 *  * Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme.network.model

import android.app.Application


abstract class NetworkModelWithNoSession<Type : Any, Req, Res> protected constructor(application: Application) : NetworkModel<Type, Req, Res>(application, false)