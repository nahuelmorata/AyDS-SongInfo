package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.card.CardBroker
import ayds.songinfo.moredetails.data.card.local.CardLocalStorage
import ayds.songinfo.moredetails.domain.CardRepository
import ayds.songinfo.moredetails.domain.Cards

internal class CardRepositoryImpl(
    private val cardLocalStorage: CardLocalStorage,
    private val cardBroker: CardBroker
) : CardRepository {
    override fun getCards(artistName: String): List<Cards.Card> {
        val cardsLocal = cardLocalStorage.getCards(artistName)
        if (cardsLocal.isNotEmpty()) {
            cardsLocal.forEach { card -> card.markItAsLocal() }
            return cardsLocal
        }
        return cardBroker.getCards(artistName)
            .filter { !it.isEmpty }
            .apply {
                cardLocalStorage.saveCards(artistName, this)
            }
    }

    private fun Cards.Card.markItAsLocal() {
        this.isStoredLocally = true
    }
}