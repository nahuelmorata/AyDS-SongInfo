package ayds.songinfo.moredetails.data

import ayds.artist.external.lastfm.data.LASTFM_LOGO_URL
import ayds.artist.external.lastfm.data.LastFMArticle
import ayds.songinfo.moredetails.domain.CardSource
import ayds.songinfo.moredetails.domain.Cards

interface LastFMArticleToBiographyMapper {
    fun getBiographyFromArticle(article: LastFMArticle.LastFMArticleWithData): Cards.Card
}

internal class LastFMArticleToBiographyMapperImpl : LastFMArticleToBiographyMapper {
    override fun getBiographyFromArticle(article: LastFMArticle.LastFMArticleWithData): Cards.Card =
        Cards.Card(article.name, article.biography, article.articleUrl, CardSource.LastFm, LASTFM_LOGO_URL)
}