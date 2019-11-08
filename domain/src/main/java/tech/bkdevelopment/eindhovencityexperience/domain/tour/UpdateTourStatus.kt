package tech.bkdevelopment.eindhovencityexperience.domain.tour

import io.reactivex.Completable
import tech.bkdevelopment.eindhovencityexperience.domain.tour.data.TourRepository
import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.TourState
import javax.inject.Inject

class UpdateTourStatus @Inject constructor(private val tourRepository: TourRepository) {

    operator fun invoke(tourId: String, tourState: TourState): Completable {
        return tourRepository.updateTourStatus(tourId, tourState)
    }
}