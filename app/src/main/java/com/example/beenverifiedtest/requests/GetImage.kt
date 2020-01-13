package com.example.beenverifiedtest.requests

import android.graphics.BitmapFactory
import android.os.AsyncTask
import com.example.beenverifiedtest.handlers.ImageHandler
import java.net.URL
import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.net.HttpURLConnection

class GetImage(private val handler: ImageHandler) : AsyncTask<String, String, String>() {


    override fun doInBackground(vararg p0: String?): String {
        try{
            val url = URL(p0[0]?.replace(".com",".com/fit-in/500x/filters:autojpg()"))
            val connection = url.openConnection() as HttpURLConnection
            if(connection.responseCode == 200){
                val bmp = BitmapFactory.decodeStream(connection.inputStream)
                return bitMapToString(bmp)
            }
        }catch (e: Exception){
            return "null"
        }
        return "null"
    }

    private fun bitMapToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if(result!!.isNotEmpty()){
            if(result != "null"){
                handler.onGetImageOK(result)
            }
        }
    }
}