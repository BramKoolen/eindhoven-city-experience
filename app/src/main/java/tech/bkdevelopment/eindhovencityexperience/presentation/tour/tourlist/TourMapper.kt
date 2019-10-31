package tech.bkdevelopment.eindhovencityexperience.presentation.tour.tourlist

import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.Address
import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.Tour
import tech.bkdevelopment.eindhovencityexperience.presentation.story.StoryMapper
import java.text.SimpleDateFormat
import javax.inject.Inject

class TourMapper @Inject constructor(private val storyMapper: StoryMapper) {

    fun mapToTourViewModels(tours: List<Tour>): List<TourViewModel> {
        return tours.map {
            TourViewModel(
                it.id,
                it.title,
                it.smallDescription,
                it.thumbnailUrl,
                it.iconUrl,
                it.remainingTourTime,
                storyMapper.mapToStoryViewModels(it.stories),
                it.extraTourItemLabel,
                it.extraTourItemIconUrl,
                mapToAddressViewModel(it.parkingAddress),
                mapToAddressViewModel(it.startAddress),
                it.longDescription,
                it.cafesOnThisRoute.map { mapToAddressViewModel(it) },
                it.state
            )
        }
    }

    private fun mapToAddressViewModel(address: Address?): AddressViewModel? {
        if (address != null) {
            return AddressViewModel(address.name, address.lat, address.long)
        }
        return null
    }
}