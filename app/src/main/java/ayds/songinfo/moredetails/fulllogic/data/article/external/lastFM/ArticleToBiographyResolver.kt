package ayds.songinfo.moredetails.fulllogic.data.article.external.lastFM

import ayds.songinfo.moredetails.fulllogic.domain.Article.ArtistBiography
import com.google.gson.Gson
import com.google.gson.JsonObject

interface ArticleToBiographyResolver {
    fun getArtistBiographyFromExternalData(
        serviceData: String?,
        artistName: String
    ): ArtistBiography?
}

private const val ARTIST_KEY_JSON = "artist"
private const val ARTIST_BIO_KEY_JSON = "bio"
private const val ARTIST_CONTENT_KEY_JSON = "content"
private const val ARTIST_URL_KEY_JSON = "url"

internal class JsonArticleToBiographyResolver : ArticleToBiographyResolver {
    override fun getArtistBiographyFromExternalData(
        serviceData: String?,
        artistName: String
    ): ArtistBiography? =
        try {
            serviceData?.getArtistObject()?.let { artistObject ->
                ArtistBiography(
                    artistName, artistObject.getBiography(), artistObject.getArticleUrl()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    private fun String?.getArtistObject(): JsonObject {
        val jobj = Gson().fromJson(this, JsonObject::class.java)
        return jobj[ARTIST_KEY_JSON].asJsonObject
    }

    private fun JsonObject.getBiography(): String {
        val bio = this[ARTIST_BIO_KEY_JSON].getAsJsonObject()
        val extract = bio[ARTIST_CONTENT_KEY_JSON]
        return extract?.asString ?: "No Results"
    }

    private fun JsonObject.getArticleUrl() = this[ARTIST_URL_KEY_JSON].asString
}