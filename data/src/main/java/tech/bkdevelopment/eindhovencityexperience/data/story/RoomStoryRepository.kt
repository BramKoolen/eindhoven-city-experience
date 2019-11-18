package tech.bkdevelopment.eindhovencityexperience.data.story

import io.reactivex.Completable
import tech.bkdevelopment.eindhovencityexperience.data.generic.room.RoomEceDatabase
import tech.bkdevelopment.eindhovencityexperience.data.story.room.RoomStoryMapper
import tech.bkdevelopment.eindhovencityexperience.domain.story.data.StoryRepository
import javax.inject.Inject

class RoomStoryRepository @Inject constructor(
    private val roomStoryMapper: RoomStoryMapper,
    private val database: RoomEceDatabase
) : StoryRepository {

    override fun updateStoryStatusToComplete(storyId: String): Completable {
        return database.roomStoryDao()
            .updateStoryToComplete(roomStoryMapper.mapToRoomCompletedStory(storyId))
    }
}