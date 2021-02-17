/*
 * *
 *  * Created by Vinay.
 *  * Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme.ui.widget.mobile_number

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.telephony.TelephonyManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.ezeetech.salonme.R
import com.ezeetech.salonme.databinding.ViewCountryCodePickerBinding
import com.ezeetech.salonme.fixString
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import java.util.*
import java.util.regex.Pattern



class MobileNumberView : LinearLayout {
    private var countryDialog: AlertDialog? = null
    private val displayList = ArrayList<CountryData>()
    private val originalList = ArrayList<CountryData>()
    private var hintLayout: TextInputLayout? = null
    private var countryCode: TextInputEditText? = null
    var mobileNumber: TextInputEditText? = null
        private set
    private var number: String = ""
    private var iso: String = "+"
    private var listener: NumberEventListener? = null

    val isValid: Boolean
        get() {
            setError(null)
            if (iso.isBlank()) {
                setError("Select country code")
                return false
            } else if (CountryCodeUtil.getCountryIsoCode(iso) == null) {
                setError("Select valid country code")
                return false
            } else if (mobileNumber?.text?.toString().isNullOrBlank()) {
                setError("Enter mobile number")
                return false
            } else if (number.isBlank() || !isNumberValid) {
                setError("Enter valid mobile number")
                return false
            }
            return true
        }

    private val isNumberValid: Boolean
        get() {
            try {
                val phoneUtil = PhoneNumberUtil.getInstance()
                val phoneNumber = phoneUtil.parse(iso + number, "")
                return phoneUtil.isValidNumber(phoneNumber)
            } catch (ignored: NumberParseException) {
            }

            return false
        }

    private var textWatcher: TextWatcher? = null

    private val MESSAGE_ID = 18
    private val handler = object : Handler(Looper.myLooper()!!) {
        override fun handleMessage(msg: Message) {
            if (msg.what == MESSAGE_ID) {
                var s = msg.obj as String
                if (iso.length > 1 && s.length > iso.length) {
                    s = s.replace("[\\D]".toRegex(), "")
                    val phone = PhoneNumberFormatter.getFormattedNumber(iso + s)
                    mobileNumber?.removeTextChangedListener(textWatcher)
                    var formattedNumber = phone.formatted.fixString()
                    if (formattedNumber.contains(iso) && formattedNumber.length > iso.length) {
                        formattedNumber =
                            formattedNumber.replace(Pattern.quote(iso).toRegex(), "")
                    }
                    mobileNumber?.setText(formattedNumber)
                    mobileNumber?.setSelection(formattedNumber.length)
                    mobileNumber?.addTextChangedListener(textWatcher)
                    number = phone.national
                    if (listener != null) {
                        listener?.onDone()
                    }
                } else {
                    number = ""
                    if (listener != null) {
                        listener?.onDone()
                    }
                }
            }
        }
    }

    interface NumberEventListener {
        fun onDone()
    }

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val styles = context.obtainStyledAttributes(attrs, R.styleable.MobileNumberView)
        val mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mInflater.inflate(R.layout.view_mobile_number, this)
        displayList.addAll(CountryCodeUtil.supportedCountries)
        originalList.addAll(displayList)
        hintLayout = findViewById(R.id.hintLayout)
        countryCode = findViewById(R.id.countryCode)
        mobileNumber = findViewById(R.id.mobileNumber)
        (findViewById<View>(R.id.hint) as TextInputEditText).keyListener = null
        countryCode?.keyListener = null
        countryCode?.setOnClickListener {
            if (countryDialog == null || countryDialog?.isShowing != true) {
                setError(null)
                countryDialog = initDialog()
                countryDialog?.show()
            }
        }
        setDefault()
        val hint = styles.getString(R.styleable.MobileNumberView_mn_hint)
        hintLayout?.hint = hint
        styles.recycle()
    }

    fun setError(error: String?) {
        hintLayout?.error = error
        hintLayout?.isErrorEnabled = error != null
    }

    private fun setCountryCode(code: String) {
        iso = code
        countryCode?.setText(code)
        setFormatter()
    }

    fun getNumber(): String {
        return iso + number
    }

    private fun setFormatter() {
        setWatcher()
        if (!TextUtils.isEmpty(number)) {
            mobileNumber?.setText(number)
        } else {
            val mNumber = mobileNumber?.text?.toString()
            mobileNumber?.setText(mNumber)
        }
    }

    private fun setWatcher() {
        if (textWatcher != null) {
            mobileNumber?.removeTextChangedListener(textWatcher)
        }
        textWatcher = null
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                handler.removeMessages(MESSAGE_ID)
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val message = Message()
                message.what = MESSAGE_ID
                message.obj = s.toString()
                handler.sendMessageDelayed(message, 400)
            }

            override fun afterTextChanged(s: Editable) {}
        }
        mobileNumber?.addTextChangedListener(textWatcher)
    }

    private fun setDefault() {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var countryTelephonyCode =
            CountryCodeUtil.getCountryTelephonyCode(
                telephonyManager.simCountryIso.toLowerCase(
                    Locale.getDefault()
                )
            )
        countryTelephonyCode = if (TextUtils.isEmpty(countryTelephonyCode)) {
            context.getString(R.string.plus)
        } else {
            context.getString(R.string.plus) + countryTelephonyCode
        }
        setCountryCode(countryTelephonyCode)
    }

    private fun initDialog(): AlertDialog {
        val builder = AlertDialog.Builder(this.context)
        val countryListAdapter = CountryListAdapter(this.context, displayList)
        val viewCountryCodePickerBinding = DataBindingUtil.inflate<ViewCountryCodePickerBinding>(
            LayoutInflater.from(this.context),
            R.layout.view_country_code_picker, this, false
        )
        viewCountryCodePickerBinding.searchCountryName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable?) {
                if (editable != null) {
                    val searchText = editable.toString()
                    displayList.clear()
                    if (searchText.isNotEmpty()) {
                        val toLowerCase = searchText.toLowerCase(Locale.getDefault())
                        for (mobileDataCountry in originalList) {
                            if (mobileDataCountry.countryFullName.toLowerCase(Locale.getDefault()).contains(
                                    toLowerCase
                                ) || mobileDataCountry.countryShortName.toLowerCase(Locale.getDefault()).contains(
                                    toLowerCase
                                )
                            ) {
                                displayList.add(mobileDataCountry)
                            }
                        }
                    } else {
                        displayList.addAll(originalList)
                    }
                    countryListAdapter.notifyDataSetChanged()
                }
            }
        })
        viewCountryCodePickerBinding.countyList.adapter = countryListAdapter
        viewCountryCodePickerBinding.cancelButton.setOnClickListener {
            if (countryDialog != null && countryDialog?.isShowing == true) {
                countryDialog?.dismiss()
            }
        }
        builder.setView(viewCountryCodePickerBinding.root)
        return builder.create()
    }

    fun setNumber(mobileNumber: String) {
        try {
            val phoneUtil = PhoneNumberUtil.getInstance()
            val phoneNumber = phoneUtil.parse(mobileNumber, "")
            if (phoneNumber == null || phoneNumber.nationalNumber.toString() == number) {
                return
            }
            this.mobileNumber?.setText(phoneNumber.nationalNumber.toString())
            this.mobileNumber?.text?.length?.let { this.mobileNumber?.setSelection(it) }
            setCountryCode(
                String.format(
                    context.getString(R.string.country_code_format),
                    phoneNumber.countryCode.toString()
                )
            )
        } catch (ignored: NumberParseException) {
        }

    }

    fun setNumberEventListener(listener: NumberEventListener) {
        this.listener = listener
    }

    private inner class CountryListAdapter internal constructor(
        private val context: Context,
        private val displayList: List<CountryData>
    ) : BaseAdapter() {

        override fun getCount(): Int {
            return this.displayList.size
        }

        override fun getItem(i: Int): CountryData {
            return this.displayList[i]
        }

        override fun getItemId(i: Int): Long {
            return i.toLong()
        }

        override fun getView(i: Int, v: View?, viewGroup: ViewGroup): View {
            var view = v
            if (view == null) {
                view =
                    LayoutInflater.from(context).inflate(R.layout.view_country_layout, null)
            }
            val (countryFullName, countryShortName, _, countryTelephonyCode) = getItem(i)
            val code = String.format(
                getContext().getString(R.string.country_code_format),
                countryTelephonyCode
            )
            (view?.findViewById<View>(R.id.country_row_item_full_name) as TextView).text =
                "$countryFullName ($countryShortName)"
            val textView = view.findViewById<TextView>(R.id.country_row_item_telephony_code)
            textView.visibility = View.VISIBLE
            textView.text = code
            view.setOnClickListener {
                setCountryCode(code)
                if (countryDialog != null && countryDialog?.isShowing == true) {
                    countryDialog?.dismiss()
                }
            }
            return view
        }
    }

    fun hideFocus(){
        mobileNumber?.clearFocus()
    }

    fun requestFocusForMobileNumber(){
        mobileNumber?.requestFocus()
    }
}