package ayds.songinfo.moredetails.data

import ayds.artist.external.lastfm.data.LastFMArticle
import ayds.artist.external.lastfm.data.LastFMService
import ayds.songinfo.moredetails.data.card.CardBroker
import ayds.songinfo.moredetails.data.card.local.CardLocalStorage
import ayds.songinfo.moredetails.domain.CardRepository
import ayds.songinfo.moredetails.domain.Cards

internal class CardRepositoryImpl(
    private val cardLocalStorage: CardLocalStorage,
    private val cardService: LastFMService,
    private val lastFMArticleToCardMapper: LastFMArticleToCardMapper,
    private val cardBroker: CardBroker
) : CardRepository {
    override fun getCards(artistName: String): List<Cards> {
        return cardBroker.getCards(artistName)
    }

    private fun Cards.Card.isSavedSong() = cardLocalStorage.getCardByName(this.name) != null

    private fun markItAsLocal(card: Cards.Card) {
        card.isStoredLocally = true
    }
}