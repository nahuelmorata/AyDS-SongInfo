package ayds.songinfo.moredetails.data.card.broker.proxy

import ayds.artist.external.lastfm.data.LASTFM_LOGO_URL
import ayds.artist.external.lastfm.data.LastFMArticle
import ayds.artist.external.lastfm.injector.LastFMInjector
import ayds.songinfo.moredetails.data.card.broker.Proxy
import ayds.songinfo.moredetails.domain.CardSource
import ayds.songinfo.moredetails.domain.Cards

internal class LastFmProxy : Proxy {
    override fun getCard(artistName: String): Cards.Card =
        LastFMInjector.lastFMService.getArticle(artistName)?.toCard() ?: Cards.Card(isEmpty = true)

    private fun LastFMArticle.toCard(): Cards.Card =
        when (this) {
            LastFMArticle.LastFMArticleWithoutData -> Cards.Card(isEmpty = true)
            is LastFMArticle.LastFMArticleWithData ->
                Cards.Card(name, biography, articleUrl, CardSource.LastFm, LASTFM_LOGO_URL)
        }
}