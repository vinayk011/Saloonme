/*
 * *
 *  * Created by Vinay.
 *  * Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.salonme.base

import android.app.Activity
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import com.google.android.material.snackbar.Snackbar
import com.salonme.base.permission.Permission
import com.salonme.base.permission.PermissionCallback
import com.salonme.base.permission.PermissionUtils
import java.util.*


open class BaseFragment<T : ViewDataBinding> : Fragment() {
    protected lateinit var binding: T
    protected val root: View by lazy {
        binding.root
    }
    private val dialogs = HashSet<Dialog>()
    private val observers = HashSet<LifecycleObserver>()
    private var permissionCallback: PermissionCallback? = null

    protected fun observeClick(view: View) {
        view.setOnTouchListener { _, _ -> true }
    }

    open override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    open override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        retainInstance = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        displayBundleValues(arguments)
        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                   onBackPressed()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    open fun onBackPressed() {}
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    open fun init() {}

    override fun onResume() {
        super.onResume()
        resume()
    }

    open fun resume() {}

    override fun onPause() {
        if (isRemoving) {
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

    open fun destroy() {}
    open fun destroyView() {}

    override fun onDestroyView() {
        destroyView()
        super.onDestroyView()
    }

    override fun onDestroy() {
        destroy()
        clean()
        super.onDestroy()
        System.gc()
        Runtime.getRuntime().gc()
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
        context?.let {
            if (permissionCallback != null && !isRemoving) {
                if (!PermissionUtils.isGranted(it, permission)) {
                    requestPermissions(arrayOf(permission.toString()), PERMISSION_REQUEST_CODE)
                } else {
                    this.permissionCallback?.onPermissionResult(true, false)
                }
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
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (i in grantResults.indices) {
                activity?.let {
                    permissionCallback?.onPermissionResult(
                        grantResults[i] == PackageManager.PERMISSION_GRANTED,
                        !PermissionUtils.shouldShowRequestPermissionRationale(
                            it,
                            permissions[i]
                        )
                    )
                }
            }
        }
    }

    fun showSnackBar(message: String, anchorView: View) {
        snackBar(
            message,
            anchorView
        ).show()
    }
    fun showSnackBar(message: String) {
        snackBar(message).show()
    }

    fun showSnackBar(message: String, duration: Int) {
        snackBar(message, duration).show()
    }

    fun snackBar(message: String, anchorView: View): Snackbar =
        Snackbar.make(
            (context as Activity).findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_LONG
        ).setAnchorView(anchorView)

    fun snackBar(message: String): Snackbar =
        Snackbar.make(
            (context as Activity).findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG
        )

    fun snackBar(message: String, duration: Int): Snackbar =
        Snackbar.make((context as Activity).findViewById(android.R.id.content), message, duration)
}