package ayds.songinfo.moredetails.data.card.broker.proxy

import ayds.artist.external.lastfm.data.LASTFM_LOGO_URL
import ayds.artist.external.lastfm.data.LastFMArticle
import ayds.artist.external.lastfm.injector.LastFMInjector
import ayds.songinfo.moredetails.data.card.broker.Proxy
import ayds.songinfo.moredetails.domain.CardSource
import ayds.songinfo.moredetails.domain.Cards

internal class LastFmProxy : Proxy {
    override fun getCard(artistName: String): Cards =
        LastFMInjector.lastFMService.getArticle(artistName)?.toCard() ?: Cards.EmptyCard

    private fun LastFMArticle.toCard(): Cards =
        when (this) {
            LastFMArticle.LastFMArticleWithoutData -> Cards.EmptyCard
            is LastFMArticle.LastFMArticleWithData ->
                Cards.Card(name, biography, articleUrl, CardSource.LastFm, LASTFM_LOGO_URL)
        }
}