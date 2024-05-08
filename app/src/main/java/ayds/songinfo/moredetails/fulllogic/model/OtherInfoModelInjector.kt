package ayds.songinfo.moredetails.fulllogic.model

import ayds.songinfo.moredetails.fulllogic.view.OtherInfoView

object OtherInfoModelInjector {
    private lateinit var otherInfoModel: OtherInfoModel

    fun getOtherInfoModel(): OtherInfoModel = otherInfoModel

    fun initOtherInfoModel(otherInfoView: OtherInfoView) {
        otherInfoModel = OtherInfoModelImpl()
    }
}
