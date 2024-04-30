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
    val name: String,
    val biography: String,
    val articleUrl: String
)

private const val ARTIST_NAME_INTENT_EXTRA = "artistName"
private const val ARTIST_KEY_JSON = "artist"
private const val ARTIST_BIO_KEY_JSON = "bio"
private const val ARTIST_CONTENT_KEY_JSON = "content"
private const val ARTIST_URL_KEY_JSON = "url"
private const val LASTFM_LOGO_URL = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
private const val LASTFM_BASE_URL = "https://ws.audioscrobbler.com/2.0/"
private const val ARTICLE_DB_NAME = "database-name-thename"

class OtherInfoWindow : Activity() {
    private lateinit var articleTextView: TextView
    private lateinit var openUrlButton: Button
    private lateinit var lastFMImageView: ImageView
    private lateinit var articleDatabase: ArticleDatabase
    private lateinit var lastFMAPI: LastFMAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        initViewProperties()
        initArticleDatabase()
        initLastFMAPI()
        getArtistInfoAsync()
    }

    private fun initViewProperties() {
        articleTextView = findViewById(R.id.textPane1)
        openUrlButton = findViewById(R.id.openUrlButton1)
        lastFMImageView = findViewById(R.id.imageView1)
    }

    private fun initLastFMAPI() {
        val retrofit = Retrofit.Builder()
            .baseUrl(LASTFM_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        lastFMAPI = retrofit.create(LastFMAPI::class.java)
    }

    private fun initArticleDatabase() {
        articleDatabase =
            databaseBuilder(this, ArticleDatabase::class.java, ARTICLE_DB_NAME).build()
    }

    private fun getArtistInfoAsync() {
        Thread {
            getArtistInfo()
        }.start()
    }

    private fun getArtistInfo() {
        val artistInfo = getArtistInfoFromRepository()
        updateUiArtistInfo(artistInfo)
    }

    private fun ArtistInfo.markItAsLocal() = copy(biography = "[*]$biography")


    private fun getArtistInfoFromRepository(): ArtistInfo {
        val artistName = getArtistName()
        val articleFromDB = getArticleFromDB(artistName)
        val artistInfo: ArtistInfo
        if (articleFromDB!= null) {
            artistInfo = articleFromDB.markItAsLocal()
        } else {
            artistInfo = getArtistInfoFromAPI(artistName)
            if (artistInfo.biography.isNotEmpty()) {
                insertArtistIntoDB(artistInfo)
            }
        }
        return artistInfo
    }

    private fun insertArtistIntoDB(artistInfo: ArtistInfo) {
        articleDatabase.ArticleDao().insertArticle(
            ArticleEntity(
                artistInfo.name, artistInfo.biography, artistInfo.articleUrl
            )
        )
    }

    private fun getArtistInfoFromAPI(artistName: String): ArtistInfo {
        var artistInfo = ArtistInfo(artistName, "", "")
        try {
            val callResponse = getSongFromService(artistName)
            artistInfo = getArtistBioFromExternalData(callResponse.body(), artistName)
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return artistInfo
    }

    private fun getArtistBioFromExternalData(
        serviceData: String?,
        artistName: String
    ): ArtistInfo {
        val gson = Gson()
        val jobj = gson.fromJson(serviceData, JsonObject::class.java)

        val artist = jobj[ARTIST_KEY_JSON].getAsJsonObject()
        val bio = artist[ARTIST_BIO_KEY_JSON].getAsJsonObject()
        val extract = bio[ARTIST_CONTENT_KEY_JSON]
        val url = artist[ARTIST_URL_KEY_JSON]
        val text = extract?.asString ?: "No Results"

        return ArtistInfo(artistName, text, url.asString)
    }

    private fun getSongFromService(artistName: String) =
        lastFMAPI.getArtistInfo(artistName).execute()

    private fun getArticleFromDB(artistName: String): ArtistInfo? {
        val artistInfoEntity = articleDatabase.ArticleDao().getArticleByArtistName(artistName)
        return artistInfoEntity?.let {
            ArtistInfo(artistName, artistInfoEntity.biography, artistInfoEntity.articleUrl)
        }
    }

    private fun getArtistName(): String =
        intent.getStringExtra(ARTIST_NAME_EXTRA) ?: throw Exception("Missing artist name")


    private fun updateUiArtistInfo(artistInfo: ArtistInfo) {
        runOnUiThread {
            updateUiArtistInfoLASTFMLogo()
            updateUiArtistInfoArticle(artistInfo)
            updateUiArtistInfoURLButton(artistInfo)
        }
    }

    private fun updateUiArtistInfoLASTFMLogo() {
        Picasso.get().load(LASTFM_LOGO_URL).into(lastFMImageView)
    }

    private fun updateUiArtistInfoArticle(artistInfo: ArtistInfo) {
        val text = artistInfo.biography.replace("\\n", "\n")
        articleTextView.text = Html.fromHtml(textToHtml(text, artistInfo.name), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun updateUiArtistInfoURLButton(artistInfo: ArtistInfo) {
        openUrlButton.setOnClickListener {
            navigateToUrl(artistInfo.articleUrl)
        }
    }

    private fun navigateToUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(url))
        startActivity(intent)
    }

    private fun textToHtml(text: String, term: String): String {
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

    companion object {
        const val ARTIST_NAME_EXTRA = ARTIST_NAME_INTENT_EXTRA
    }
}
