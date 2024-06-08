package ayds.songinfo.moredetails.injector

import android.content.Context
import androidx.room.Room
import ayds.songinfo.moredetails.data.CardRepositoryImpl
import ayds.artist.external.lastfm.injector.LastFMInjector
import ayds.songinfo.moredetails.data.LastFMArticleToCardMapperImpl
import ayds.songinfo.moredetails.data.card.broker.CardBrokerImpl
import ayds.songinfo.moredetails.data.card.broker.Proxy
import ayds.songinfo.moredetails.data.card.broker.proxy.LastFmProxy
import ayds.songinfo.moredetails.data.card.broker.proxy.NYTimesProxy
import ayds.songinfo.moredetails.data.card.broker.proxy.WikipediaProxy
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
        val proxies = listOf(LastFmProxy(), NYTimesProxy(), WikipediaProxy())
        val cardBroker = CardBrokerImpl(proxies)

        val repository = CardRepositoryImpl(cardLocalStorage, cardBroker)

        val otherInfoDescriptionHelper = OtherInfoDescriptionHelperImpl()

        presenter = OtherInfoPresenterImpl(repository, otherInfoDescriptionHelper)
    }
}