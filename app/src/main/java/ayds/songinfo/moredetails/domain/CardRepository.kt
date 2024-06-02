package ayds.songinfo.moredetails.domain

interface CardRepository {
    fun getCard(artistName: String): Cards
    fun getCards(artistName: String): List<Cards>
}