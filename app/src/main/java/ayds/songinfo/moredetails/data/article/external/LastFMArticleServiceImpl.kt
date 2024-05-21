package ayds.songinfo.moredetails.data.article.external

import ayds.songinfo.moredetails.domain.Article.ArtistBiography
import ayds.songinfo.moredetails.data.article.ArticleService
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