package ayds.songinfo.moredetails.fulllogic.model

import ayds.observer.Observable
import ayds.observer.Subject
import ayds.songinfo.moredetails.fulllogic.model.entities.Article
import ayds.songinfo.moredetails.fulllogic.model.repository.ArticleRepository

interface OtherInfoModel {
    val articleObservable: Observable<Article>
    fun getArticleByArtistName(artistName: String)
} 
 
internal class OtherInfoModelImpl(
    private val repository: ArticleRepository,
) : OtherInfoModel {
    override val articleObservable = Subject<Article>()

    override fun getArticleByArtistName(artistName: String) {
        repository.getArticleByArtistName(artistName).let {
            articleObservable.notify(it)
        }
    }
}