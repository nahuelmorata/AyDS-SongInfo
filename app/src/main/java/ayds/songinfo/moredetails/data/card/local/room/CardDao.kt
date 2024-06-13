package ayds.songinfo.moredetails.data.card.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCard(card: CardEntity)

    @Query("SELECT * FROM CardEntity WHERE name LIKE :name LIMIT 1")
    fun getCardByName(name: String): CardEntity?

    @Update
    fun updateCard(card: CardEntity)

    @Query("SELECT * FROM CardEntity WHERE name LIKE :name")
    fun getCardsByName(name: String): List<CardEntity>
}