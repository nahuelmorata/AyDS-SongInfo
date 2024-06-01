package ayds.songinfo.moredetails.data.article.local.room

import ayds.artist.external.lastfm.data.LASTFM_LOGO_URL
import ayds.songinfo.moredetails.data.article.local.ArticleLocalStorage
import ayds.songinfo.moredetails.domain.CardSource
import ayds.songinfo.moredetails.domain.Cards

class ArticleLocalStorageRoomImpl(
    dataBase: ArticleDatabase,
) : ArticleLocalStorage {
    private val songDao: ArticleDao = dataBase.articleDao()

    override fun updateArticle(artistName: String, card: Cards.Card) {
        songDao.updateArticle(card.toArticleEntity(artistName))
    }

    override fun insertArticle(artistName: String, card: Cards.Card) {
        songDao.insertArticle(card.toArticleEntity(artistName))
    }

    override fun getArticleByArtistName(artistName: String): Cards.Card? =
        songDao.getArticleByArtistName(artistName)?.toArticleBiography()

    private fun Cards.Card.toArticleEntity(artistName: String) = ArticleEntity(
        artistName,
        this.description,
        this.infoUrl
    )

    private fun ArticleEntity.toArticleBiography() = Cards.Card(
        this.artistName,
        this.biography,
        this.articleUrl,
        CardSource.LastFm,
        LASTFM_LOGO_URL
    )
}