package ayds.songinfo.moredetails.fulllogic.domain

import ayds.songinfo.moredetails.fulllogic.domain.Article.ArtistBiography

interface ArticleRepository {
    fun getArticleByArtistName(artistName: String): ArtistBiography
}