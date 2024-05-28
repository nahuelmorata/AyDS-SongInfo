package ayds.songinfo.moredetails.data

import ayds.artist.external.lastfm.data.LastFMArticle
import ayds.artist.external.lastfm.data.LastFMService
import ayds.songinfo.moredetails.data.article.local.ArticleLocalStorage
import ayds.songinfo.moredetails.domain.Article
import ayds.songinfo.moredetails.domain.Article.ArtistBiography
import ayds.songinfo.moredetails.domain.ArticleRepository

internal class ArticleRepositoryImpl(
    private val articleLocalStorage: ArticleLocalStorage,
    private val articleService: LastFMService,
    private val lastFMArticleToBiographyMapper: LastFMArticleToBiographyMapper
) : ArticleRepository {
    override fun getArticleByArtistName(artistName: String): Article {
        val dbArtistBiography = articleLocalStorage.getArticleByArtistName(artistName)
        val artistBiography: ArtistBiography?

        if (dbArtistBiography != null)
            artistBiography = dbArtistBiography.apply { markItAsLocal(dbArtistBiography) }
        else {
            val lastFMArticle = articleService.getArticle(artistName)
            artistBiography = if (lastFMArticle == LastFMArticle.LastFMArticleWithoutData) null
                else lastFMArticleToBiographyMapper.getBiographyFromArticle(lastFMArticle as LastFMArticle.LastFMArticleWithData)
            artistBiography?.let {
                if (it.isSavedSong())
                    articleLocalStorage.updateArticle(artistName, it)
                else
                    articleLocalStorage.insertArticle(artistName, it)
            }
        }

        return artistBiography ?: Article.EmptyArtistData
    }

    private fun ArtistBiography.isSavedSong() = articleLocalStorage.getArticleByArtistName(this.name) != null

    private fun markItAsLocal(artistBiography: ArtistBiography) {
        artistBiography.isStoredLocally = true
    }
}