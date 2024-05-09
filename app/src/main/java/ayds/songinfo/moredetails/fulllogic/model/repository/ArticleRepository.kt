package ayds.songinfo.moredetails.fulllogic.model.repository

import ayds.songinfo.moredetails.fulllogic.model.entities.Article
import ayds.songinfo.moredetails.fulllogic.model.entities.Article.ArtistBiography
import ayds.songinfo.moredetails.fulllogic.model.entities.Article.ArtistBiographyEmpty
import ayds.songinfo.moredetails.fulllogic.model.repository.external.article.ArticleService
import ayds.songinfo.moredetails.fulllogic.model.repository.local.article.ArticleLocalStorage

interface ArticleRepository {
    fun getArticleByArtistName(artistName: String): Article
}

internal class ArticleRepositoryImpl(
    private val articleLocalStorage: ArticleLocalStorage,
    private val articleService: ArticleService
) : ArticleRepository {
    override fun getArticleByArtistName(artistName: String): Article {
        var artistBiography = articleLocalStorage.getArticleByArtistName(artistName)

        when {
            artistBiography != null -> markItAsLocal(artistBiography)
            else -> {
                try {
                    artistBiography = articleService.getArticle(artistName)

                    artistBiography?.let {
                        when {
                            it.isSavedSong() -> articleLocalStorage.updateArticle(artistName, it)
                            else -> articleLocalStorage.insertArticle(artistName, it)
                        }
                    }
                } catch (e: Exception) {
                    artistBiography = null
                }
            }
        }

        return artistBiography ?: ArtistBiographyEmpty
    }

    private fun ArtistBiography.isSavedSong() = articleLocalStorage.getArticleByArtistName(this.name) != null

    private fun markItAsLocal(artistBiography: ArtistBiography) {
        artistBiography.biography = "[*]${artistBiography.biography}"
    }
}