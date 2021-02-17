/*
 * *
 *  * Created by Vinay.
 *  * Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme.ui.widget.mobile_number

import android.text.TextUtils
import com.ezeetech.salonme.App
import org.json.JSONObject
import java.io.IOException
import java.util.*



object CountryCodeUtil {
    private const val KEY_COUNTRIES = "countries"
    private const val KEY_NAME = "fullCountryName"
    private const val KEY_SHORT_NAME = "shortCountryName"
    private const val KEY_LOCALE = "locale"
    private const val KEY_TELEPHONY_CODE = "countryTelephonyCode"
    private const val COUNTRIES_JSON_FILE = "Countries.json"

    var supportedCountries: List<CountryData>

    init {
        supportedCountries = readMobileDataCountriesJSONFile()
    }

    private fun readMobileDataCountriesJSONFile(): List<CountryData> {
        val countryList = ArrayList<CountryData>()
        try {
            val jSONArray = JSONObject(loadJSONFromAsset()!!).getJSONArray(KEY_COUNTRIES)
            var i = 0
            while (i < jSONArray.length()) {
                val jSONObject = jSONArray.getJSONObject(i)
                countryList.add(
                    CountryData(
                        jSONObject.getString(KEY_NAME),
                        jSONObject.getString(KEY_SHORT_NAME),
                        jSONObject.getString(KEY_LOCALE),
                        jSONObject.getString(KEY_TELEPHONY_CODE)
                    )
                )
                i++
            }
        } catch (e: Exception) {
        }
        return countryList
    }

    fun getCountryTelephonyCode(countryIso: String): String? {
        var countryTelephonyCode: String? = null
        if (!TextUtils.isEmpty(countryIso)) {
            for (country in supportedCountries) {
                if (countryIso.toLowerCase(Locale.getDefault()) == country.locale.toLowerCase(Locale.getDefault())) {
                    countryTelephonyCode = country.countryTelephonyCode
                    break
                }
            }
        }
        return countryTelephonyCode
    }

    fun getCountryIsoCode(countryTelephonyCode: String): String? {
        var countryIso: String? = null
        if (!TextUtils.isEmpty(countryTelephonyCode)) {
            for (country in supportedCountries) {
                if (countryTelephonyCode == "+" + country.countryTelephonyCode) {
                    countryIso = country.locale
                    break
                }
            }
        }
        return countryIso
    }

    private fun loadJSONFromAsset(): String? {
        return try {
            val open = App.application.assets.open(COUNTRIES_JSON_FILE)
            val bArr = ByteArray(open.available())

            open.read(bArr)
            open.close()
            String(bArr)
        } catch (e: IOException) {
            null
        }
    }
}
