package com.example.beenverifiedtest

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.beenverifiedtest.handlers.ImageHandler
import com.example.beenverifiedtest.requests.GetImage

class ArticlesLayout(
    context: Context?,
    titleData: String,
    descriptionData: String,
    authorData: String,
    dateData: String,
    imgData: String
) : LinearLayout(context), ImageHandler {

    private lateinit var title: TextView
    private lateinit var description: TextView
    private lateinit var author: TextView
    private lateinit var date: TextView
    private lateinit var img: ImageView

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.layout_article, this, true)
        orientation = VERTICAL
        initViews()
        fillArticle(titleData, descriptionData, authorData, dateData, imgData)
    }

    private fun initViews() {
        title = findViewById(R.id.title)
        description = findViewById(R.id.description)
        author = findViewById(R.id.author)
        date = findViewById(R.id.date)
        img = findViewById(R.id.img)
    }

    private fun fillArticle(titleText: String, descriptionText: String, authorText: String, dateText: String, imgUrl: String){
        title.text = titleText
        description.text = descriptionText
        author.text = authorText
        date.text = dateText

        getImage(imgUrl)
    }

    private fun getImage(img: String){
        if(img.isNotEmpty()){
            GetImage(this).execute(img)
        }
    }

    override fun onGetImageOK(result: String) {
        img.setImageBitmap(toBitMap(result))
    }

    private fun toBitMap(encodedString: String): Bitmap? {
        try {
            val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        } catch (e: Exception) {
            e.message
            return null
        }
    }

}