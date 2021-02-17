/*
 * *
 *  * Created by Vinay.
 *  * Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme.ui.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.navigation.fragment.NavHostFragment
import com.ezeetech.salonme.R
import com.ezeetech.salonme.databinding.ActivityUserHomeBinding
import com.ezeetech.salonme.ellipsize
import com.ezeetech.salonme.handler.LocationPermissionHandler
import com.ezeetech.salonme.handler.PermissionHandler
import com.ezeetech.salonme.ui.action.BottomNavigationBehavior
import com.ezeetech.salonme.util.GpsUtils
import com.google.android.gms.location.*
import com.salonme.base.BaseActivity
import com.salonme.base.GPS_REQUEST
import com.salonme.base.Trace
import com.salonme.base.inflateActivity
import com.salonme.base.permission.Permission
import com.salonme.base.permission.PermissionCallback
import com.salonme.base.permission.PermissionUtils
import kotlinx.android.synthetic.main.activity_user_home.*
import kotlinx.coroutines.*
import java.util.*


class ActivityUserHome : BaseActivity<ActivityUserHomeBinding>() {
    private val locationInterval: Long = 10 * 1000 //10 seconds
    private val locationFastInterval: Long = 5 * 1000  //5 seconds
    val navHostFragment: NavHostFragment by lazy {
        user_home_fragment as NavHostFragment
    }
    private val navInflater by lazy {
        navHostFragment.navController.navInflater
    }
    private val navGraph by lazy { navInflater.inflate(R.navigation.nav_user_home) }

    private val mFusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    private val locationRequest by lazy {
        LocationRequest.create()
    }
    private val locationPermissionHandler: LocationPermissionHandler by lazy {
        LocationPermissionHandler(this, locationHandler)
    }

    private val locationHandler = object : PermissionHandler {
        override fun request(
            permission: Permission,
            permissionCallback: PermissionCallback
        ) {
            requestPermission(permission, permissionCallback)
        }

        override fun permissionGranted(permission: Permission) {
            getLocation()
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult?.let {
                for (location in it.locations) {
                    if (location != null) {
                        CoroutineScope(Dispatchers.Main).launch {
                            updateAddress(location.latitude, location.longitude)
                        }
                        return
                    }
                }
            }
        }
    }

    private val gpsStatusListener = object : GpsUtils.GpsStatusListener {
        override fun onStatusChanged(isGPSEnable: Boolean) {
            if (isGPSEnable)
                getLocation()
        }
    }
    private val gpsUtils by lazy {
        GpsUtils(this)
    }
    private var selected: Int = R.id.navigation_home
    override fun onBackPressed() {
        if (selected == R.id.navigation_home) {
            finish()
        } else {
            binding.bottomNavigation.selectedItemId = R.id.navigation_home
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        binding =
            inflateActivity(this, R.layout.activity_user_home) as ActivityUserHomeBinding

        val layoutParams = binding.bottomNavigation.layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.behavior = BottomNavigationBehavior()
        init()
    }

    override fun resume() {
        super.resume()
        if (!PermissionUtils.isGranted(this, Permission.FINE_LOCATION)) {
            requestForLocation()
        } else if (gpsUtils.isGPSEnabled()) {
            getLocation()
        } else {
            gpsUtils.turnGPSOn(gpsStatusListener)
        }
    }

    private fun init() {
        binding.tvLocation.text = getString(R.string.location_unknown)
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            Trace.i("Selected menu: " + it.itemId)
            if (it.itemId != selected) {
                selected = it.itemId
                navHostFragment.navController.navigate(it.itemId)
                return@setOnNavigationItemSelectedListener true
            }
            return@setOnNavigationItemSelectedListener false
        }
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_left)
        addLifecycleObserver(locationPermissionHandler)

        //setup Location request
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = locationInterval
        locationRequest.fastestInterval = locationFastInterval
    }

    private fun requestForLocation() {
        if (locationPermissionHandler.isPermissionGranted()) {
            getLocation()
        } else {
            locationPermissionHandler.requestPermission()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        mFusedLocationClient?.lastLocation?.addOnSuccessListener { location ->
            if (location != null) {
                CoroutineScope(Dispatchers.Main).launch {
                    updateAddress(location.latitude, location.longitude)
                }
                Trace.e("location: " + location.latitude + " long:" + location.longitude)
            } else {
                mFusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    null
                )
            }
        }?.addOnFailureListener { exception ->
            Trace.e("Error trying to get last GPS location")
            exception.printStackTrace()
        }
    }

    private fun updateAddress(latitude: Double, longitude: Double) {
        val addresses = getAddressFromLocation(latitude, longitude)
        if (addresses.isNotEmpty()) {
            val address: String? = addresses[0].getAddressLine(0)
            val city: String? = addresses[0].locality
            val state: String? = addresses[0].adminArea
            val country: String? = addresses[0].countryName
            val postalCode: String? = addresses[0].postalCode
            val knownName: String? = addresses[0].featureName
            Trace.i(
                "Current Address: address - $address, city - $city, state - $state," +
                        " country - $country, postalCode - $postalCode, known name - $knownName"
            )
            binding.tvLocation.text = knownName ?: "$city, $state."
            binding.tvAddress.text = address?.ellipsize(50)
        }
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    private fun getAddressFromLocation(latitude: Double, longitude: Double): List<Address> {
        val gcd = Geocoder(this@ActivityUserHome, Locale.getDefault())
        return gcd.getFromLocation(
            latitude,
            longitude,
            1
        )
    }

    override fun onSupportNavigateUp() = navHostFragment.navController.navigateUp()

    fun appBar(show: Boolean, back: Boolean) {
        binding.layoutAppbar.layoutParams.height =
            if (show) ViewGroup.LayoutParams.WRAP_CONTENT else 0
        supportActionBar?.setDisplayHomeAsUpEnabled(back)
        supportActionBar?.setDisplayShowHomeEnabled(back)
//        blockBack = !back
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GPS_REQUEST) {
                //Gps is enabled, get the location
                getLocation()
            }
        }
    }
}