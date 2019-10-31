package tech.bkdevelopment.eindhovencityexperience.data.story.room

import androidx.room.Dao
import androidx.room.Query

@Dao
interface RoomStoryDao {

    @Query("SELECT * FROM completed_story")
    fun getCompletedStoryList(): List<CompletedStory>
}