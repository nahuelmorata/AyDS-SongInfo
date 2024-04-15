package ayds.songinfo.moredetails.fulllogic

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.room.Room.databaseBuilder
import ayds.songinfo.R
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.Locale

private data class ArtistInfo(
    val extract: String?,
    val url: String
)

private const val ARTIST_NAME_INTENT_EXTRA = "artistName"

private const val ARTIST_KEY_JSON = "artist"

private const val ARTIST_BIO_KEY_JSON = "bio"

private const val ARTIST_CONTENT_KEY_JSON = "content"

private const val ARTIST_URL_KEY_JSON = "url"

class OtherInfoWindow : Activity() {
    private var textPane1: TextView? = null
    private var openUrlButton: Button? = null
    private var dataBase: ArticleDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        textPane1 = findViewById(R.id.textPane1)
        openUrlButton = findViewById(R.id.openUrlButton1)
        intent.getStringExtra(ARTIST_NAME_INTENT_EXTRA)?.let { open(it) }
    }

    private fun loadArtistInfo(artistName: String) {
        Log.e("TAG", "artistName $artistName")
        Thread {
            val article = dataBase!!.ArticleDao().getArticleByArtistName(artistName)
            val text = generateArtistInfo(article, artistName)
            val imageUrl =
                "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
            Log.e("TAG", "Get Image from $imageUrl")
            runOnUiThread {
                Picasso.get().load(imageUrl).into(findViewById<ImageView>(R.id.imageView1))
                textPane1?.text = Html.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
            }
        }.start()
    }

    private fun generateArtistInfo(
        article: ArticleEntity?,
        artistName: String
    ): String {
        var text = ""
        var urlString = article?.articleUrl
        if (article != null) {
            text = "[*]" + article.biography
        } else {
            val artistInfoJSON = getArtistInfoFromApi(artistName) ?: return text
            val artistInfo = getArtistInfoFromJSON(artistInfoJSON)
            if (artistInfo.extract == null) {
                text = "No Results"
            } else {
                text = artistInfo.extract.replace("\\n", "\n")
                text = textToHtml(text, artistName)

                Thread {
                    dataBase!!.ArticleDao()
                        .insertArticle(ArticleEntity(artistName, text, artistInfo.url))
                }
                    .start()
            }
            urlString = artistInfo.url
        }
        openUrlButton?.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(urlString))
            startActivity(intent)
        }
        return text
    }

    private fun createLastFMAPI(): LastFMAPI {
        // create
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://ws.audioscrobbler.com/2.0/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        return retrofit.create(LastFMAPI::class.java)
    }

    private fun open(artist: String) {
        dataBase =
            databaseBuilder(this, ArticleDatabase::class.java, "database-name-thename").build()
        Thread {
            dataBase!!.ArticleDao().insertArticle(ArticleEntity("test", "sarasa", ""))
            Log.e("TAG", "" + dataBase!!.ArticleDao().getArticleByArtistName("test"))
            Log.e("TAG", "" + dataBase!!.ArticleDao().getArticleByArtistName("nada"))
        }.start()
        loadArtistInfo(artist)
    }

    private fun getArtistInfoFromJSON(artistInfoJSON: String): ArtistInfo {
        val gson = Gson()
        val jobj = gson.fromJson(artistInfoJSON, JsonObject::class.java)
        val artist = jobj[ARTIST_KEY_JSON].getAsJsonObject()
        val bio = artist[ARTIST_BIO_KEY_JSON].getAsJsonObject()
        val extract = bio[ARTIST_CONTENT_KEY_JSON]
        val url = artist[ARTIST_URL_KEY_JSON]
        return ArtistInfo(extract.asString, url.asString)
    }

    private fun getArtistInfoFromApi(artistName: String): String? {
        val lastFMAPI: LastFMAPI = createLastFMAPI()
        try {
            val callResponse = lastFMAPI.getArtistInfo(artistName).execute()
            Log.e("TAG", "JSON " + callResponse.body())
            return callResponse.body()
        } catch (e1: IOException) {
            Log.e("TAG", "Error $e1")
            e1.printStackTrace()
        }
        return null
    }

    companion object {
        const val ARTIST_NAME_EXTRA = ARTIST_NAME_INTENT_EXTRA
        fun textToHtml(text: String, term: String): String {
            val builder = StringBuilder()
            builder.append("<html><div width=400>")
            builder.append("<font face=\"arial\">")
            val textWithBold = text
                .replace("'", " ")
                .replace("\n", "<br>")
                .replace(
                    "(?i)$term".toRegex(),
                    "<b>" + term.uppercase(Locale.getDefault()) + "</b>"
                )
            builder.append(textWithBold)
            builder.append("</font></div></html>")
            return builder.toString()
        }
    }
}
