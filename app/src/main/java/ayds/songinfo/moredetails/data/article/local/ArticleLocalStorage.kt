package ayds.songinfo.moredetails.data.article.local

import ayds.songinfo.moredetails.domain.Article.ArtistBiography

interface ArticleLocalStorage {
    fun updateArticle(artistName: String, article: ArtistBiography)

    fun insertArticle(artistName: String, article: ArtistBiography)

    fun getArticleByArtistName(artistName: String): ArtistBiography?
}