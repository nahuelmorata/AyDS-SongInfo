package ayds.songinfo.moredetails.data.card.broker

import ayds.songinfo.moredetails.data.card.CardBroker
import ayds.songinfo.moredetails.domain.Cards

internal class CardBrokerImpl(
    private val proxies: List<Proxy>
): CardBroker {
    override fun getCards(artistName: String): List<Cards> {
        return proxies.map {
            it.getCard(artistName)
        }
    }
}