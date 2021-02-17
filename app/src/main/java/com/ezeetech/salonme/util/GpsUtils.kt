/*
 *  Created by Vinay on 2-2-2021 for EzeeTech.
 *  Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme.util

import android.app.Activity
import android.content.Context
import android.content.IntentSender.SendIntentException
import android.location.LocationManager
import android.widget.Toast
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.salonme.base.GPS_REQUEST
import com.salonme.base.Trace


class GpsUtils(private val context: Context) {
    private val mSettingsClient by lazy {
        LocationServices.getSettingsClient(context)
    }
    private val locationManager by lazy {
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
    private val locationRequest by lazy {
        LocationRequest.create()
    }
    private val mLocationSettingsRequest: LocationSettingsRequest

    // method for turn on GPS
    fun turnGPSOn(gpsStatusListener: GpsStatusListener?) {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            gpsStatusListener?.onStatusChanged(true)
        } else {
            mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(
                    context as Activity
                ) { //  GPS is already enable, callback GPS status through listener
                    gpsStatusListener?.onStatusChanged(true)
                }
                .addOnFailureListener(context as Activity
                ) { e ->
                    when ((e as ApiException).statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                            // Show the dialog by calling startResolutionForResult(), and check the
                            // result in onActivityResult().
                            val rae: ResolvableApiException = e as ResolvableApiException
                            rae.startResolutionForResult(
                                context as Activity,
                                GPS_REQUEST
                            )
                        } catch (sie: SendIntentException) {
                            Trace.e("PendingIntent unable to execute request.")
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            val errorMessage =
                                "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings."
                            Trace.e(errorMessage)
                            Toast.makeText(
                                context as Activity,
                                errorMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
        }
    }

    fun isGPSEnabled() : Boolean{
       return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
    interface GpsStatusListener {
        fun onStatusChanged(isGPSEnable: Boolean)
    }

    init {
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10 * 1000.toLong()
        locationRequest.fastestInterval = 2 * 1000.toLong()
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        mLocationSettingsRequest = builder.build()

        //**************************
        builder.setAlwaysShow(true) //this is the key ingredient
        //**************************
    }
}