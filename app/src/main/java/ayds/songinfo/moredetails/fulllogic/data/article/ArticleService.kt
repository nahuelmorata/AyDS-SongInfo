package ayds.songinfo.moredetails.fulllogic.data.article

import ayds.songinfo.moredetails.fulllogic.domain.Article.ArtistBiography

interface ArticleService {
    fun getArticle(artistName: String): ArtistBiography?
}