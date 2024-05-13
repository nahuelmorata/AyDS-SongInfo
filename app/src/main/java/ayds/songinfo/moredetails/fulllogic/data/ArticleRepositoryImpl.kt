package ayds.songinfo.moredetails.fulllogic.data

import ayds.songinfo.moredetails.fulllogic.data.article.local.ArticleLocalStorage
import ayds.songinfo.moredetails.fulllogic.data.article.ArticleService
import ayds.songinfo.moredetails.fulllogic.domain.Article.ArtistBiography
import ayds.songinfo.moredetails.fulllogic.domain.ArticleRepository

internal class ArticleRepositoryImpl(
    private val articleLocalStorage: ArticleLocalStorage,
    private val articleService: ArticleService
) : ArticleRepository {
    override fun getArticleByArtistName(artistName: String): ArtistBiography {
        val dbArtistBiography = articleLocalStorage.getArticleByArtistName(artistName)
        val artistBiography: ArtistBiography?

        when {
            dbArtistBiography != null -> artistBiography = dbArtistBiography.apply { markItAsLocal(dbArtistBiography) }
            else -> {
                artistBiography = articleService.getArticle(artistName)

                artistBiography?.let {
                    when {
                        it.isSavedSong() -> articleLocalStorage.updateArticle(artistName, it)
                        else -> articleLocalStorage.insertArticle(artistName, it)
                    }
                }
            }
        }

        return artistBiography ?: ArtistBiography(artistName, "", "")
    }

    private fun ArtistBiography.isSavedSong() = articleLocalStorage.getArticleByArtistName(this.name) != null

    private fun markItAsLocal(artistBiography: ArtistBiography) {
        artistBiography.biography = "[*]${artistBiography.biography}"
    }
}