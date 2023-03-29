package com.example.personalapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.random_button)
        val imageView = findViewById<ImageView>(R.id.marvel_card)
        val textViewName = findViewById<TextView>(R.id.name)
        val textViewDescription = findViewById<TextView>(R.id.description)

        getNextImage(button, imageView, textViewName, textViewDescription)


    }

    private fun getMarvelImageURL() {
        val client = AsyncHttpClient()

//        marvelapikey = https://gateway.marvel.com/v1/public/characters?ts=1&apikey=614ebc1fab96ac5ee97313b8242a1b24&hash=941c29c8f6855429d81b9698ecc05081
//        omdb api key = https://www.omdbapi.com/?i=tt3896198&apikey=4c7902e0

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


    private fun getNextImage(button: Button, imageView: ImageView, textViewName: TextView, textViewDescription: TextView) {
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

}