package ayds.songinfo.moredetails.fulllogic.model.repository.external.article.lastFM

import ayds.songinfo.moredetails.fulllogic.model.entities.Article.ArtistBiography
import ayds.songinfo.moredetails.fulllogic.model.repository.external.article.ArticleService
import retrofit2.Response

internal class LastFMArticleServiceImpl(
    private val lastFMArticleAPI: LastFMArticleAPI,
    private val articleToBiographyResolver: ArticleToBiographyResolver,
) : ArticleService {
    override fun getArticle(artistName: String): ArtistBiography? {
        val callResponse = getArticleFromService(artistName)
        return articleToBiographyResolver.getArtistBiographyFromExternalData(callResponse.body(), artistName)
    }

    private fun getArticleFromService(query: String): Response<String> {
        return lastFMArticleAPI.getArtistInfo(query).execute()
    }
}