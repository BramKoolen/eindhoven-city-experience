package tech.bkdevelopment.eindhovencityexperience.domain.tour.data

import io.reactivex.Observable
import io.reactivex.Single
import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.Tour

interface TourRepository {

    fun fetchTours(): Observable<List<Tour>>
}