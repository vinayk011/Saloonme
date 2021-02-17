package com.ezeetech.salonme.ui.signup

import android.content.Context
import androidx.databinding.ObservableField
import com.ezeetech.salonme.*
import com.ezeetech.salonme.network.model.SignInRequest
import com.ezeetech.salonme.network.model.SignUpRequest

/**
 * Created by Vinay.
 * Copyright (c) 2021 EzeeTech. All rights reserved.
 */

class SignUpModel {
    private val firstName = ObservableField<String>()
    private val lastName = ObservableField<String>()
    private val mobileNumber = ObservableField<String>()
    private val password = ObservableField<String>()
    private val confirmPassword = ObservableField<String>()
    private val email = ObservableField<String>()
    private val dateOfBirth = ObservableField<String>()

    val firstNameError = ObservableField<String>()
    val lastNameError = ObservableField<String>()
    val mobileNumberError = ObservableField<String>()
    val passwordError = ObservableField<String>()
    val confirmPasswordError = ObservableField<String>()
    val emailError = ObservableField<String>()
    val dobError = ObservableField<String>()

    fun getFirstName(): ObservableField<String> {
        firstNameError.set(null)
        return firstName
    }

    fun getLastName(): ObservableField<String>{
        lastNameError.set(null)
        return lastName
    }

    fun getMobileNumber(): ObservableField<String> {
        mobileNumberError.set(null)
        return mobileNumber
    }

    fun getPassword(): ObservableField<String> {
        passwordError.set(null)
        return password
    }

    fun getConfirmPassword(): ObservableField<String> {
        confirmPasswordError.set(null)
        return confirmPassword
    }

    fun getEmail(): ObservableField<String> {
        emailError.set(null)
        return email
    }

    fun getDateOfBirth(): ObservableField<String> {
        dobError.set(null)
        return dateOfBirth
    }

    private fun firstName(): String {
        firstName.get()?.let {
            return it
        }
        return ""
    }

    private fun lastName(): String {
        lastName.get()?.let {
            return it
        }
        return ""
    }

    private fun mobileNumber(): String {
        mobileNumber.get()?.let {
            return it
        }
        return ""
    }

    private fun nationalMobileNumber(): String {
        return mobileNumber().getNationalMobileNumber()
    }
    private fun password(): String {
        password.get()?.let {
            return it
        }
        return ""
    }

    private fun confirmPassword(): String {
        confirmPassword.get()?.let {
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

    private fun dateOfBirth(): String {
        dateOfBirth.get()?.let {
            return it
        }
        return ""
    }

    fun isValid(context: Context?): Boolean {
        firstNameError.set(null)
        lastNameError.set(null)
        mobileNumberError.set(null)
        passwordError.set(null)
        confirmPasswordError.set(null)
        emailError.set(null)
        dobError.set(null)
        if (firstName().isNotBlank() && lastName().isNotBlank() && mobileNumber().isNotBlank() && mobileNumber().isMobileNumber() && password().length == 4 &&
            password() == confirmPassword() && email().isEmailAddress() && dateOfBirth().isDate()
        ) {
            return true
        } else {
            if (firstName().isBlank()) {
                firstNameError.set(context?.getString(R.string.empty_first_name))
            }
            if (lastName().isBlank()) {
                lastNameError.set(context?.getString(R.string.empty_last_name))
            }
            if (mobileNumber().isBlank()) {
                mobileNumberError.set(context?.getString(R.string.empty_mobile_number))
            } else if (!mobileNumber().isMobileNumber()) {
                mobileNumberError.set(context?.getString(R.string.valid_mobile_number))
            }
            if (password().length != 4) {
                passwordError.set(context?.getString(R.string.valid_password))
            }
            if (confirmPassword() != password()) {
                confirmPasswordError.set(context?.getString(R.string.not_same_password))
            }
            if (email().isEmpty()) {
                emailError.set(context?.getString(R.string.enter_email))
            } else if (!email().isEmailAddress()) {
                emailError.set(context?.getString(R.string.valid_email))
            }
            if(dateOfBirth().isEmpty()){
                dobError.set("Enter date")
            }
        }
        return false
    }

    fun getNetworkRequestForSignIn(): SignInRequest =
        SignInRequest(email(), encrypt(password())/*, getAppId()*/)

    fun getNetworkRequest(): SignUpRequest =
        SignUpRequest(firstName(), lastName(), nationalMobileNumber(), encrypt(password()), email(), dateOfBirth())
}