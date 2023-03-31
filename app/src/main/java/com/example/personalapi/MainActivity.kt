package com.example.personalapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    var marvelImageURL = ""
    var marvelName = ""
    var marvelDescription = ""
    var marvelDate = ""

    var goTImageURL = ""
    var goTName = ""
    var goTFamily = ""
    var goTTitle = ""

    var goTImageURL2 = ""
    var goTName2 = ""
    var goTFamily2 = ""
    var goTTitle2 = ""

    lateinit var editText: EditText
    lateinit var search_term: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.random_button)
        val imageView = findViewById<ImageView>(R.id.marvel_card)
        val textViewName = findViewById<TextView>(R.id.name)
        val textViewDescription = findViewById<TextView>(R.id.description)

        editText = findViewById(R.id.search_term)
        search_term = editText.text.toString()

        val search_button = findViewById<Button>(R.id.search_button)

//        getNextImage_Marvel(button, imageView, textViewName, textViewDescription)
        getNextImage_GoT(button, imageView, textViewName, textViewDescription)

        getSearchResult(search_term, search_button, imageView,textViewName, textViewDescription)
    }

    //        marvelapikey = https://gateway.marvel.com/v1/public/characters?ts=1&apikey=614ebc1fab96ac5ee97313b8242a1b24&hash=941c29c8f6855429d81b9698ecc05081
    //        omdb api key = https://www.omdbapi.com/?i=tt3896198&apikey=4c7902e0
    //        game of throne api key = https://thronesapi.com/api/v2/Characters

    private fun getMarvelImageURL() {
        val client = AsyncHttpClient()

        client["https://gateway.marvel.com/v1/public/characters?ts=1&apikey=614ebc1fab96ac5ee97313b8242a1b24&hash=941c29c8f6855429d81b9698ecc05081", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                Log.d("Marvel", "response successful$json")
                val jsonArray = json.jsonObject.getJSONObject("data").getJSONArray("results")
                val randomIndex = Random.nextInt(jsonArray.length())
                val character = jsonArray.getJSONObject(randomIndex)
                val thumbnail = character.getJSONObject("thumbnail")
                val path = thumbnail.getString("path")
                val extension = thumbnail.getString("extension")
                marvelImageURL = "$path.$extension".replace("http", "https")
                marvelName = character.getString("name")
                marvelDescription = character.getString("description")
                marvelDate = character.getString("modified")
                Log.d("marvelImageURL", "marvel image URL set")
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Marvel Error", errorResponse)
            }
        }]

    }

    private fun getGoTImageURL() {
        val client = AsyncHttpClient()

        client["https://thronesapi.com/api/v2/Characters", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                Log.d("Marvel", "response successful$json")
                val jsonArray = json.jsonArray
                val randomIndex = Random.nextInt(jsonArray.length())
                val character = jsonArray.getJSONObject(randomIndex)
                goTImageURL = character.getString("imageUrl")
                goTName = character.getString("fullName")
                goTFamily = character.getString("family")
                goTTitle = character.getString("title")
                Log.d("marvelImageURL", "marvel image URL set")
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Marvel Error", errorResponse)
            }
        }]

    }


    private fun getNextImage_Marvel(button: Button, imageView: ImageView, textViewName: TextView, textViewDescription: TextView) {
        button.setOnClickListener {
            getMarvelImageURL()

            Glide.with(this)
                .load(marvelImageURL)
                .fitCenter()
                .into(imageView)

            textViewName.text = "$marvelName"

            textViewDescription.text = "$marvelDate"
        }
    }

    private fun getNextImage_GoT(button: Button, imageView: ImageView, textViewName: TextView, textViewDescription: TextView) {
        button.setOnClickListener {
            getGoTImageURL()

            Glide.with(this)
                .load(goTImageURL)
                .fitCenter()
                .override(1800, 600)
                .into(imageView)

            textViewName.text = "$goTName"

            textViewDescription.text = "$goTFamily"
        }
    }

    private fun searchForResult(string: String) {
        val client = AsyncHttpClient()

        client["https://thronesapi.com/api/v2/Characters", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                Log.d("Marvel", "response successful$json")
                val jsonArray = json.jsonArray
                for (i in 0 until jsonArray.length()){
                    val goTcharacter = jsonArray.getJSONObject(i)
                    if (string == goTcharacter.getString("fullName")) {
                        goTImageURL2 = goTcharacter.getString("imageUrl")
                        goTName2 = goTcharacter.getString("fullName")
                        goTFamily2 = goTcharacter.getString("family")
                        goTTitle2 = goTcharacter.getString("title")
                        Log.d("marvelImageURL", "marvel image URL set")
                    }
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Marvel Error", errorResponse)
            }
        }]
    }

    private fun getSearchResult(search_term: String, button: Button, imageView: ImageView, textViewName: TextView, textViewDescription: TextView) {
        button.setOnClickListener{
            searchForResult(search_term)
        }

        Glide.with(this)
            .load(goTImageURL2)
            .fitCenter()
            .override(1800, 600)
            .into(imageView)

        textViewName.text = "$goTName2"

        textViewDescription.text = "$goTFamily2"
    }

}