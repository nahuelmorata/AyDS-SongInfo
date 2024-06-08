package ayds.songinfo.moredetails.data.card.broker.proxy

import ayds.artist.external.wikipedia.data.WikipediaArticle
import ayds.artist.external.wikipedia.injector.WikipediaInjector
import ayds.songinfo.moredetails.data.card.broker.Proxy
import ayds.songinfo.moredetails.domain.CardSource
import ayds.songinfo.moredetails.domain.Cards

internal class WikipediaProxy : Proxy {
    override fun getCard(artistName: String): Cards.Card =
        WikipediaInjector.wikipediaTrackService.getInfo(artistName)?.toCard(artistName) ?:
            Cards.Card(isEmpty = true)


    private fun WikipediaArticle.toCard(artistName: String): Cards.Card =
        Cards.Card(artistName, description, wikipediaURL, CardSource.Wikipedia, wikipediaLogoURL)
}