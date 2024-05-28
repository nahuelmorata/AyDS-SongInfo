package ayds.artist.external.lastfm.injector

import ayds.artist.external.lastfm.data.JsonLastFMToBiographyResolver
import ayds.artist.external.lastfm.data.LastFMArticleAPI
import ayds.artist.external.lastfm.data.LastFMArticleServiceImpl
import ayds.artist.external.lastfm.data.LastFMService
import ayds.artist.external.lastfm.data.LastFMToBiographyResolver
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val LASTFM_BASE_URL = "https://ws.audioscrobbler.com/2.0/"

object LastFMInjector {
    private val lastFMAPI = getLastFMAPI()
    private val lastFMToArtistResolver: LastFMToBiographyResolver = JsonLastFMToBiographyResolver()

    val lastFMService: LastFMService = LastFMArticleServiceImpl(lastFMAPI, lastFMToArtistResolver)

    private fun getLastFMAPI(): LastFMArticleAPI {
        val nyTimesAPIRetrofit = Retrofit.Builder()
            .baseUrl(LASTFM_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        return nyTimesAPIRetrofit.create(LastFMArticleAPI::class.java)
    }
}