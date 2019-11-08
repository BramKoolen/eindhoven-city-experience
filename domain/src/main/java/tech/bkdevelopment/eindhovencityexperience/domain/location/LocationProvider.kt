package tech.bkdevelopment.eindhovencityexperience.domain.location

import io.reactivex.Observable
import tech.bkdevelopment.eindhovencityexperience.domain.location.model.Location

interface LocationProvider {

    fun fetchCurrentLocation(): Observable<Location>
}