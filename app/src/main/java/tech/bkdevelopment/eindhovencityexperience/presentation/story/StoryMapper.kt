package tech.bkdevelopment.eindhovencityexperience.presentation.story

import tech.bkdevelopment.eindhovencityexperience.domain.story.model.Story
import javax.inject.Inject

class StoryMapper @Inject constructor() {

    fun mapToStoryViewModels(stories: List<Story>): List<StoryViewModel> {
        return stories.map {
            StoryViewModel(
                it.id,
                it.title,
                it.summaryText,
                it.storyType,
                it.summary,
                it.description,
                it.mediaUrlList,
                it.lat,
                it.long,
                it.completed,
                0
            )
        }
    }
}