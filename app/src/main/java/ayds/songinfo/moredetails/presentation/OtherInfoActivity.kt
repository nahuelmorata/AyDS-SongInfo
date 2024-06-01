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
    private lateinit var descriptionTextView: TextView
    private lateinit var openUrlButton: Button
    private lateinit var lastFMImageView: ImageView

    private lateinit var presenter: OtherInfoPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        initViewProperties()
        initPresenter()
        initObservers()
        getCardAsync()
    }

    private fun initPresenter() {
        OtherInfoInjector.initGraph(this)
        presenter = OtherInfoInjector.presenter
    }

    private fun initViewProperties() {
        descriptionTextView = findViewById(R.id.textPane1)
        openUrlButton = findViewById(R.id.openUrlButton1)
        lastFMImageView = findViewById(R.id.imageView1)
    }

    private fun initObservers() {
        presenter.otherInfoObservable
            .subscribe { value -> updateUiCard(value) }
    }

    private fun getCardAsync() {
        Thread {
            presenter.getCard(getArtistName())
        }.start()
    }

    private fun getArtistName(): String =
        intent.getStringExtra(ARTIST_NAME_INTENT_EXTRA) ?: throw Exception("Missing artist name")


    private fun updateUiCard(otherInfoUiState: OtherInfoUiState) {
        runOnUiThread {
            updateUiCardLogo(otherInfoUiState.logoUrl)
            updateUiCardDescription(otherInfoUiState.descriptionHtml)
            updateUiCardInfoUrlButton(otherInfoUiState.infoUrl)
        }
    }

    private fun updateUiCardLogo(logoUrl: String) {
        Picasso.get().load(logoUrl).into(lastFMImageView)
    }

    private fun updateUiCardDescription(description: String) {
        descriptionTextView.text = Html.fromHtml(description, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun updateUiCardInfoUrlButton(infoUrl: String) {
        openUrlButton.setOnClickListener {
            navigateToUrl(infoUrl)
        }
    }

    private fun navigateToUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(url))
        startActivity(intent)
    }
}
