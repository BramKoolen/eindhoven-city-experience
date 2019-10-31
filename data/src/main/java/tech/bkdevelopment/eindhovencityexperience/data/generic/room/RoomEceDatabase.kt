package tech.bkdevelopment.eindhovencityexperience.data.generic.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import tech.bkdevelopment.eindhovencityexperience.data.story.room.CompletedStory
import tech.bkdevelopment.eindhovencityexperience.data.story.room.RoomStoryDao
import tech.bkdevelopment.eindhovencityexperience.data.tour.room.RoomTourDao
import tech.bkdevelopment.eindhovencityexperience.data.tour.room.TourStatus

@Database(entities = [
    TourStatus::class,
    CompletedStory::class],
    version = 1)
abstract class RoomEceDatabase : RoomDatabase() {

    abstract fun roomToursDao(): RoomTourDao

    abstract fun roomStoryDao(): RoomStoryDao

    companion object {

        private const val DATABASE_NAME = "ece_database"

        fun getInstance(context: Context): RoomEceDatabase {
            return Room.databaseBuilder(context, RoomEceDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
        }
    }
}