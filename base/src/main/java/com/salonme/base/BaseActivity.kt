/*
 * *
 *  * Created by Vinay.
 *  * Copyright (c) 2021 EzeeTech . All rights reserved.
 *
 */

package com.salonme.base

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleObserver
import com.salonme.base.permission.Permission
import com.salonme.base.permission.PermissionCallback
import com.salonme.base.permission.PermissionUtils
import java.util.HashSet

open class BaseActivity<T:ViewDataBinding> : AppCompatActivity(){
    protected lateinit var binding: T
    private val dialogs = HashSet<Dialog>()
    private val observers = HashSet<LifecycleObserver>()
    private var permissionCallback: PermissionCallback? = null

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    public override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    /*override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        displayBundleValues(intent.extras)
        displayIntentAction(intent)
    }
*/
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        displayBundleValues(intent?.extras)
        displayIntentAction(intent)
        newIntent(intent)
    }

    open fun newIntent(intent: Intent?) {}
    override fun onResume() {
        super.onResume()
        hideKeyboard(this)
        resume()
    }

    open fun resume() {}

    override fun onPause() {
        hideKeyboard(this)
        if (isFinishing) {
            clean()
        }
        pause()
        super.onPause()
    }

    open fun pause() {}

    override fun onStart() {
        super.onStart()
        start()
    }

    open fun start() {}

    override fun onStop() {
        stop()
        super.onStop()
    }

    open fun stop() {}
    override fun onDestroy() {
        destroy()
        clean()
        super.onDestroy()
    }

    private fun clean() {
        for (dialog in dialogs) {
            if (dialog.isShowing) {
                dialog.dismiss()
            }
        }
        dialogs.clear()
        for (observer in observers) {
            lifecycle.removeObserver(observer)
        }
        observers.clear()
    }

    open fun destroy() {}

    protected fun addLifecycleObserver(lifecycleObserver: LifecycleObserver) {
        lifecycle.addObserver(lifecycleObserver)
        observers.add(lifecycleObserver)
    }

    protected fun addDialog(dialog: Dialog): Dialog {
        if (!dialogs.add(dialog) && dialog.isShowing) {
            dialog.dismiss()
        }
        return dialog
    }

    protected fun removeDialog(dialog: Dialog) {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
        dialogs.remove(dialog)
    }

    fun requestPermission(permission: Permission, permissionCallback: PermissionCallback?) {
        this.permissionCallback = permissionCallback
        if (permissionCallback != null && !isFinishing) {
            if (!PermissionUtils.isGranted(this, permission)) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(permission.toString()),
                    PERMISSION_REQUEST_CODE
                )
            } else {
                permissionCallback.onPermissionResult(true, false)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        onPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun onPermissionsResult(
        requestCode: Int,
        permissions: Array<String>?,
        grantResults: IntArray?
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults != null && permissions != null) {
            for (i in grantResults.indices) {
                if (permissionCallback != null) {
                    permissionCallback!!.onPermissionResult(
                        grantResults[i] == PackageManager.PERMISSION_GRANTED,
                        !PermissionUtils.shouldShowRequestPermissionRationale(this, permissions[i])
                    )
                }
            }
        }
    }

    protected fun isPermissionGranted(permission: Permission): Boolean {
        return PermissionUtils.isGranted(this, permission)
    }
}