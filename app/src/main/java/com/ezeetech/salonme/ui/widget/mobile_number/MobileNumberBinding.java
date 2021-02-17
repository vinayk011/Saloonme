/*
 * *
 *  * Created by Vinay.
 *  * Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme.ui.widget.mobile_number;


import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;
import androidx.databinding.InverseBindingMethod;
import androidx.databinding.InverseBindingMethods;


@InverseBindingMethods({
        @InverseBindingMethod(type = MobileNumberView.class, attribute = "number_input", event = "number_inputAttrChanged"),
})
public class MobileNumberBinding {
    @InverseBindingAdapter(attribute = "number_input", event = "number_inputAttrChanged")
    public static String getMobileNumber(MobileNumberView mobileNumberView) {
        return mobileNumberView.getNumber();
    }

    @BindingAdapter("number_input")
    public static void setMobileNumber(MobileNumberView mobileNumberView, String number) {
        mobileNumberView.setNumber(number == null ? "" : number);
    }

    @BindingAdapter("number_error")
    public static void setMobileNumberError(MobileNumberView mobileNumberView, String error) {
        mobileNumberView.setError(error);
    }

    @BindingAdapter(value = {"number_inputAttrChanged"}, requireAll = false)
    public static void setMobileNumberListener(final MobileNumberView mobileNumberView, final InverseBindingListener number_inputAttrChanged) {
        mobileNumberView.setNumberEventListener(new MobileNumberView.NumberEventListener() {
            @Override
            public void onDone() {
                number_inputAttrChanged.onChange();
            }
        });
    }
}
