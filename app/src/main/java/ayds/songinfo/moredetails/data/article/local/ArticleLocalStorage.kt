package ayds.songinfo.moredetails.data.article.local

import ayds.songinfo.moredetails.domain.Cards

interface ArticleLocalStorage {
    fun updateArticle(artistName: String, card: Cards.Card)

    fun insertArticle(artistName: String, card: Cards.Card)

    fun getArticleByArtistName(artistName: String): Cards.Card?
}