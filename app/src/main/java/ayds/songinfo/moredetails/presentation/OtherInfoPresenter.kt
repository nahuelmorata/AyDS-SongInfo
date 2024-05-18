package ayds.songinfo.moredetails.presentation

import ayds.observer.Observable
import ayds.observer.Subject
import ayds.songinfo.moredetails.domain.Article.ArtistBiography
import ayds.songinfo.moredetails.domain.ArticleRepository

interface OtherInfoPresenter {
    val otherInfoObservable: Observable<OtherInfoUiState>

    fun getArtistBiography(artistName: String)
}

internal class OtherInfoPresenterImpl(
    private val articleRepository: ArticleRepository
) : OtherInfoPresenter {
    override val otherInfoObservable = Subject<OtherInfoUiState>()

    override fun getArtistBiography(artistName: String) {
        val artistBiography = articleRepository.getArticleByArtistName(artistName)
        val uiState = artistBiography.toUiState()
        otherInfoObservable.notify(uiState)
    }

    private fun ArtistBiography.toUiState() = OtherInfoUiState(
        this.name,
        this.biography,
        articleUrl
    )
}