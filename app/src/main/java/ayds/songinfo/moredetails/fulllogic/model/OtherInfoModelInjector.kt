package ayds.songinfo.moredetails.fulllogic.model

import android.content.Context
import androidx.room.Room
import ayds.songinfo.moredetails.fulllogic.model.repository.ArticleRepository
import ayds.songinfo.moredetails.fulllogic.model.repository.ArticleRepositoryImpl
import ayds.songinfo.moredetails.fulllogic.model.repository.external.article.ArticleInjector
import ayds.songinfo.moredetails.fulllogic.model.repository.external.article.ArticleService
import ayds.songinfo.moredetails.fulllogic.model.repository.local.article.ArticleLocalStorage
import ayds.songinfo.moredetails.fulllogic.model.repository.local.article.room.ArticleDatabase
import ayds.songinfo.moredetails.fulllogic.model.repository.local.article.room.ArticleLocalStorageRoomImpl
import ayds.songinfo.moredetails.fulllogic.view.OtherInfoView

object OtherInfoModelInjector {
    private lateinit var otherInfoModel: OtherInfoModel

    fun getOtherInfoModel(): OtherInfoModel = otherInfoModel

    fun initOtherInfoModel(otherInfoView: OtherInfoView) {
        val dataBase = Room.databaseBuilder(
            otherInfoView as Context,
            ArticleDatabase::class.java, "article-database"
        ).build()

        val articleLocalRoomStorage: ArticleLocalStorage = ArticleLocalStorageRoomImpl(dataBase)
        val articleService: ArticleService = ArticleInjector.articleService

        val articleRepository: ArticleRepository =
            ArticleRepositoryImpl(articleLocalRoomStorage, articleService)

        otherInfoModel = OtherInfoModelImpl(articleRepository)
    }
}
