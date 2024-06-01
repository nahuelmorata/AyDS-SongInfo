package ayds.songinfo.moredetails.data

import ayds.artist.external.lastfm.data.LastFMArticle
import ayds.artist.external.lastfm.data.LastFMService
import ayds.songinfo.moredetails.data.article.local.ArticleLocalStorage
import ayds.songinfo.moredetails.domain.CardRepository
import ayds.songinfo.moredetails.domain.Cards

internal class ArticleRepositoryImpl(
    private val articleLocalStorage: ArticleLocalStorage,
    private val articleService: LastFMService,
    private val lastFMArticleToBiographyMapper: LastFMArticleToBiographyMapper
) : CardRepository {
    override fun getCard(artistName: String): Cards {
        val dbArtistBiography = articleLocalStorage.getArticleByArtistName(artistName)
        val card: Cards.Card?

        if (dbArtistBiography != null)
            card = dbArtistBiography.apply { markItAsLocal(dbArtistBiography) }
        else {
            val lastFMArticle = articleService.getArticle(artistName)
            card = if (lastFMArticle == LastFMArticle.LastFMArticleWithoutData) null
                else lastFMArticleToBiographyMapper.getBiographyFromArticle(lastFMArticle as LastFMArticle.LastFMArticleWithData)
            card?.let {
                if (it.isSavedSong())
                    articleLocalStorage.updateArticle(artistName, it)
                else
                    articleLocalStorage.insertArticle(artistName, it)
            }
        }

        return card ?: Cards.EmptyCard
    }

    private fun Cards.Card.isSavedSong() = articleLocalStorage.getArticleByArtistName(this.name) != null

    private fun markItAsLocal(card: Cards.Card) {
        card.isStoredLocally = true
    }
}