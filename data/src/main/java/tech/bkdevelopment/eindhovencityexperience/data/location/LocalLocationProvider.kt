package tech.bkdevelopment.eindhovencityexperience.data.location

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider
import tech.bkdevelopment.eindhovencityexperience.data.generic.dagger.DataContext
import tech.bkdevelopment.eindhovencityexperience.domain.location.data.LocationProvider
import tech.bkdevelopment.eindhovencityexperience.domain.location.model.Location
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import android.location.Location as AndroidLocation

@Singleton
class LocalLocationProvider @Inject constructor(@DataContext private val context: Context) :
    LocationProvider {

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    private var compositeDisposable = CompositeDisposable()

    private val currentLocationSubject: BehaviorSubject<Location> by lazy {
        BehaviorSubject.createDefault(Location(0.0, 0.0))
    }

    override fun observeCurrentLocation(): Observable<Location> {
        return currentLocationSubject
    }

    override fun startObserveCurrentLocation() {
        val request = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(LOCATION_UPDATE_INTERVAL)

        val locationProvider = ReactiveLocationProvider(context)
        compositeDisposable.clear()
        locationProvider.getUpdatedLocation(request)
            .map { androidLocation: android.location.Location ->
                Location(
                    androidLocation.latitude,
                    androidLocation.longitude
                )
            }
            .subscribe(::onCurrentLocationUpdateFetched, ::onCurrentLocationUpdateFailed)
            .addTo(compositeDisposable)
    }

    override fun stopObserveCurrentLocation() {
        compositeDisposable.clear()
    }

    private fun onCurrentLocationUpdateFetched(currentLocation: Location) {
        currentLocationSubject.onNext(currentLocation)
    }

    private fun onCurrentLocationUpdateFailed(throwable: Throwable) {
        Timber.e(throwable)
    }

    override fun getLastKnownLocation(): Observable<Location> {
        return Observable.create {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: AndroidLocation ->
                val test = Location(location.latitude, location.longitude)
                currentLocationSubject.onNext(test)
                it.onNext(test)
            }
        }
    }

    companion object {

        private const val LOCATION_UPDATE_INTERVAL: Long = 5000
    }
}