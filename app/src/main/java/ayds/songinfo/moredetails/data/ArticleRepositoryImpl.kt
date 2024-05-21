package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.article.local.ArticleLocalStorage
import ayds.songinfo.moredetails.data.article.ArticleService
import ayds.songinfo.moredetails.domain.Article.ArtistBiography
import ayds.songinfo.moredetails.domain.ArticleRepository

internal class ArticleRepositoryImpl(
    private val articleLocalStorage: ArticleLocalStorage,
    private val articleService: ArticleService
) : ArticleRepository {
    override fun getArticleByArtistName(artistName: String): ArtistBiography {
        val dbArtistBiography = articleLocalStorage.getArticleByArtistName(artistName)
        val artistBiography: ArtistBiography?

        if (dbArtistBiography != null)
            artistBiography = dbArtistBiography.apply { markItAsLocal(dbArtistBiography) }
        else {
            artistBiography = articleService.getArticle(artistName)
            artistBiography?.let {
                if (it.isSavedSong())
                    articleLocalStorage.updateArticle(artistName, it)
                else
                    articleLocalStorage.insertArticle(artistName, it)
            }
        }

        return artistBiography ?: ArtistBiography(artistName, "", "")
    }

    private fun ArtistBiography.isSavedSong() = articleLocalStorage.getArticleByArtistName(this.name) != null

    private fun markItAsLocal(artistBiography: ArtistBiography) {
        artistBiography.isStoredLocally = true
    }
}