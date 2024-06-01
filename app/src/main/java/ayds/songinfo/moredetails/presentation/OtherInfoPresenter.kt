package ayds.songinfo.moredetails.presentation

import ayds.observer.Observable
import ayds.observer.Subject
import ayds.songinfo.moredetails.domain.CardRepository
import ayds.songinfo.moredetails.domain.Cards

interface OtherInfoPresenter {
    val otherInfoObservable: Observable<OtherInfoUiState>

    fun getCard(name: String)
}

internal class OtherInfoPresenterImpl(
    private val articleRepository: CardRepository,
    private val otherInfoDescriptionHelper: OtherInfoDescriptionHelper
) : OtherInfoPresenter {
    override val otherInfoObservable = Subject<OtherInfoUiState>()

    override fun getCard(name: String) {
        val card = articleRepository.getCard(name)
        val uiState = if (card == Cards.EmptyCard) OtherInfoUiState()
            else (card as Cards.Card).toUiState()
        otherInfoObservable.notify(uiState)
    }

    private fun Cards.Card.toUiState() = OtherInfoUiState(
        this.name,
        otherInfoDescriptionHelper.getDescription(this),
        infoUrl,
        sourceLogoUrl ?: "",
    )
}