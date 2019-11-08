package tech.bkdevelopment.eindhovencityexperience.domain.tour.data

import io.reactivex.Completable
import io.reactivex.Observable
import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.Tour
import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.TourState

interface TourRepository {

    fun fetchTours(): Observable<List<Tour>>
    fun fetchTourById(tourId: String): Observable<Tour>
    fun updateTourStatus(tourId: String, tourState: TourState): Completable
}