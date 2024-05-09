package ayds.songinfo.moredetails.fulllogic.controller

import ayds.observer.Observer
import ayds.songinfo.home.view.HomeUiEvent
import ayds.songinfo.moredetails.fulllogic.model.OtherInfoModel
import ayds.songinfo.moredetails.fulllogic.view.OtherInfoUiEvent
import ayds.songinfo.moredetails.fulllogic.view.OtherInfoView

interface OtherInfoController {
    fun setOtherInfoView(otherInfoView: OtherInfoView)
}

internal class OtherInfoControllerImpl(
    private val otherInfoModel: OtherInfoModel
) : OtherInfoController {
    private lateinit var otherInfoView: OtherInfoView

    override fun setOtherInfoView(otherInfoView: OtherInfoView) {
        this.otherInfoView = otherInfoView
        otherInfoView.uiEventObservable.subscribe(observer)
    }

    private val observer: Observer<OtherInfoUiEvent> =
        Observer { value ->
            when (value) {
                OtherInfoUiEvent.OpenBiographyUrl -> openBiographyUrl()
            }
        }

    private fun openBiographyUrl() {
        Thread {

        }.start()
    }
}
