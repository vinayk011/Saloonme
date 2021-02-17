/*
 * *
 *  * Created by Vinay.
 *  * Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme.ui.widget.mobile_number

import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil


object PhoneNumberFormatter {

    private val phoneUtil = PhoneNumberUtil.getInstance()

    fun getFormattedNumber(number: String): Phone {
        val phone = Phone()
        try {
            val numberProto = phoneUtil.parse(number, "")
            phone.formatted =
                phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.NATIONAL)
            phone.national = numberProto.nationalNumber.toString()
            phone.countryCode = numberProto.countryCode.toString()
        } catch (e: NumberParseException) {
            e.printStackTrace()
            phone.formatted = number
            phone.national = number
        }

        return phone
    }

    class Phone {
        lateinit var formatted: String
        lateinit var national: String
        lateinit var countryCode: String
    }
}
