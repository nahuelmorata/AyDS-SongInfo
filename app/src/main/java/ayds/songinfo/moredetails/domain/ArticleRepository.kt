package ayds.songinfo.moredetails.domain

import ayds.songinfo.moredetails.domain.Article.ArtistBiography

interface ArticleRepository {
    fun getArticleByArtistName(artistName: String): ArtistBiography
}