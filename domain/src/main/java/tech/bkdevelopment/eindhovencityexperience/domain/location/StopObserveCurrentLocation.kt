package tech.bkdevelopment.eindhovencityexperience.domain.location

import tech.bkdevelopment.eindhovencityexperience.domain.location.data.LocationProvider
import javax.inject.Inject

class StopObserveCurrentLocation @Inject constructor(private val locationProvider: LocationProvider) {

    operator fun invoke() {
        locationProvider.stopObserveCurrentLocation()
    }
}