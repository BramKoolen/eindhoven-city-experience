package tech.bkdevelopment.eindhovencityexperience.data.tour.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tour_status")
class TourStatus(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "status")
    val status: String
)