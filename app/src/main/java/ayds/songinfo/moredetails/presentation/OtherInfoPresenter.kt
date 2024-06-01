package ayds.songinfo.moredetails.presentation

import ayds.observer.Observable
import ayds.observer.Subject
import ayds.songinfo.moredetails.domain.CardRepository
import ayds.songinfo.moredetails.domain.Cards

interface OtherInfoPresenter {
    val otherInfoObservable: Observable<OtherInfoUiState>

    fun getArtistBiography(artistName: String)
}

internal class OtherInfoPresenterImpl(
    private val articleRepository: CardRepository,
    private val otherInfoDescriptionHelper: OtherInfoDescriptionHelper
) : OtherInfoPresenter {
    override val otherInfoObservable = Subject<OtherInfoUiState>()

    override fun getArtistBiography(artistName: String) {
        val artistBiography = articleRepository.getCard(artistName)
        val uiState = if (artistBiography == Cards.EmptyCard) OtherInfoUiState()
            else (artistBiography as Cards.Card).toUiState()
        otherInfoObservable.notify(uiState)
    }

    private fun Cards.Card.toUiState() = OtherInfoUiState(
        this.name,
        otherInfoDescriptionHelper.getDescription(this),
        infoUrl,
        sourceLogoUrl ?: ""
    )
}