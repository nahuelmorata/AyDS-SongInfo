package ayds.songinfo.moredetails.presentation

import ayds.songinfo.moredetails.domain.CardSource

data class OtherInfoUiState(
    val cards: List<OtherInfoCardUiState> = emptyList(),
)

data class OtherInfoCardUiState(
    val artistName: String = "",
    val descriptionHtml: String = "",
    val infoUrl: String = "",
    val logoUrl: String = "",
    val source: CardSource = CardSource.LastFm,
    val isEmpty: Boolean = false
)