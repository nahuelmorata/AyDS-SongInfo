package ayds.songinfo.moredetails.injector

import android.content.Context
import androidx.room.Room
import ayds.songinfo.moredetails.data.ArticleRepositoryImpl
import ayds.artist.external.lastfm.injector.LastFMInjector
import ayds.songinfo.moredetails.data.LastFMArticleToBiographyMapperImpl
import ayds.songinfo.moredetails.data.article.local.room.ArticleDatabase
import ayds.songinfo.moredetails.data.article.local.room.ArticleLocalStorageRoomImpl
import ayds.songinfo.moredetails.presentation.OtherInfoDescriptionHelperImpl
import ayds.songinfo.moredetails.presentation.OtherInfoPresenter
import ayds.songinfo.moredetails.presentation.OtherInfoPresenterImpl


private const val ARTICLE_DATABASE_NAME = "article-database"

object OtherInfoInjector {
    lateinit var presenter: OtherInfoPresenter

    fun initGraph(context: Context) {
        val dataBase = Room.databaseBuilder(
            context,
            ArticleDatabase::class.java, ARTICLE_DATABASE_NAME
        ).build()

        val articleLocalStorage = ArticleLocalStorageRoomImpl(dataBase)
        val articleToBiographyMapper = LastFMArticleToBiographyMapperImpl()

        val repository = ArticleRepositoryImpl(articleLocalStorage, LastFMInjector.lastFMService, articleToBiographyMapper)

        val otherInfoDescriptionHelper = OtherInfoDescriptionHelperImpl()

        presenter = OtherInfoPresenterImpl(repository, otherInfoDescriptionHelper)
    }
}