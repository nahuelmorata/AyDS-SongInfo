package ayds.songinfo.moredetails.data.card.broker.proxy

import ayds.artist.external.newyorktimes.data.NYT_LOGO_URL
import ayds.artist.external.newyorktimes.data.NYTimesArticle
import ayds.artist.external.newyorktimes.injector.NYTimesInjector
import ayds.songinfo.moredetails.data.card.broker.Proxy
import ayds.songinfo.moredetails.domain.CardSource
import ayds.songinfo.moredetails.domain.Cards

internal class NYTimesProxy : Proxy {
    override fun getCard(artistName: String): Cards.Card =
        NYTimesInjector.nyTimesService.getArtistInfo(artistName).toCard()

    private fun NYTimesArticle.toCard(): Cards.Card =
        when (this) {
            NYTimesArticle.EmptyArtistDataExternal -> Cards.Card(isEmpty = true)
            is NYTimesArticle.NYTimesArticleWithData ->
                Cards.Card(name ?: "", info ?: "", url, CardSource.NYTimes, NYT_LOGO_URL)
        }
}