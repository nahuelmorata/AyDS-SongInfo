package ayds.songinfo.moredetails.domain

sealed class Cards {
    data class Card(
        val name: String = "",
        var description: String = "",
        val infoUrl: String = "",
        val source: CardSource = CardSource.LastFm,
        val sourceLogoUrl: String? = null,
        val isEmpty: Boolean = false,
        var isStoredLocally: Boolean = false
    ) : Cards()
}