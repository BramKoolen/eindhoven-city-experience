package tech.bkdevelopment.eindhovencityexperience.data.tour.room

import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.TourState
import javax.inject.Inject

class RoomTourMapper @Inject constructor() {

    fun mapToRoomTourStatus(tourId: String, tourState: TourState): TourStatus{
        return TourStatus(tourId,tourState.toString())
    }
}