package ayds.songinfo.moredetails.domain

sealed class Article {
    data class ArtistBiography(
        val name: String,
        var biography: String,
        val articleUrl: String,
        var isStoredLocally: Boolean = false
    ) : Article()

    object EmptyArtistData : Article()
}