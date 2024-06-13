package ayds.songinfo.moredetails.data.card.local

import ayds.songinfo.moredetails.domain.Cards

interface CardLocalStorage {
    fun getCards(name: String): List<Cards.Card>

    fun saveCards(name: String, cards: List<Cards.Card>)

    fun updateCard(name: String, card: Cards.Card)

    fun insertCard(name: String, card: Cards.Card)

    fun getCardByName(name: String): Cards.Card?
}