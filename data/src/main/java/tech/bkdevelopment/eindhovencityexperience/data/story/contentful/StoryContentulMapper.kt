package tech.bkdevelopment.eindhovencityexperience.data.story.contentful

import com.contentful.vault.Resource
import tech.bkdevelopment.eindhovencityexperience.data.generic.contentful.ContentfulSubstring
import tech.bkdevelopment.eindhovencityexperience.data.generic.contentful.httpsUrl
import tech.bkdevelopment.eindhovencityexperience.domain.story.model.Media
import tech.bkdevelopment.eindhovencityexperience.domain.story.model.MediaType
import tech.bkdevelopment.eindhovencityexperience.domain.story.model.Story
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
                mapToMediaType(storyType),
                contentfulSubstring.subStringContenfulRichTextItem(summary.orEmpty()),
                contentfulSubstring.subStringContenfulRichTextItem(description.orEmpty()),
                mapToMediaList(media),
                contentfulSubstring.subStringContentfulLocationLat(location.orEmpty()),
                contentfulSubstring.subStringContentfulLocationLong(location.orEmpty()),
                false
            )
        }
    }

    private fun mapToMediaType(mediaType: String?): MediaType {
        return when (mediaType?.toLowerCase()) {
            "text" -> MediaType.TEXT
            "photo" -> MediaType.PHOTO
            "audio" -> MediaType.AUDIO
            "video" -> MediaType.VIDEO
            "vr" -> MediaType.VR
            else -> MediaType.TEXT
        }
    }

    private fun mapToMediaList(list: List<@JvmSuppressWildcards Resource>?): List<Media> {
        return list?.let {
            it.mapIndexedNotNull { _, resource: Resource ->
                when (resource) {
                    is MediaResponse -> mapToMedia(resource)
                    else -> null
                }
            }
        } ?: emptyList()
    }

    private fun mapToMedia(response: MediaResponse): Media {
        with(response) {
            return Media(
                remoteId(),
                title.orEmpty(),
                thumbnail?.httpsUrl(),
                mapToMediaType(type),
                mediaItem?.httpsUrl(),
                source.orEmpty()
            )
        }
    }
}