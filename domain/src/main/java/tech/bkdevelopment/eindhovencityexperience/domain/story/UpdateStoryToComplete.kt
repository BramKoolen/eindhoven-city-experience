package tech.bkdevelopment.eindhovencityexperience.domain.story

import io.reactivex.Completable
import tech.bkdevelopment.eindhovencityexperience.domain.story.data.StoryRepository
import javax.inject.Inject

class UpdateStoryToComplete @Inject constructor(private val storyRepository: StoryRepository) {

    operator fun invoke(tourId: String): Completable {
        return storyRepository.updateStoryStatusToComplete(tourId)
    }
}