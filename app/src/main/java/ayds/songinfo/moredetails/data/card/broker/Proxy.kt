package ayds.songinfo.moredetails.data.card.broker

import ayds.songinfo.moredetails.domain.Cards

interface Proxy {
    fun getCard(artistName: String): Cards
}