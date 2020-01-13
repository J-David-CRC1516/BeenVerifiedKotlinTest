package com.example.beenverifiedtest

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
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
        getData()
    }

    private fun getData(){
        articlesHolder?.removeAllViews()
        GetArticles(this).execute()
    }

    override fun onGetArticlesOk(result: String) {
        val obj = JSONObject(result)
        val array: JSONArray = obj.getJSONArray("articles")
        for (i in 0 until  array.length()) {
            createArticle(array[i])
        }
    }

    private fun createArticle(data: Any){
        val dataObj: JSONObject = data as JSONObject

        val tableRow = LinearLayout(context)

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
        val checkConnection = CheckForConnection()

        val errorHolder = LinearLayout(context)
        errorHolder.orientation = LinearLayout.VERTICAL

        val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.CENTER
        errorHolder.layoutParams = params

        val errorText = TextView(context)
        errorText.setPadding(10,10,10,0)
        errorText.gravity = Gravity.CENTER
        if(!checkConnection.checkForConnection(this.requireContext())){
            errorText.text = getText(R.string.error_getArticlesNoConnection)
        }else{
            errorText.text = getText(R.string.error_getArticles)
        }

        val tryAgainBtn = Button(context)
        tryAgainBtn.text = getText(R.string.try_again)
        tryAgainBtn.setOnClickListener{getData()}

        errorHolder.addView(errorText)
        errorHolder.addView(tryAgainBtn)

        articlesHolder?.addView(errorHolder)
    }
}