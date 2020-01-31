package tech.bkdevelopment.eindhovencityexperience.data.story.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable

@Dao
interface RoomStoryDao {

    @Query("SELECT * FROM completed_story")
    fun getCompletedStoryList(): List<CompletedStory>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateStoryToComplete(completedStory: CompletedStory): Completable
}