/*
 * *
 *  * Created by Vinay.
 *  * Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme.ui.login

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.`in`.ezeetech.social.FACEBOOK
import com.`in`.ezeetech.social.GOOGLE
import com.ezeetech.salonme.*
import com.ezeetech.salonme.R
import com.ezeetech.salonme.databinding.FragmentLoginBinding
import com.ezeetech.salonme.network.model.NetworkResponse
import com.ezeetech.salonme.network.model.SignInRequest
import com.ezeetech.salonme.network.model.SignInResponse
import com.ezeetech.salonme.network_call.SignInNetworkCall
import com.ezeetech.salonme.util.BlurBuilder
import com.salonme.base.*


import io.paperdb.Paper


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val loginViewModel: LoginViewModel by lazy {
        LoginViewModel()
    }

    private val networkCall by lazy {
        ViewModelProvider(this).get(SignInNetworkCall::class.java)
    }

    private var networkLoader: LoadingView? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //(activity as ActivityUserAccount).toolBar(show = false, back = false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflateFragment(
            inflater,
            container,
            R.layout.fragment_login
        ) as FragmentLoginBinding
        observeClick(root)
        return root
    }

    override fun init() {
        binding.model = loginViewModel
        binding.handler = loginHandler
        binding.email.requestFocus()

        binding.password.apply {
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginHandler.signIn()
                }
                false
            }
        }

        networkCall.getResult().observe(this, networkResult)
    }

    private val loginHandler = object : LoginHandler {
        override fun signIn() {
            hideKeyboard(activity)
            if (loginViewModel.isValid(context)) {
                val signInRequest: SignInRequest = loginViewModel.getNetworkRequest()
                Trace.i("*******************Sign in Request encrypted: $signInRequest")

                Trace.i("*******************Sign in Request decrypted: "+ decrypt(signInRequest.password))
                if (networkLoader?.isShowing == true) {
                    networkLoader?.dismiss()
                }
                context?.let {
                    networkLoader = LoadingView(it)
                    networkLoader?.let { nl ->
                        addDialog(nl)
                        nl.show()
                    }
                    Paper.book().delete(DB_USER)
                    networkCall.execute(loginViewModel.getNetworkRequest())
                }
            }
        }

        override fun forgotPassword() {
            /* loginViewModel.getPassword().set("")
             val bundle = bundleOf(
                 Pair("mobile", loginViewModel.getMobileNumber().get())
             )*/
            /* (activity as ActivityUserAccount).navHostFragment.navController.navigate(
                 R.id.action_SignIn_to_ForgotPin,
                 bundle
             )*/
        }

        override fun register() {
            (activity as ActivityUserAccount).navHostFragment.navController.navigate(R.id.action_SignIn_to_SignUp)
        }

        override fun google() {
            context?.let { socialLogin(it, GOOGLE) }
        }

        override fun facebook() {
            context?.let { socialLogin(it, FACEBOOK) }
        }
    }
    private val networkResult = Observer<NetworkResponse<SignInResponse>> { res ->
        networkLoader?.dismiss()
        when (res) {
            is NetworkResponse.Success -> {
                if(res.response?.success!!){
                    Trace.i("Sign in Response: $res")
                    context?.let { home(it, null, true) }
                }else{
                    showSnackBar(res.response.message)
                }
            }
            is NetworkResponse.Error -> {
                if (res.error.isNotEmpty()) {
                    // showSnackBar(res.error[0].asString())
                }
            }
            is NetworkResponse.Exception -> {
                //showSnackBar(getString(R.string.something_went_wrong))
            }
        }
        loginViewModel.getPassword().set("")
    }
}
