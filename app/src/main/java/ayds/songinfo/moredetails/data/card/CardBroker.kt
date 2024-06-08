package ayds.songinfo.moredetails.data.card

import ayds.songinfo.moredetails.domain.Cards

internal interface CardBroker {
    fun getCards(artistName: String): List<Cards.Card>
}