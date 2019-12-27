package com.example.beenverifiedtest

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.beenverifiedtest.handlers.ArticlesHandler
import com.example.beenverifiedtest.requests.GetArticles
import org.json.JSONArray
import org.json.JSONObject


class BlogFragment : Fragment(), ArticlesHandler {

    private var articlesHolder: TableLayout? = null
    //private var linearLayout: LinearLayout? = null //todo: add again the progress bar
    private var progressBar: ProgressBar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_blog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        articlesHolder = view.findViewById(R.id.articlesHolder)
        //linearLayout = view.findViewById(R.id.rootContainer)
        //showProgressBar()
        getData()
    }

    private fun getData(){
        GetArticles(this).execute()
    }

    override fun onGetArticlesOk(result: String) {
        val obj = JSONObject(result)//.get("articles")
        val array: JSONArray = obj.getJSONArray("articles")
        for (i in 0 until  array.length()) {
            createArticle(array[i])
        }

        //hideProgressBar()

    }

    private fun createArticle(data: Any){
        val dataObj: JSONObject = data as JSONObject

        val tableRow = TableRow(context)

        tableRow.setOnClickListener{
            val intent = Intent(this.context, WebActivity::class.java)
            intent.putExtra("link", dataObj.getString("link"))
            startActivity(intent)
        }

        tableRow.addView(ArticlesLayout(
            this.context,
            dataObj.getString("title"),
            dataObj.getString("description"),
            dataObj.getString("author"),
            dataObj.getString("article_date"),
            dataObj.getString("image")
        ))

        articlesHolder?.addView(tableRow)

    }

    override fun onGetArticlesError() {
        println("Error when retrieving data")
        //todo: manage error on not connection found and status errors
        //hideProgressBar()
    }

    private fun showProgressBar(){
        progressBar = ProgressBar(this.context)
        progressBar!!.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        //linearLayout?.addView(progressBar)

    }

    private fun hideProgressBar(){
        progressBar?.visibility = View.GONE
    }
}