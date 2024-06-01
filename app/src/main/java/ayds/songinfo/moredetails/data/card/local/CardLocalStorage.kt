package ayds.songinfo.moredetails.data.card.local

import ayds.songinfo.moredetails.domain.Cards

interface CardLocalStorage {
    fun updateCard(name: String, card: Cards.Card)

    fun insertCard(name: String, card: Cards.Card)

    fun getCardByName(name: String): Cards.Card?
}