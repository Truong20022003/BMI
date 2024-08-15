package com.example.bmi.Utils

import android.content.Context
import android.net.ConnectivityManager

class CheckInternet(var context: Context) {
    fun haveNetworkConnection(): Boolean {
        var haveConnectedWifi = false
        var haveConnectedMobile = false
        val cm =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.allNetworkInfo
        for (ni in netInfo) {
            if (ni.typeName.equals("WIFI", ignoreCase = true))
                if (ni.isConnected) haveConnectedWifi = true
            if (ni.typeName.equals("MOBILE", ignoreCase = true))
                if (ni.isConnected) haveConnectedMobile = true
        }
        return haveConnectedWifi || haveConnectedMobile
    }

    companion object{
        fun haveNetworkConnection(context: Context): Boolean {
            var haveConnectedWifi = false
            var haveConnectedMobile = false
            val cm =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.allNetworkInfo
            for (ni in netInfo) {
                if (ni.typeName.equals("WIFI", ignoreCase = true))
                    if (ni.isConnected) haveConnectedWifi = true
                if (ni.typeName.equals("MOBILE", ignoreCase = true))
                    if (ni.isConnected) haveConnectedMobile = true
            }
            return haveConnectedWifi || haveConnectedMobile
        }

    }
}