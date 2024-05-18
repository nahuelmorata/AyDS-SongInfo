package ayds.songinfo.moredetails.domain

sealed class Article {
    data class ArtistBiography(
        val name: String,
        var biography: String,
        val articleUrl: String
    ) : Article()
}