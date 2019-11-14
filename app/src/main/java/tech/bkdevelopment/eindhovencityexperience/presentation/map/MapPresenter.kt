package tech.bkdevelopment.eindhovencityexperience.presentation.map

import android.app.Activity
import com.google.android.gms.location.LocationRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider
import tech.bkdevelopment.eindhovencityexperience.domain.location.model.Location
import tech.bkdevelopment.eindhovencityexperience.domain.story.GetDistanceBetweenTwoLocations
import tech.bkdevelopment.eindhovencityexperience.domain.tour.GetTourById
import tech.bkdevelopment.eindhovencityexperience.domain.tour.UpdateTourStatus
import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.TourState
import tech.bkdevelopment.eindhovencityexperience.presentation.notification.ContinuousNotificationService
import tech.bkdevelopment.eindhovencityexperience.presentation.story.StoryViewModel
import tech.bkdevelopment.eindhovencityexperience.presentation.tour.TourMapper
import tech.bkdevelopment.eindhovencityexperience.presentation.tour.TourViewModel
import timber.log.Timber
import javax.inject.Inject
import android.location.Location as AndroidLocation

class MapPresenter @Inject constructor(
    private val view: MapContract.View,
    private val navigator: MapContract.Navigator,
    private val activity: Activity,
    private val getTourById: GetTourById,
    private val tourMapper: TourMapper,
    private val getDistanceBetweenTwoLocations: GetDistanceBetweenTwoLocations,
    private val updateTourStatus: UpdateTourStatus
) : MapContract.Presenter {

    private var compositeDisposable = CompositeDisposable()

    override fun startPresenting() {
        view.checkLocationPermissions()
    }

    override fun onStoryClicked(story: StoryViewModel) {
        when (view.tour?.state) {
            TourState.STARTED -> isCloseEnoughToStory(story)
            TourState.DONE -> navigator.navigateToStory(story)
            TourState.TODO -> view.showStartTourDialog()
            TourState.STOPPED -> view.showStartTourDialog()
        }
    }

    override fun onCurrentLocationButtonClicked() {
        getCurrentLocation()
    }

    override fun onStoryCardFocusChanged(position: Int) {
        view.tour?.stories?.get(position)?.lat?.let {
            view.tour?.stories?.get(position)?.long?.let { it1 -> Location(it, it1) }
        }?.let { view.zoomToLocation(it) }

        view.updateSelectedMarker(position)
    }

    override fun onStartTourClicked() {
        view.tour?.state = TourState.STARTED
        activity.startService(
            ContinuousNotificationService.createForegroundServiceIntent(
                activity, view.tour
            )
        )

        view.tour?.id?.let {
            updateTourStatus.invoke(it, TourState.STARTED)
                .subscribeOn(Schedulers.io())
                .subscribe()
                .addTo(compositeDisposable)
        }
    }

    override fun onLocationPermissionGranted() {
        view.updateCurrentLocationButton(true)
        view.tourId?.let { getTour(it) }
        getCurrentLocation()
        observeCurrentLocation()
    }

    override fun onLocationPermissionDenied() {
        view.updateCurrentLocationButton(false)
        view.showLocationPermissionDeniedExplanationDialog()
    }

    override fun onDialogSettingsButtonClicked() {
        navigator.navigateToPermissionSettings()
    }

    override fun stopPresenting() {
        compositeDisposable.clear()
    }

    private fun getTour(tourId: String) {
        getTourById(tourId)
            .map { tourMapper.mapToTourVieModel(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onTourFetched, ::onTourFetchedFailed)
            .addTo(compositeDisposable)
    }

    private fun onTourFetched(tour: TourViewModel) {
        if (tour.stories.any { it.completed }) {
            activity.stopService(
                ContinuousNotificationService.createForegroundServiceIntent(
                    activity,
                    null
                )
            )
            updateTourStatus(tour.id, TourState.DONE)
            tour.state = TourState.DONE
            view.showTourFinishedDialog()
        }
        view.tour = tour
    }

    private fun onTourFetchedFailed(throwable: Throwable) {
        Timber.i(throwable)
    }

    private fun getCurrentLocation() {
        val locationProvider = ReactiveLocationProvider(activity)
        locationProvider.lastKnownLocation
            .map { androidLocation: AndroidLocation ->
                Location(
                    androidLocation.latitude,
                    androidLocation.longitude
                )
            }
            .subscribe(::onCurrentLocationFetched, ::onCurrentLocationFailed)
            .addTo(compositeDisposable)
    }

    private fun onCurrentLocationFetched(currentLocation: Location) {
        view.zoomToLocation(
            Location(
                currentLocation.lat,
                currentLocation.long
            )
        )
    }

    private fun onCurrentLocationFailed(throwable: Throwable) {
        Timber.e(throwable)
    }

    private fun observeCurrentLocation() {
        val request = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(LOCATION_UPDATE_INTERVAL)

        val locationProvider = ReactiveLocationProvider(activity)
        locationProvider.getUpdatedLocation(request)
            .map { androidLocation: AndroidLocation ->
                Location(
                    androidLocation.latitude,
                    androidLocation.longitude
                )
            }
            .subscribe(::onCurrentLocationUpdateFetched, ::onCurrentLocationUpdateFailed)
            .addTo(compositeDisposable)

    }

    private fun onCurrentLocationUpdateFetched(currentLocation: Location) {
        view.tour?.stories?.forEach {
            it.distanceToCurrentLocation = getDistanceBetweenTwoLocations(
                it.lat,
                it.long,
                currentLocation.lat,
                currentLocation.long
            )
        }
        view.updateStories()
    }

    private fun onCurrentLocationUpdateFailed(throwable: Throwable) {
        Timber.e(throwable)
    }

    private fun isCloseEnoughToStory(story: StoryViewModel) {
        if (story.distanceToCurrentLocation <= DISTANCE_TO_UNLOCK_STORY) {
            navigator.navigateToStory(story)
        } else {
            view.showNotCloseEnoughToStoryDialog()
        }
    }

    companion object {

        private const val DISTANCE_TO_UNLOCK_STORY = 10
        private const val LOCATION_UPDATE_INTERVAL: Long = 5000
    }
}