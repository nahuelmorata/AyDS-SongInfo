package ayds.songinfo.moredetails.fulllogic.domain

sealed class Article {
    data class ArtistBiography(
        val name: String,
        var biography: String,
        val articleUrl: String
    ) : Article()
}