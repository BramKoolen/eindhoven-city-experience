package tech.bkdevelopment.eindhovencityexperience.data.story.contentful

import com.contentful.vault.Asset
import com.contentful.vault.Resource
import tech.bkdevelopment.eindhovencityexperience.data.generic.contentful.ContentfulSubstring
import tech.bkdevelopment.eindhovencityexperience.data.generic.contentful.httpsUrl
import tech.bkdevelopment.eindhovencityexperience.domain.story.model.Story
import tech.bkdevelopment.eindhovencityexperience.domain.story.model.StoryType
import javax.inject.Inject

class StoryContentulMapper @Inject constructor(private val contentfulSubstring: ContentfulSubstring) {

    fun mapToStories(list: List<@JvmSuppressWildcards Resource>?): List<Story> {
        return list?.let {
            it.mapIndexedNotNull { _, resource: Resource ->
                when (resource) {
                    is StoryResponse -> mapToStory(resource)
                    else -> null
                }
            }
        } ?: emptyList()
    }

    private fun mapToStory(response: StoryResponse): Story {
        with(response) {
            return Story(
                remoteId(),
                title.orEmpty(),
                summaryText.orEmpty(),
                mapToStoryType(storyType),
                contentfulSubstring.subStringContenfulRichTextItem(summary.orEmpty()),
                contentfulSubstring.subStringContenfulRichTextItem(description.orEmpty()),
                mapToMediaList(media),
                contentfulSubstring.subStringContentfulLocationLat(location.orEmpty()),
                contentfulSubstring.subStringContentfulLocationLong(location.orEmpty()),
                false
            )
        }
    }

    private fun mapToStoryType(storyType: String?): StoryType {
        return when (storyType?.toLowerCase()) {
            "text" -> StoryType.TEXT
            "photo" -> StoryType.PHOTO
            "audio" -> StoryType.AUDIO
            "video" -> StoryType.VIDEO
            "vr" -> StoryType.VR
            else -> StoryType.TEXT
        }
    }

    private fun mapToMediaList(list: List<@JvmSuppressWildcards Resource>?): List<String> {
        return list?.let {
            it.mapIndexedNotNull { _, resource: Resource ->
                mapToAsset(resource)
            }
        } ?: emptyList()
    }

    private fun mapToAsset(resource: Resource): String? {
        return when (resource) {
            is Asset -> resource.httpsUrl()
            else -> null
        }
    }
}