package ayds.artist.external.lastfm.data

const val LASTFM_LOGO_URL = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"

sealed class LastFMArticle {
    class LastFMArticleWithData(
        val name: String,
        val biography: String,
        val articleUrl: String,
    ) : LastFMArticle()

    object LastFMArticleWithoutData : LastFMArticle()
}