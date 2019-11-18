package tech.bkdevelopment.eindhovencityexperience.domain.story.data

import io.reactivex.Completable

interface StoryRepository {

    fun updateStoryStatusToComplete(storyId: String): Completable
}