package com.masum.gpson

import android.app.Activity
import android.content.IntentSender
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener


object GpsOnUtils {
    //location on
    private lateinit var mSettingsClient: SettingsClient
    private var mLocationSettingsRequest: LocationSettingsRequest? = null
    private val REQUEST_CHECK_SETTINGS = 214
    private val REQUEST_ENABLE_GPS = 516

    fun openLocationDialog(activity: Activity) {
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY))
        builder.setAlwaysShow(true)
        mLocationSettingsRequest = builder.build()

        mSettingsClient = LocationServices.getSettingsClient(activity)

        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(object : OnSuccessListener<LocationSettingsResponse?> {
                    override fun onSuccess(locationSettingsResponse: LocationSettingsResponse?) {
                        Toast.makeText(activity, "Location enable to execute request.", Toast.LENGTH_LONG).show()
                    }
                })
                .addOnFailureListener(object : OnFailureListener {
                    override fun onFailure(e: Exception) {
                        val statusCode = (e as ApiException).statusCode
                        when (statusCode) {
                            LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                                val rae = e as ResolvableApiException
                                rae.startResolutionForResult(
                                        activity,
                                        REQUEST_CHECK_SETTINGS
                                )
                            } catch (sie: IntentSender.SendIntentException) {
                                Log.e("GPS", "Unable to execute request.")
                            }
                            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> Log.e(
                                    "GPS",
                                    "Location settings are inadequate, and cannot be fixed here. Fix in Settings."
                            )
                        }
                    }
                })
                .addOnCanceledListener { Log.e("GPS", "checkLocationSettings -> onCanceled") }
    }

}