package ayds.songinfo.moredetails.fulllogic.model.entities

import ayds.songinfo.home.model.entities.Song

sealed class Article {
    data class ArtistBiography(
        val name: String,
        var biography: String,
        val articleUrl: String
    ) : Article()

    object ArtistBiographyEmpty : Article()
}