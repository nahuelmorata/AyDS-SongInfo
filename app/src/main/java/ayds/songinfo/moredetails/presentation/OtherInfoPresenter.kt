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
        val cards = articleRepository.getCards(name)
        val uiState = cards.toUiState()
        otherInfoObservable.notify(uiState)
    }

    private fun List<Cards>.toUiState() = OtherInfoUiState(
        cards = map {
            card -> when (card) {
                is Cards.Card -> OtherInfoCardUiState(
                    card.name,
                    otherInfoDescriptionHelper.getDescription(card),
                    card.infoUrl,
                    card.sourceLogoUrl ?: "",
                )
                is Cards.EmptyCard -> OtherInfoCardUiState()
            }
        }
    )
}