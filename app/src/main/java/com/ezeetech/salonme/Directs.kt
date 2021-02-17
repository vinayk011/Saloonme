/*
 * *
 *  * Created by Vinay.
 *  * Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.`in`.ezeetech.social.ActivitySocial
import com.ezeetech.salonme.ui.home.ActivityUserHome
import com.ezeetech.salonme.ui.login.ActivityUserAccount
import com.ezeetech.salonme.ui.store.ActivityUserStores
import com.salonme.base.PREF_PROFILE_ACTIVE
import com.salonme.base.PREF_RESET_PIN
import com.salonme.base.Preference
import com.salonme.base.LOGOUT
import com.salonme.base.REFRESH
import com.salonme.base.TYPE

fun signIn(context:Context, bundle:Bundle? = null){
    when {
        Preference.instance.getBoolean(PREF_PROFILE_ACTIVE) -> {
//        sync(context, bundle)
            home(context, bundle, finish = true)
        }
        Preference.instance.getBoolean(PREF_RESET_PIN) -> {
            logout(context, true)
        }
        else -> {
            val intent = Intent(context, ActivityUserAccount::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            (context as Activity).finish()
        }
    }
}

fun home(context: Context, bundle: Bundle? = null, finish: Boolean = false) {
    val intent = Intent(context, ActivityUserHome::class.java)
    bundle?.let { intent.putExtras(it) }
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
    if (finish) {
        (context as Activity).finish()
    }
}
fun stores(context: Context, bundle:Bundle? = null, finish: Boolean = false){
    val intent = Intent(context, ActivityUserStores::class.java)
    bundle?.let { intent.putExtras(it) }
    context.startActivity(intent)
    if (finish) {
        (context as Activity).finish()
    }
}
fun logout(context: Context, finish: Boolean = false) {

//    clearData()
//    clearDB()
    val intent = Intent(context, ActivityUserAccount::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
    if (finish) {
        (context as Activity).finish()    }
}

fun socialLogin(context: Context, type: String, refresh: Boolean = false, logout: Boolean = false) {
    val intent = Intent(context, ActivitySocial::class.java)
    intent.putExtra(TYPE, type)
    intent.putExtra(REFRESH, refresh)
    intent.putExtra(LOGOUT, logout)
    (context as Activity).startActivityForResult(intent, 100)
}
