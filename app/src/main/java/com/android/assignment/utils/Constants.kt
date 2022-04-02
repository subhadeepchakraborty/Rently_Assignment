package com.android.assignment.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.ContextCompat.getSystemService

class Constants {
    companion object {
        val KOCHI_LAT: String = "9.931233"
        val KOCHI_LONG: String = "76.267303"

        val COIMBATORE_LAT: String = "11.004556"
        val COIMBATORE_LONG: String = "76.961632"

        val MADURAI_LAT: String = "9.939093"
        val MADURAI_LONG: String = "78.121719"

        val MUNNAR_LAT: String = "10.089167"
        val MUNNAR_LONG: String = "77.059723"


        fun checkForIntenetConnection(activity: Activity):Boolean {
            return try {
                val connectivityManager: ConnectivityManager =
                    activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

                connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo()!!
                    .isConnected();
            } catch (exception: Exception) {
                false
            }
        }
    }
}