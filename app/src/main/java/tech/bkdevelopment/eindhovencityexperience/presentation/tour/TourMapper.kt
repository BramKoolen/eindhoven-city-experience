package tech.bkdevelopment.eindhovencityexperience.presentation.tour

import tech.bkdevelopment.eindhovencityexperience.domain.tour.GetRemainingTourDistance
import tech.bkdevelopment.eindhovencityexperience.domain.tour.GetRemainingTourTime
import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.Address
import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.Tour
import tech.bkdevelopment.eindhovencityexperience.presentation.story.StoryMapper
import javax.inject.Inject

class TourMapper @Inject constructor(
    private val storyMapper: StoryMapper,
    private val getRemainingTourDistance: GetRemainingTourDistance,
    private val getRemainingTourTime: GetRemainingTourTime
) {

    fun mapToTourViewModels(tours: List<Tour>): List<TourViewModel> {
        return tours.map { mapToTourVieModel(it) }
    }

    fun mapToTourVieModel(tour: Tour): TourViewModel {
        return TourViewModel(
            tour.id,
            tour.title,
            tour.smallDescription,
            tour.thumbnailUrl,
            tour.iconUrl,
            getRemainingTourTime(tour),
            getRemainingTourDistance(tour),
            storyMapper.mapToStoryViewModels(tour.stories),
            tour.extraTourItemLabel,
            tour.extraTourItemIconUrl,
            tour.progressTextColor,
            mapToAddressViewModel(tour.parkingAddress),
            mapToAddressViewModel(tour.startAddress),
            tour.longDescription,
            tour.cafesOnThisRoute.map { mapToAddressViewModelNotNull(it) },
            tour.state
        )
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