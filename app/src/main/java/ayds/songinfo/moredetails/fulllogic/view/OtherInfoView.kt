package ayds.songinfo.moredetails.fulllogic.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import ayds.observer.Observable
import ayds.observer.Subject
import ayds.songinfo.R
import com.squareup.picasso.Picasso
import java.util.Locale
import ayds.songinfo.moredetails.fulllogic.model.OtherInfoModel
import ayds.songinfo.moredetails.fulllogic.model.OtherInfoModelInjector
import ayds.songinfo.moredetails.fulllogic.model.entities.Article
import ayds.songinfo.utils.UtilsInjector
import ayds.songinfo.utils.navigation.NavigationUtils

const val ARTIST_NAME_INTENT_EXTRA = "artistName"
private const val LASTFM_LOGO_URL = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"

interface OtherInfoView {
    val uiState: OtherInfoUiState
    val uiEventObservable: Observable<OtherInfoUiEvent>

    fun openExternalLink(url: String)
}

class OtherInfoViewActivity : Activity(), OtherInfoView {
    private val onActionSubject = Subject<OtherInfoUiEvent>()
    private lateinit var articleTextView: TextView
    private lateinit var openUrlButton: Button
    private lateinit var lastFMImageView: ImageView

    private lateinit var otherInfoModel: OtherInfoModel
    private val navigationUtils: NavigationUtils = UtilsInjector.navigationUtils

    override var uiState = OtherInfoUiState()
    override val uiEventObservable: Observable<OtherInfoUiEvent> = onActionSubject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        initModule()
        initViewProperties()
        initObservers()
        getArtistBiography()
    }

    override fun openExternalLink(url: String) {
        navigationUtils.openExternalUrl(this, url)
    }

    private fun initModule() {
        OtherInfoViewInjector.init(this)
        otherInfoModel = OtherInfoModelInjector.getOtherInfoModel()
    }

    private fun initViewProperties() {
        articleTextView = findViewById(R.id.textPane1)
        openUrlButton = findViewById(R.id.openUrlButton1)
        lastFMImageView = findViewById(R.id.imageView1)
    }

    private fun initObservers() {
        otherInfoModel.articleObservable
            .subscribe { value -> updateArtistBiography(value) }
    }

    private fun getArtistBiography() {
        loadArtistNameInUiState()
        onActionSubject.notify(OtherInfoUiEvent.LoadArtistBiography)
    }

    private fun getArtistName(): String =
        intent.getStringExtra(ARTIST_NAME_INTENT_EXTRA) ?: throw Exception("Missing artist name")

    private fun loadArtistNameInUiState() {
        uiState = uiState.copy(
            artistName = getArtistName()
        )
    }

    private fun updateArtistBiography(article: Article) {
        updateArticleUiState(article)
        updateUiArtistBiography()
    }

    private fun updateUiArtistBiography() {
        runOnUiThread {
            updateUiArtistBiographyLASTFMLogo()
            updateUiArtistBiographyArticle()
            updateUiArtistBiographyURLButton()
        }
    }

    private fun updateArticleUiState(article: Article) {
        when (article) {
            is Article.ArtistBiography -> updateArticleUiState(article)
            Article.ArtistBiographyEmpty -> updateNoResultsUiState()
        }
    }

    private fun updateArticleUiState(article: Article.ArtistBiography) {
        uiState = uiState.copy(
            artistName = article.name,
            artistUrl = article.articleUrl,
            biographyArtist = article.biography
        )
    }

    private fun updateNoResultsUiState() {
        uiState = uiState.copy(
            artistName = "",
            artistUrl = "",
            biographyArtist = ""
        )
    }

    private fun updateUiArtistBiographyLASTFMLogo() {
        Picasso.get().load(LASTFM_LOGO_URL).into(lastFMImageView)
    }

    private fun updateUiArtistBiographyArticle() {
        val text = uiState.biographyArtist.replace("\\n", "\n")
        articleTextView.text = Html.fromHtml(textToHtml(text, uiState.artistName), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun updateUiArtistBiographyURLButton() {
        openUrlButton.setOnClickListener {
            navigateToUrl(uiState.artistUrl)
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
}
