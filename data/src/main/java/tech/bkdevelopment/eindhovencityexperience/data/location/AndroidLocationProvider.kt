package tech.bkdevelopment.eindhovencityexperience.data.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import io.reactivex.Observable
import tech.bkdevelopment.eindhovencityexperience.data.generic.dagger.DataContext
import tech.bkdevelopment.eindhovencityexperience.domain.location.LocationNotEnabledException
import tech.bkdevelopment.eindhovencityexperience.domain.location.LocationPermissionException
import tech.bkdevelopment.eindhovencityexperience.domain.location.LocationProvider
import tech.bkdevelopment.eindhovencityexperience.domain.location.model.Location
import javax.inject.Inject
import android.location.Location as AndroidLocation

class AndroidLocationProvider @Inject constructor(
    @DataContext private val context: Context,
    private val fusedLocationClient: FusedLocationProviderClient
) :
    LocationProvider {

    override fun fetchCurrentLocation(): Observable<Location> {
        if (!checkPermission()) return Observable.error(LocationPermissionException())
        if (!checkLocationServices()) return Observable.error(LocationNotEnabledException())
        return Observable.create { emitter ->
            fusedLocationClient.lastLocation.addOnSuccessListener { androidLocation ->
                androidLocation?.let {
                    emitter.onNext(mapToLocation(it))
                } ?: emitter.tryOnError(Exception("No location available"))

            }
            fusedLocationClient.lastLocation.addOnFailureListener {
                emitter.tryOnError(it)
            }
        }
    }

    private fun mapToLocation(androidLocation: AndroidLocation): Location {
        return Location(
            lat = androidLocation.latitude,
            long = androidLocation.longitude
        )
    }

    private fun checkPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun checkLocationServices(): Boolean {
        return try {
            Settings.Secure.getInt(
                context.contentResolver,
                Settings.Secure.LOCATION_MODE
            ) != Settings.Secure.LOCATION_MODE_OFF
        } catch (exception: Settings.SettingNotFoundException) {
            false
        }
    }
}