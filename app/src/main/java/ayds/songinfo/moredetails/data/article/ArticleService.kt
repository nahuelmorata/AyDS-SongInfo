package ayds.songinfo.moredetails.data.article

import ayds.songinfo.moredetails.domain.Article.ArtistBiography

interface ArticleService {
    fun getArticle(artistName: String): ArtistBiography?
}