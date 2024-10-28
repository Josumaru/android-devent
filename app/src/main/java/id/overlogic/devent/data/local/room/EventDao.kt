package id.overlogic.devent.data.local.room


import androidx.lifecycle.LiveData
import androidx.room.*
import id.overlogic.devent.data.local.entity.EventEntity

@Dao
interface EventDao {
    @Query("SELECT * FROM event ORDER BY id DESC")
    fun getEvent(): LiveData<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEvent(event: List<EventEntity>)

    @Query("SELECT * FROM event WHERE id = :id LIMIT 1")
    fun getEventById(id: Int): EventEntity?

    @Query("UPDATE event SET bookmark = :status WHERE id = :id")
    fun setBookmark(status: Int, id: Int): Int

    @Query("SELECT * FROM event WHERE bookmark = 1")
    fun getBookmark(): List<EventEntity>?

    @Query("DELETE FROM event")
    fun deleteAll()
}
