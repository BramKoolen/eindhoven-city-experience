package tech.bkdevelopment.eindhovencityexperience.data.tour.contentful

import com.contentful.vault.Resource
import tech.bkdevelopment.eindhovencityexperience.data.generic.contentful.ContentfulSubstring
import tech.bkdevelopment.eindhovencityexperience.data.generic.contentful.httpsUrl
import tech.bkdevelopment.eindhovencityexperience.data.story.contentful.StoryContentulMapper
import tech.bkdevelopment.eindhovencityexperience.domain.story.model.MediaType
import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.Address
import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.TextColor
import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.Tour
import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.TourState
import javax.inject.Inject

class TourContentfulMapper @Inject constructor(
    private val storyContentulMapper: StoryContentulMapper,
    private val contentfulSubstring: ContentfulSubstring
) {

    fun mapToTours(response: List<TourResponse>): List<Tour> {
        return response.map { mapToTour(it) }
    }

    private fun mapToTour(response: TourResponse): Tour {
        with(response) {
            return Tour(
                remoteId(),
                title.orEmpty(),
                description.orEmpty(),
                thumbnail?.httpsUrl(),
                icon?.httpsUrl(),
                storyContentulMapper.mapToStories(story),
                extraTourItemText.orEmpty(),
                extraTourItemIcon?.httpsUrl(),
                mapToTextColor(progressTextColor),
                mapToAddress(parkingAddress),
                mapToAddress(startAddress),
                contentfulSubstring.subStringContenfulRichTextItem(longDescription.orEmpty()),
                mapToCafeList(cafes),
                TourState.TODO
            )
        }
    }

    private fun mapToTextColor(textColor: String?): TextColor {
        return when (textColor?.toLowerCase()) {
            "black" -> TextColor.BLACK
            "white" -> TextColor.WHITE
            else -> TextColor.BLACK
        }
    }

    private fun mapToAddress(resource: Resource?): Address? {
        return resource?.let {
            when (resource) {
                is AddressResponse -> mapResourceToAddress(resource)
                else -> null
            }
        }
    }


    private fun mapResourceToAddress(response: AddressResponse): Address {
        with(response) {
            return Address(
                remoteId(),
                name.orEmpty(),
                contentfulSubstring.subStringContentfulLocationLat(location.orEmpty()),
                contentfulSubstring.subStringContentfulLocationLong(location.orEmpty())
            )
        }
    }

    private fun mapToCafeList(list: List<@JvmSuppressWildcards Resource>?): List<Address> {
        return list?.let {
            it.mapIndexedNotNull { _, resource: Resource ->
                mapToAddress(resource)
            }
        } ?: emptyList()
    }
}