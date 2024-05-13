package ayds.songinfo.moredetails.fulllogic.injector

import android.content.Context
import androidx.room.Room
import ayds.songinfo.moredetails.fulllogic.data.ArticleRepositoryImpl
import ayds.songinfo.moredetails.fulllogic.data.article.external.lastFM.JsonArticleToBiographyResolver
import ayds.songinfo.moredetails.fulllogic.data.article.external.lastFM.LastFMArticleAPI
import ayds.songinfo.moredetails.fulllogic.data.article.external.lastFM.LastFMArticleServiceImpl
import ayds.songinfo.moredetails.fulllogic.data.article.local.room.ArticleDatabase
import ayds.songinfo.moredetails.fulllogic.data.article.local.room.ArticleLocalStorageRoomImpl
import ayds.songinfo.moredetails.fulllogic.domain.ArticleRepository
import ayds.songinfo.moredetails.fulllogic.presentation.OtherInfoPresenter
import ayds.songinfo.moredetails.fulllogic.presentation.OtherInfoPresenterImpl
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val LASTFM_BASE_URL = "https://ws.audioscrobbler.com/2.0/"
private const val ARTICLE_DATABASE_NAME = "article-database"

object OtherInfoInjector {
    lateinit var presenter: OtherInfoPresenter

    fun initGraph(context: Context) {
        val dataBase = Room.databaseBuilder(
            context,
            ArticleDatabase::class.java, ARTICLE_DATABASE_NAME
        ).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(LASTFM_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val lastFMAPI = retrofit.create(LastFMArticleAPI::class.java)

        val jsonArticleToBiographyResolver = JsonArticleToBiographyResolver()
        val lastFMArticleServiceImpl = LastFMArticleServiceImpl(lastFMAPI, jsonArticleToBiographyResolver)
        val articleLocalStorage = ArticleLocalStorageRoomImpl(dataBase)

        val repository = ArticleRepositoryImpl(articleLocalStorage, lastFMArticleServiceImpl)

        presenter = OtherInfoPresenterImpl(repository)
    }
}