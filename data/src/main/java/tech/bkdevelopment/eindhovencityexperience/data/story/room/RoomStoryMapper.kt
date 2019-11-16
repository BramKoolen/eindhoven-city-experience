package tech.bkdevelopment.eindhovencityexperience.data.story.room

import javax.inject.Inject

class RoomStoryMapper @Inject constructor() {

    fun mapToRoomCompletedStory(storyId: String): CompletedStory {
        return CompletedStory(storyId)
    }
}