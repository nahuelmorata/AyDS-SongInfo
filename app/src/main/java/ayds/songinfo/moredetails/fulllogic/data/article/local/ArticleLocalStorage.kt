package ayds.songinfo.moredetails.fulllogic.data.article.local

import ayds.songinfo.moredetails.fulllogic.domain.Article.ArtistBiography

interface ArticleLocalStorage {
    fun updateArticle(artistName: String, article: ArtistBiography)

    fun insertArticle(artistName: String, article: ArtistBiography)

    fun getArticleByArtistName(artistName: String): ArtistBiography?
}