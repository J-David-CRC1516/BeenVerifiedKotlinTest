package com.example.beenverifiedtest.requests

import android.os.AsyncTask
import com.example.beenverifiedtest.handlers.articlesHandler
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URL
import java.io.InputStreamReader

class GetArticles(private val handler: articlesHandler) : AsyncTask<String, Int, String>()  {

    override fun doInBackground(vararg p0: String?): String {

        val url = URL("https://www.beenverified.com/articles/index.android.json")
        val httpClient = url.openConnection() as HttpURLConnection
        if (httpClient.responseCode == HttpURLConnection.HTTP_OK) {
            try {
                val stream = BufferedInputStream(httpClient.inputStream)
                val data: String = readStream(inputStream = stream)
                return data
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                httpClient.disconnect()
            }
        } else {
            println("ERROR ${httpClient.responseCode}")
        }
        return "null"
    }

    fun readStream(inputStream: BufferedInputStream): String {
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        bufferedReader.forEachLine { stringBuilder.append(it) }
        return stringBuilder.toString()
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if(result!!.isNotEmpty()){
            if(result != "null"){
                handler.onGetArticlesOk(result)
            }else{
                handler.onGetArticlesError()
            }

        }
    }
}