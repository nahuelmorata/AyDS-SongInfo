package ayds.songinfo.moredetails.fulllogic.controller

import ayds.songinfo.moredetails.fulllogic.model.OtherInfoModelInjector
import ayds.songinfo.moredetails.fulllogic.view.OtherInfoView

object OtherInfoControllerInjector {
    fun onViewStarted(otherInfoView: OtherInfoView) {
        OtherInfoControllerImpl(OtherInfoModelInjector.getOtherInfoModel()).apply {
            setOtherInfoView(otherInfoView)
        }
    }
}
