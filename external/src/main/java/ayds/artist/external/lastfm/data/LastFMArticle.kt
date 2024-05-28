package ayds.artist.external.lastfm.data

sealed class LastFMArticle {
    class LastFMArticleWithData(
        val name: String,
        val biography: String,
        val articleUrl: String,
    ) : LastFMArticle()

    object LastFMArticleWithoutData : LastFMArticle()
}