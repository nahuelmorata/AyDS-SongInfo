package ayds.songinfo.moredetails.fulllogic.model.repository.external.article

import ayds.songinfo.moredetails.fulllogic.model.repository.external.article.lastFM.LastFMArticleInjector

object ArticleInjector {
    val articleService: ArticleService = LastFMArticleInjector.articleService
}