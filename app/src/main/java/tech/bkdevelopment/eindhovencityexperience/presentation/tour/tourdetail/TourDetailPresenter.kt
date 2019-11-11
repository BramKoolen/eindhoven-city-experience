package tech.bkdevelopment.eindhovencityexperience.presentation.tour.tourdetail

import android.Manifest
import android.app.Activity
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider
import tech.bkdevelopment.eindhovencityexperience.domain.location.model.Location
import tech.bkdevelopment.eindhovencityexperience.domain.story.GetNearestStory
import tech.bkdevelopment.eindhovencityexperience.domain.story.model.Story
import tech.bkdevelopment.eindhovencityexperience.domain.tour.GetTourById
import tech.bkdevelopment.eindhovencityexperience.domain.tour.UpdateTourStatus
import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.Tour
import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.TourState
import tech.bkdevelopment.eindhovencityexperience.presentation.notification.ContinuousNotificationService
import tech.bkdevelopment.eindhovencityexperience.presentation.tour.TourMapper
import tech.bkdevelopment.eindhovencityexperience.presentation.tour.TourViewModel
import timber.log.Timber
import javax.inject.Inject
import android.location.Location as AndroidLocation

class TourDetailPresenter @Inject constructor(
    private val view: TourDetailContract.View,
    private val navigator: TourDetailContract.Navigator,
    private val activity: Activity,
    private val getNearestStory: GetNearestStory,
    private val updateTourStatus: UpdateTourStatus,
    private val getTourById: GetTourById,
    private val tourMapper: TourMapper
) : TourDetailContract.Presenter {

    private var compositeDisposable = CompositeDisposable()

    override fun startPresenting() {
        view.tourId?.let { getTour(it) }

    }

    override fun onStartStopTourButtonClicked() {
        if (view.tour?.state != TourState.STARTED) {
            checkLocationPermission()
        } else {
            view.showStopTourDialog()
        }
    }

    override fun onMapButtonClicked() {
        navigator.navigateToMap()
    }

    override fun onDialogSettingsButtonClicked() {
        navigator.navigateToPermissionSettings()
    }

    override fun onDialogStopTourButtonClicked() {
        view.tour?.let { updateTourStatus(it, TourState.STOPPED) }
    }

    override fun stopPresenting() {
        compositeDisposable.clear()
    }

    private fun getTour(tourId: String){
        getTourById(tourId)
            .map { tourMapper.mapToTourVieModel(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onTourFetched, ::onTourFetchedFailed)
            .addTo(compositeDisposable)
    }

    private fun onTourFetched(tour: TourViewModel){
        view.tour = tour
    }

    private fun onTourFetchedFailed(throwable: Throwable){
        Timber.i(throwable)
        //todo create error state
    }

    private fun checkLocationPermission() {
        Dexter.withActivity(activity)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    getCurrentLocation()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    view.showLocationPermissionDeniedExplanationDialog()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }).check()
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
        view.tour?.id?.let {
            getNearestStory(it, currentLocation)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::onNearestStoryFetched, ::onNearestStoryFetchedFailed)
                .addTo(compositeDisposable)
        }
    }

    private fun onCurrentLocationFailed(throwable: Throwable) {
        Timber.e(throwable)
    }

    private fun onNearestStoryFetched(nearestStory: Pair<Story, Int>) {
        if (nearestStory.second < MAX_DISTANCE_TO_NEAREST_STORY) {
            view.tour?.let { updateTourStatus(it, TourState.STARTED) }
        } else {
            view.showCantStartTourDialog()
        }
    }

    private fun onNearestStoryFetchedFailed(throwable: Throwable) {
        Timber.i(throwable)
    }

    private fun updateTourStatus(tour: TourViewModel, tourState: TourState) {
        updateTourStatus.invoke(tour.id, tourState)
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    if (tourState == TourState.STARTED) {
                        view.showTourState(TourState.STARTED)
                        view.tour?.state = TourState.STARTED
                        activity.startService(
                            ContinuousNotificationService.createForegroundServiceIntent(
                                activity, tour
                            )
                        )
                        //todo navigate to mapview
                    } else {
                        view.showTourState(TourState.STOPPED)
                        view.tour?.state = TourState.STOPPED
                        activity.stopService(
                            ContinuousNotificationService.createForegroundServiceIntent(
                                activity, tour
                            )
                        )
                    }
                },
                { Timber.e("update tour status failed") }
            )
            .addTo(compositeDisposable)
    }

    companion object {

        private const val MAX_DISTANCE_TO_NEAREST_STORY = 10000
    }
}