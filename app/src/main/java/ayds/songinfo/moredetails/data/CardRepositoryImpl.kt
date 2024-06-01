package ayds.songinfo.moredetails.data

import ayds.artist.external.lastfm.data.LastFMArticle
import ayds.artist.external.lastfm.data.LastFMService
import ayds.songinfo.moredetails.data.card.local.CardLocalStorage
import ayds.songinfo.moredetails.domain.CardRepository
import ayds.songinfo.moredetails.domain.Cards

internal class CardRepositoryImpl(
    private val cardLocalStorage: CardLocalStorage,
    private val cardService: LastFMService,
    private val lastFMArticleToCardMapper: LastFMArticleToCardMapper
) : CardRepository {
    override fun getCard(artistName: String): Cards {
        val dbArtistBiography = cardLocalStorage.getCardByName(artistName)
        val card: Cards.Card?

        if (dbArtistBiography != null)
            card = dbArtistBiography.apply { markItAsLocal(dbArtistBiography) }
        else {
            val lastFMArticle = cardService.getArticle(artistName)
            card = if (lastFMArticle == LastFMArticle.LastFMArticleWithoutData) null
                else lastFMArticleToCardMapper.getCardFromArticle(lastFMArticle as LastFMArticle.LastFMArticleWithData)
            card?.let {
                if (it.isSavedSong())
                    cardLocalStorage.updateCard(artistName, it)
                else
                    cardLocalStorage.insertCard(artistName, it)
            }
        }

        return card ?: Cards.EmptyCard
    }

    private fun Cards.Card.isSavedSong() = cardLocalStorage.getCardByName(this.name) != null

    private fun markItAsLocal(card: Cards.Card) {
        card.isStoredLocally = true
    }
}