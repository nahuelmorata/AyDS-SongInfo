package ayds.songinfo.moredetails.fulllogic.model.repository.local.article.room

import ayds.songinfo.moredetails.fulllogic.model.entities.Article.ArtistBiography
import ayds.songinfo.moredetails.fulllogic.model.repository.local.article.ArticleLocalStorage

class ArticleLocalStorageRoomImpl(
    dataBase: ArticleDatabase,
) : ArticleLocalStorage {
    private val songDao: ArticleDao = dataBase.articleDao()

    override fun updateArticle(artistName: String, article: ArtistBiography) {
        songDao.updateArticle(article.toArticleEntity(artistName))
    }

    override fun insertArticle(artistName: String, article: ArtistBiography) {
        songDao.insertArticle(article.toArticleEntity(artistName))
    }

    override fun getArticleByArtistName(artistName: String): ArtistBiography? =
        songDao.getArticleByArtistName(artistName)?.toArticleBiography()

    private fun ArtistBiography.toArticleEntity(artistName: String) = ArticleEntity(
        artistName,
        this.biography,
        this.articleUrl
    )

    private fun ArticleEntity.toArticleBiography() = ArtistBiography(
        this.artistName,
        this.biography,
        this.articleUrl
    )
}