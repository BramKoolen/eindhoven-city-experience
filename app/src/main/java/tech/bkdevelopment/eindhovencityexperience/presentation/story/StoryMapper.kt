package tech.bkdevelopment.eindhovencityexperience.presentation.story

import tech.bkdevelopment.eindhovencityexperience.domain.story.model.Story
import tech.bkdevelopment.eindhovencityexperience.presentation.media.MediaMapper
import javax.inject.Inject

class StoryMapper @Inject constructor(private val mediaMapper: MediaMapper) {

    fun mapToStoryViewModels(stories: List<Story>): List<StoryViewModel> {
        return stories.map {
            mapToStoryViewModel(it)
        }
    }

    fun mapToStoryViewModel(story: Story): StoryViewModel {
        return StoryViewModel(
            story.id,
            story.title,
            story.summaryText,
            story.storyType,
            story.summary,
            story.description,
            mediaMapper.mapToMediaViewModels(story.mediaItems),
            story.lat,
            story.long,
            story.completed,
            1200
        )
    }
}