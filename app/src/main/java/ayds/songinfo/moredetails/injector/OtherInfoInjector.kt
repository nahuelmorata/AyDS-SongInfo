package ayds.songinfo.moredetails.injector

import android.content.Context
import androidx.room.Room
import ayds.songinfo.moredetails.data.ArticleRepositoryImpl
import ayds.songinfo.moredetails.data.article.external.JsonArticleToBiographyResolver
import ayds.songinfo.moredetails.data.article.external.LastFMArticleAPI
import ayds.songinfo.moredetails.data.article.external.LastFMArticleServiceImpl
import ayds.songinfo.moredetails.data.article.local.room.ArticleDatabase
import ayds.songinfo.moredetails.data.article.local.room.ArticleLocalStorageRoomImpl
import ayds.songinfo.moredetails.presentation.OtherInfoDescriptionHelperImpl
import ayds.songinfo.moredetails.presentation.OtherInfoPresenter
import ayds.songinfo.moredetails.presentation.OtherInfoPresenterImpl
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

        val otherInfoDescriptionHelper = OtherInfoDescriptionHelperImpl()

        presenter = OtherInfoPresenterImpl(repository, otherInfoDescriptionHelper)
    }
}