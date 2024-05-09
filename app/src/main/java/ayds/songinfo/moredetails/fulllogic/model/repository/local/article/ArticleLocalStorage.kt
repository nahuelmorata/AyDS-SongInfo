package ayds.songinfo.moredetails.fulllogic.model.repository.local.article

import ayds.songinfo.moredetails.fulllogic.model.entities.Article.ArtistBiography

interface ArticleLocalStorage {
    fun updateArticle(artistName: String, article: ArtistBiography)

    fun insertArticle(artistName: String, article: ArtistBiography)

    fun getArticleByArtistName(artistName: String): ArtistBiography?
}