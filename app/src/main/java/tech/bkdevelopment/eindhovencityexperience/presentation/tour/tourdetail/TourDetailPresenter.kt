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
import tech.bkdevelopment.eindhovencityexperience.domain.story.GetNearestStory
import tech.bkdevelopment.eindhovencityexperience.domain.story.model.Story
import tech.bkdevelopment.eindhovencityexperience.domain.tour.UpdateTourStatus
import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.TourState
import tech.bkdevelopment.eindhovencityexperience.presentation.tour.TourViewModel
import timber.log.Timber
import javax.inject.Inject

class TourDetailPresenter @Inject constructor(
    private val view: TourDetailContract.View,
    private val navigator: TourDetailContract.Navigator,
    private val activity: Activity,
    private val getNearestStory: GetNearestStory,
    private val updateTourStatus: UpdateTourStatus
) : TourDetailContract.Presenter {

    private var compositeDisposable = CompositeDisposable()

    override fun startPresenting() {
        if (view.tour?.state == TourState.STARTED) {
            view.showTourState(true)
        }
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

    private fun checkLocationPermission() {
        Dexter.withActivity(activity)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    view.tour?.id?.let { startTour(it) }
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

    private fun startTour(storyId: String) {
        getNearestStory(storyId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onNearestStoryFetched, ::onNearestStoryFetchedFailed)
            .addTo(compositeDisposable)
    }

    private fun onNearestStoryFetched(nearestStory: Pair<Story, Double>) {
        if (nearestStory.second < MAX_DISTANCE_TO_NEAREST_STORY) {
            view.tour?.let { updateTourStatus(it, TourState.STARTED) }
        }else{
            Timber.i("todo")
            //todo show to far from nearest story dialog
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
                        view.showTourState(true)
                        view.tour?.state = TourState.STARTED
                        //todo show contious notification
                        //todo navigate to mapview
                    } else {
                        view.showTourState(false)
                        view.tour?.state = TourState.STOPPED
                    }
                },
                { Timber.e("update tour status failed") }
            )
            .addTo(compositeDisposable)
    }

    companion object {

        private const val MAX_DISTANCE_TO_NEAREST_STORY = 10000.0
    }
}