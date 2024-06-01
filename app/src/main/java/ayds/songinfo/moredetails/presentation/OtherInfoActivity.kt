package ayds.songinfo.moredetails.presentation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import ayds.songinfo.R
import com.squareup.picasso.Picasso
import ayds.songinfo.moredetails.injector.OtherInfoInjector

const val ARTIST_NAME_INTENT_EXTRA = "artistName"

class OtherInfoViewActivity : Activity() {
    private lateinit var articleTextView: TextView
    private lateinit var openUrlButton: Button
    private lateinit var lastFMImageView: ImageView

    private lateinit var presenter: OtherInfoPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        initViewProperties()
        initPresenter()
        initObservers()
        getArtistBiographyAsync()
    }

    private fun initPresenter() {
        OtherInfoInjector.initGraph(this)
        presenter = OtherInfoInjector.presenter
    }

    private fun initViewProperties() {
        articleTextView = findViewById(R.id.textPane1)
        openUrlButton = findViewById(R.id.openUrlButton1)
        lastFMImageView = findViewById(R.id.imageView1)
    }

    private fun initObservers() {
        presenter.otherInfoObservable
            .subscribe { value -> updateUiArtistBiography(value) }
    }

    private fun getArtistBiographyAsync() {
        Thread {
            presenter.getArtistBiography(getArtistName())
        }.start()
    }

    private fun getArtistName(): String =
        intent.getStringExtra(ARTIST_NAME_INTENT_EXTRA) ?: throw Exception("Missing artist name")


    private fun updateUiArtistBiography(otherInfoUiState: OtherInfoUiState) {
        runOnUiThread {
            updateUiArtistBiographyLogo(otherInfoUiState.logoUrl)
            updateUiArtistBiographyArticle(otherInfoUiState.biographyArtistHtml)
            updateUiArtistBiographyURLButton(otherInfoUiState.artistUrl)
        }
    }

    private fun updateUiArtistBiographyLogo(logoUrl: String) {
        Picasso.get().load(logoUrl).into(lastFMImageView)
    }

    private fun updateUiArtistBiographyArticle(biographyArtist: String) {
        articleTextView.text = Html.fromHtml(biographyArtist, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun updateUiArtistBiographyURLButton(artistUrl: String) {
        openUrlButton.setOnClickListener {
            navigateToUrl(artistUrl)
        }
    }

    private fun navigateToUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(url))
        startActivity(intent)
    }
}
