package ayds.songinfo.moredetails.data

import ayds.artist.external.lastfm.data.LastFMArticle
import ayds.songinfo.moredetails.domain.Article

interface LastFMArticleToBiographyMapper {
    fun getBiographyFromArticle(article: LastFMArticle.LastFMArticleWithData): Article.ArtistBiography
}

internal class LastFMArticleToBiographyMapperImpl : LastFMArticleToBiographyMapper {
    override fun getBiographyFromArticle(article: LastFMArticle.LastFMArticleWithData): Article.ArtistBiography =
        Article.ArtistBiography(article.name, article.biography, article.articleUrl)
}