package ayds.songinfo.moredetails.fulllogic.view

import ayds.songinfo.moredetails.fulllogic.controller.OtherInfoControllerInjector
import ayds.songinfo.moredetails.fulllogic.model.OtherInfoModelInjector

object OtherInfoViewInjector {
    fun init(otherInfoView: OtherInfoView) {
        OtherInfoModelInjector.initOtherInfoModel(otherInfoView)
        OtherInfoControllerInjector.onViewStarted(otherInfoView)
    }
}
