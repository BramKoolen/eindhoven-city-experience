package tech.bkdevelopment.eindhovencityexperience.domain.location

import io.reactivex.Observable
import tech.bkdevelopment.eindhovencityexperience.domain.location.data.LocationProvider
import tech.bkdevelopment.eindhovencityexperience.domain.location.model.Location
import javax.inject.Inject

class GetLastKnownLocation @Inject constructor(private val locationProvider: LocationProvider) {

    operator fun invoke(): Observable<Location> {
        return locationProvider.getLastKnownLocation()
    }
}