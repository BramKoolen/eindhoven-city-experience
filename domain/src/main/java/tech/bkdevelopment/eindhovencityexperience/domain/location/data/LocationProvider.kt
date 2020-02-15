package tech.bkdevelopment.eindhovencityexperience.domain.location.data

import io.reactivex.Observable
import tech.bkdevelopment.eindhovencityexperience.domain.location.model.Location

interface LocationProvider {

    fun observeCurrentLocation(): Observable<Location>
    fun startObserveCurrentLocation()
    fun stopObserveCurrentLocation()
    fun getLastKnownLocation(): Observable<Location>
}