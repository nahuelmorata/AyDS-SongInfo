package ayds.songinfo.moredetails.fulllogic.model.repository.external.article

import ayds.songinfo.moredetails.fulllogic.model.entities.Article

interface ArticleService {
    fun getArticle(artistName: String): Article.ArtistBiography?
}