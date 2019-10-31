package tech.bkdevelopment.eindhovencityexperience.domain.tour

import io.reactivex.Observable
import io.reactivex.Single
import tech.bkdevelopment.eindhovencityexperience.domain.tour.data.TourRepository
import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.Tour
import javax.inject.Inject

class GetTours @Inject constructor(private val tourRepository: TourRepository) {

    operator fun invoke(): Observable<List<Tour>> {
        return tourRepository.fetchTours().map {
            it.sortedBy { tour: Tour -> tour.title  }.sortedBy { tour -> tour.state }
        }
    }
}