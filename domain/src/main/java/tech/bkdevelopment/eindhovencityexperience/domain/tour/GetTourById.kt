package tech.bkdevelopment.eindhovencityexperience.domain.tour

import io.reactivex.Observable
import tech.bkdevelopment.eindhovencityexperience.domain.tour.data.TourRepository
import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.Tour
import javax.inject.Inject

class GetTourById @Inject constructor(private val tourRepository: TourRepository) {

    operator fun invoke(tourId: String): Observable<Tour> {
        return tourRepository.fetchTourById(tourId)
    }
}