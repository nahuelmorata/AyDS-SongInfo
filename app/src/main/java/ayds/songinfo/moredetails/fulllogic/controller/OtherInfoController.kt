package ayds.songinfo.moredetails.fulllogic.controller

import ayds.songinfo.moredetails.fulllogic.model.OtherInfoModel
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
    }
}
