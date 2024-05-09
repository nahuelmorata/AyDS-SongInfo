package ayds.songinfo.moredetails.fulllogic.model.repository.external.article.lastFM

import ayds.songinfo.moredetails.fulllogic.model.repository.external.article.ArticleService
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object LastFMArticleInjector {
    private const val LASTFM_URL = "https://ws.audioscrobbler.com/2.0/"
    private val lastFMArticleAPIRetrofit = Retrofit.Builder()
        .baseUrl(LASTFM_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
    private val lastFMArticleAPI = lastFMArticleAPIRetrofit.create(LastFMArticleAPI::class.java)
    private val articleToBiographyResolver: ArticleToBiographyResolver = JsonArticleToBiographyResolver()


    val articleService: ArticleService = LastFMArticleServiceImpl(
        lastFMArticleAPI,
        articleToBiographyResolver
    )
}