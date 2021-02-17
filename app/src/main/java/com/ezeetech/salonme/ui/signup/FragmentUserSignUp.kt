package com.ezeetech.salonme.ui.signup

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Html
import android.text.InputType
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ezeetech.salonme.LoadingView
import com.ezeetech.salonme.R
import com.ezeetech.salonme.databinding.FragmentUserSignUpBinding
import com.ezeetech.salonme.home
import com.ezeetech.salonme.network.model.NetworkResponse
import com.ezeetech.salonme.network.model.SignInResponse
import com.ezeetech.salonme.network.model.SignUpResponse
import com.ezeetech.salonme.network_call.SignInNetworkCall
import com.ezeetech.salonme.network_call.SignUpNetworkCall
import com.ezeetech.salonme.ui.login.ActivityUserAccount
import com.ezeetech.salonme.util.DateUtil
import com.ezeetech.salonme.util.toDate
import com.google.android.material.datepicker.MaterialDatePicker
import com.salonme.base.*
import io.paperdb.Paper


class FragmentUserSignUp : BaseFragment<FragmentUserSignUpBinding>(), View.OnClickListener{
    private val model: SignUpModel by lazy {
        SignUpModel()
    }
    private val networkCall by lazy {
        ViewModelProvider(this).get(SignUpNetworkCall::class.java)
    }
    private val signInNetworkCall by lazy {
        ViewModelProvider(this).get(SignInNetworkCall::class.java)
    }
    private var networkLoader: LoadingView? = null

    private val materialDateBuilder by lazy {
        MaterialDatePicker.Builder.datePicker()
    }

    private val materialDatePicker by lazy {
        materialDateBuilder.build()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    (activity as ActivityUserAccount).navHostFragment.navController.navigateUp();
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
        // The callback can be enabled or disabled here or in the lambda
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflateFragment(
            inflater,
            container,
            R.layout.fragment_user_sign_up
        ) as FragmentUserSignUpBinding
        observeClick(root)
        return root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun init() {
        binding.termsServices.text = Html.fromHtml(
            getString(R.string.terms_services_text),
            HtmlCompat.FROM_HTML_MODE_COMPACT
        )
        binding.termsServices.isClickable = true
        binding.termsServices.movementMethod = LinkMovementMethod.getInstance()
        materialDateBuilder.setTitleText("Select a Date")
        materialDatePicker.addOnPositiveButtonClickListener {
            // if the user clicks on the positive
            // button that is ok button update the
            // selected date
            binding.dateOfBirth.setText(DateUtil.y4M2d2.format(it.toDate(convert = true)))
        }
        binding.model = model
        binding.signUpButton.setOnClickListener(this)
        binding.dateOfBirth.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                materialDatePicker.show(parentFragmentManager, "MATERIAL_DATE_PICKER");
            }
            false
        }
        binding.dateOfBirth.inputType = InputType.TYPE_NULL
        binding.dateOfBirth.keyListener = null
        networkCall.getResult().observe(this, networkResult)
        signInNetworkCall.getResult().observe(this, signInNetworkResult)
        binding.signUpButton.setOnClickListener {
            if (model.isValid(context)) {
                if (binding.termsServicesCheckbox.isChecked) {
//                    Toast.makeText(
//                        context,
//                        model.getNetworkRequest().toString(),
//                        Toast.LENGTH_SHORT
//                    ).show()
                    Trace.i("SignUp Request: ${model.getNetworkRequest()}")
                    if (networkLoader?.isShowing == true) {
                        networkLoader?.dismiss()
                    }
                    context?.let {
                        networkLoader = LoadingView(it)
                        networkLoader?.let { nl ->
                            addDialog(nl)
                            nl.show()
                        }
                        networkCall.execute(model.getNetworkRequest())
                    }
                } else {
                    showSnackBar(
                        getString(R.string.error_accept_terms),
                        binding.termsServicesCheckbox
                    )
                }
            }
        }
    }

    private val networkResult = Observer<NetworkResponse<SignUpResponse>> { res ->
        when (res) {
            is NetworkResponse.Success -> {
                if(res.response?.success!!) {
                    //signInNetworkCall.execute(model.getNetworkRequestForSignIn())

                    //todo remove below block once signin works fine
                    Preference.instance.putBoolean(PREF_PROFILE_ACTIVE, true)
                    Paper.book().write(DB_USER, model.getNetworkRequestForSignIn())
                    //Trace.i("Sign in Response: $res")
                    context?.let { home(it, null, true) }
                }else{
                    showSnackBar(res.response.message)
                }
            }
            is NetworkResponse.Error -> {
                networkLoader?.dismiss()
                if (res.error.isNotEmpty()) {
                    //showSnackBar(res.error[0].asString())
                }
            }
            is NetworkResponse.Exception -> {
                networkLoader?.dismiss()
                //showSnackBar(getString(R.string.something_went_wrong))
            }
        }
        model.getConfirmPassword().set("")
    }

    private val signInNetworkResult = Observer<NetworkResponse<SignInResponse>> { res ->
        networkLoader?.dismiss()
        when (res) {
            is NetworkResponse.Success -> {
                if(res.response?.success!!){
                    Preference.instance.putBoolean(PREF_PROFILE_ACTIVE, true)
                    Paper.book().write(DB_USER, model.getNetworkRequestForSignIn())
                    Trace.i("Sign in Response: $res")
                    context?.let { home(it, null, true) }
                }else{
                    showSnackBar(res.response.message)
                }
            }
            is NetworkResponse.Error -> {
                if (res.error.isNotEmpty()) {
                    //showSnackBar(res.error[0].asString())
                }
            }
            is NetworkResponse.Exception -> {
                //showSnackBar(getString(R.string.something_went_wrong))
            }
        }
        model.getPassword().set("")
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.sign_up_button) {
            if (model.isValid(context)) Toast.makeText(
                context,
                model.getNetworkRequest().toString(),
                Toast.LENGTH_SHORT
            ).show()
        }else if (v?.id == R.id.date_of_birth) {
            materialDatePicker.show(parentFragmentManager, "MATERIAL_DATE_PICKER");
        }else{
            Trace.i("")
        }
    }
}