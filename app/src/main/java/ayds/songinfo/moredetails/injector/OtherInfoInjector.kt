package ayds.songinfo.moredetails.injector

import android.content.Context
import androidx.room.Room
import ayds.songinfo.moredetails.data.CardRepositoryImpl
import ayds.artist.external.lastfm.injector.LastFMInjector
import ayds.songinfo.moredetails.data.LastFMArticleToCardMapperImpl
import ayds.songinfo.moredetails.data.card.local.room.CardDatabase
import ayds.songinfo.moredetails.data.card.local.room.CardLocalStorageRoomImpl
import ayds.songinfo.moredetails.presentation.OtherInfoDescriptionHelperImpl
import ayds.songinfo.moredetails.presentation.OtherInfoPresenter
import ayds.songinfo.moredetails.presentation.OtherInfoPresenterImpl


private const val CARD_DATABASE_NAME = "card-database"

object OtherInfoInjector {
    lateinit var presenter: OtherInfoPresenter

    fun initGraph(context: Context) {
        val dataBase = Room.databaseBuilder(
            context,
            CardDatabase::class.java, CARD_DATABASE_NAME
        ).build()

        val cardLocalStorage = CardLocalStorageRoomImpl(dataBase)
        val articleToCardMapper = LastFMArticleToCardMapperImpl()

        val repository = CardRepositoryImpl(cardLocalStorage, LastFMInjector.lastFMService, articleToCardMapper)

        val otherInfoDescriptionHelper = OtherInfoDescriptionHelperImpl()

        presenter = OtherInfoPresenterImpl(repository, otherInfoDescriptionHelper)
    }
}