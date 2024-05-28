package ayds.songinfo.moredetails.domain

interface ArticleRepository {
    fun getArticleByArtistName(artistName: String): Article
}