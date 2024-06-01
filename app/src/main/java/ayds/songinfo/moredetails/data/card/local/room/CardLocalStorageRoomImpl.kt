package ayds.songinfo.moredetails.data.card.local.room

import ayds.artist.external.lastfm.data.LASTFM_LOGO_URL
import ayds.songinfo.moredetails.data.card.local.CardLocalStorage
import ayds.songinfo.moredetails.domain.CardSource
import ayds.songinfo.moredetails.domain.Cards

class CardLocalStorageRoomImpl(
    dataBase: CardDatabase,
) : CardLocalStorage {
    private val songDao: CardDao = dataBase.cardDao()

    override fun updateCard(name: String, card: Cards.Card) {
        songDao.updateCard(card.toCardEntity(name))
    }

    override fun insertCard(name: String, card: Cards.Card) {
        songDao.insertCard(card.toCardEntity(name))
    }

    override fun getCardByName(name: String): Cards.Card? =
        songDao.getCardByName(name)?.toCard()

    private fun Cards.Card.toCardEntity(name: String) = CardEntity(
        name,
        this.description,
        this.infoUrl,
        this.source,
        sourceLogoUrl
    )

    private fun CardEntity.toCard() = Cards.Card(
        this.name,
        this.description,
        this.infoUrl,
        CardSource.LastFm,
        LASTFM_LOGO_URL
    )
}