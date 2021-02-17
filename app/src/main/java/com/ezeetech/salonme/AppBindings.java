/*
 * *
 *  * Created by Vinay.
 *  * Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme;

import androidx.databinding.BindingAdapter;

import com.google.android.material.textfield.TextInputLayout;

public class AppBindings {
    @BindingAdapter("password_error")
    public static void setPasswordError(TextInputLayout inputLayout, String error) {
        inputLayout.setError(error);
    }
}
