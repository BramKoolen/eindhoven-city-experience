package tech.bkdevelopment.eindhovencityexperience.data.story.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "completed_story")
class CompletedStory(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String
)