package ayds.songinfo.moredetails.data.card.local.room

import ayds.artist.external.lastfm.data.LASTFM_LOGO_URL
import ayds.songinfo.moredetails.data.card.local.CardLocalStorage
import ayds.songinfo.moredetails.domain.CardSource
import ayds.songinfo.moredetails.domain.Cards

class CardLocalStorageRoomImpl(
    dataBase: CardDatabase,
) : CardLocalStorage {
    private val cardDao: CardDao = dataBase.cardDao()
    override fun getCards(name: String): List<Cards.Card> =
        cardDao.getCardsByName(name).map { cardEntity -> cardEntity.toCard() }

    override fun saveCards(name: String, cards: List<Cards.Card>) {
        cards.map {
            card -> cardDao.insertCard(card.toCardEntity(name))
        }
    }

    override fun updateCard(name: String, card: Cards.Card) {
        cardDao.updateCard(card.toCardEntity(name))
    }

    override fun insertCard(name: String, card: Cards.Card) {
        cardDao.insertCard(card.toCardEntity(name))
    }

    override fun getCardByName(name: String): Cards.Card? =
        cardDao.getCardByName(name)?.toCard()

    private fun Cards.Card.toCardEntity(name: String) =
        CardEntity(
            name,
            description,
            infoUrl,
            source,
            sourceLogoUrl
        )

    private fun CardEntity.toCard() = Cards.Card(
        name,
        description,
        infoUrl,
        source,
        sourceLogoUrl
    )
}