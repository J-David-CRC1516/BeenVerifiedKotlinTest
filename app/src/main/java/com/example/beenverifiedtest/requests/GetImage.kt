package com.example.beenverifiedtest.requests

import android.graphics.BitmapFactory
import android.os.AsyncTask
import com.example.beenverifiedtest.handlers.ImageHandler
import java.net.URL
import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream

class GetImage(private val handler: ImageHandler) : AsyncTask<String, String, String>() {


    override fun doInBackground(vararg p0: String?): String {

        //p0[0]?.replace(".com",".com/fit-in/30x/filters:autojpg()")//todo: Image Quality becomes too bad, check why and add all
        //"https://bv-content.beenverified.com/fit-in/60x/filters:autojpg()/2019-12-06-What-Is-Formjacking-CDN.jpg"
        val url = URL("https://bv-content.beenverified.com/fit-in/60x/filters:autojpg()/2019-12-06-What-Is-Formjacking-CDN.jpg")
        val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
        return bitMapToString(bmp)
    }

    private fun bitMapToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if(result!!.isNotEmpty()){
            if(result != "null"){
                handler.onGetImageOK(result)
            }else{
                handler.onGetImageError()
            }
        }
    }
}