package ayds.artist.external.lastfm.data

import retrofit2.Response

internal class LastFMArticleServiceImpl(
    private val lastFMArticleAPI: LastFMArticleAPI,
    private val articleToBiographyResolver: LastFMToBiographyResolver,
) : LastFMService {
    override fun getArticle(artistName: String): LastFMArticle? {
        val callResponse = getArticleFromService(artistName)
        return articleToBiographyResolver.getArtistBiographyFromExternalData(callResponse.body(), artistName)
    }

    private fun getArticleFromService(query: String): Response<String> {
        return lastFMArticleAPI.getArtistInfo(query).execute()
    }
}