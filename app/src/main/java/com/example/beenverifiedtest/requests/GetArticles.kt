package com.example.beenverifiedtest.requests

import android.os.AsyncTask
import com.example.beenverifiedtest.handlers.ArticlesHandler
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URL
import java.io.InputStreamReader

class GetArticles(private val handler: ArticlesHandler) : AsyncTask<String, Int, String>()  {

    override fun doInBackground(vararg p0: String?): String {

        val url = URL("https://www.beenverified.com/articles/index.android.json")
        val httpClient = url.openConnection() as HttpURLConnection
        try {
            if (httpClient.responseCode == HttpURLConnection.HTTP_OK) {
                try {
                    val stream = BufferedInputStream(httpClient.inputStream)
                    return readStream(inputStream = stream)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    httpClient.disconnect()
                }
            } else {
                return ("ERROR ${httpClient.responseCode}")
            }
        }catch (e: Exception){
            return "ERROR"
        }
        return "ERROR"
    }

    private fun readStream(inputStream: BufferedInputStream): String {
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        bufferedReader.forEachLine { stringBuilder.append(it) }
        return stringBuilder.toString()
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if(result!!.isNotEmpty()){
            if(result.startsWith("ERROR")){
                handler.onGetArticlesError()
            }else{
                handler.onGetArticlesOk(result)
            }
        }
    }
}