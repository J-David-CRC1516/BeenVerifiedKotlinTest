package com.example.beenverifiedtest

import android.content.Context
import android.net.ConnectivityManager

class CheckForConnection {

    fun checkForConnection(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null
    }
}