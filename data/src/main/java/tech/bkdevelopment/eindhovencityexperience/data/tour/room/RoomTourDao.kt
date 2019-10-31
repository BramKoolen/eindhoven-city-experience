package tech.bkdevelopment.eindhovencityexperience.data.tour.room

import androidx.room.Dao
import androidx.room.Query

@Dao
interface RoomTourDao {

    @Query("SELECT * FROM tour_status")
    fun getTourStatusList(): List<TourStatus>
}