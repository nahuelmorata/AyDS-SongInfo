package ayds.songinfo.moredetails.data.card.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import ayds.songinfo.moredetails.domain.CardSource

@Entity
data class CardEntity(
    @PrimaryKey
    val name: String,
    var description: String,
    val infoUrl: String,
    val source: CardSource = CardSource.LastFm,
    val sourceLogoUrl: String? = null,
)