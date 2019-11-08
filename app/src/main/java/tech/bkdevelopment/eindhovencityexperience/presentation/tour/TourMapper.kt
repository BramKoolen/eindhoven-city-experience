package tech.bkdevelopment.eindhovencityexperience.presentation.tour

import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.Address
import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.Tour
import tech.bkdevelopment.eindhovencityexperience.presentation.story.StoryMapper
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
                0,//todo
                1000,//todo
                storyMapper.mapToStoryViewModels(it.stories),
                it.extraTourItemLabel,
                it.extraTourItemIconUrl,
                mapToAddressViewModel(it.parkingAddress),
                mapToAddressViewModel(it.startAddress),
                it.longDescription,
                it.cafesOnThisRoute.map { mapToAddressViewModelNotNull(it) },
                it.state
            )
        }
    }

    private fun mapToAddressViewModelNotNull(address: Address): AddressViewModel {
        return AddressViewModel(
            address.name,
            address.lat,
            address.long
        )
    }

    private fun mapToAddressViewModel(address: Address?): AddressViewModel? {
        if (address != null) {
            return AddressViewModel(
                address.name,
                address.lat,
                address.long
            )
        }
        return null
    }
}