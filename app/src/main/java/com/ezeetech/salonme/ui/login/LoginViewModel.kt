/*
 * *
 *  * Created by Vinay.
 *  * Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme.ui.login

import android.content.Context
import androidx.databinding.ObservableField
import com.ezeetech.salonme.*
import com.ezeetech.salonme.network.model.SignInRequest


class LoginViewModel {
    private val email = ObservableField<String>()
    private val password = ObservableField<String>()

    val emailError = ObservableField<String>()
    val passwordError = ObservableField<String>()

    fun getPassword(): ObservableField<String> {
        passwordError.set(null)
        return password
    }

    fun getEmail(): ObservableField<String> {
        emailError.set(null)
        return email
    }

    private fun password(): String {
        password.get()?.let {
            return it
        }
        return ""
    }

    private fun email(): String {
        email.get()?.let {
            return it
        }
        return ""
    }

    fun isValid(context: Context?): Boolean {
        //mobileNumberError.set(null)
        emailError.set(null)
        passwordError.set(null)
        if (email().isEmailAddress() && password().length >= 4) {
            return true
        } else {
            if (email().isEmpty()) {
                emailError.set(context?.getString(R.string.enter_email))
            } else if (!email().isEmailAddress()) {
                emailError.set(context?.getString(R.string.valid_email))
            }
            if (password().isBlank()) {
                passwordError.set(context?.getString(R.string.empty_password))
            } else if (password().length < 4) {
                passwordError.set(context?.getString(R.string.valid_password))
            }
        }
        return false
    }

    fun getNetworkRequest(): SignInRequest =
        SignInRequest(email(), encrypt(password())/*encrypt(password()) getAppId()*/)
}