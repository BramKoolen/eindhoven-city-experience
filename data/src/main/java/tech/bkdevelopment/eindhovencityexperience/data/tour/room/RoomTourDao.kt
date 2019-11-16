package tech.bkdevelopment.eindhovencityexperience.data.tour.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable

@Dao
interface RoomTourDao {

    @Query("SELECT * FROM tour_status")
    fun getTourStatusList(): List<TourStatus>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateTourStatus(tourStatus: TourStatus): Completable
}