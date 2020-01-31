package tech.bkdevelopment.eindhovencityexperience.presentation.tour.tourlist

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import tech.bkdevelopment.eindhovencityexperience.domain.sync.SyncGenericContentData
import tech.bkdevelopment.eindhovencityexperience.domain.tour.GetTours
import tech.bkdevelopment.eindhovencityexperience.domain.tour.UpdateTourStatus
import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.TourState
import tech.bkdevelopment.eindhovencityexperience.generic.service.IsServiceRunning
import tech.bkdevelopment.eindhovencityexperience.presentation.notification.ContinuousNotificationService
import tech.bkdevelopment.eindhovencityexperience.presentation.tour.TourMapper
import tech.bkdevelopment.eindhovencityexperience.presentation.tour.TourViewModel
import timber.log.Timber
import javax.inject.Inject

class TourListPresenter @Inject constructor(
    private val view: TourListContract.View,
    private val navigator: TourListContract.Navigator,
    private val getTours: GetTours,
    private val tourMapper: TourMapper,
    private val isServiceRunning: IsServiceRunning,
    private val syncGenericContentData: SyncGenericContentData,
    private val updateTourStatus: UpdateTourStatus
) : TourListContract.Presenter {

    private var compositeDisposable = CompositeDisposable()

    override fun startPresenting() {
        fetchTours()
    }

    override fun onErrorStateButtonClicked() {
        view.hideErrorStateList()
        syncContentfulData()
    }

    override fun onTourClicked(tourViewModel: TourViewModel) {
        if (!isServiceRunning(ContinuousNotificationService::class.java) || tourViewModel.state == TourState.STARTED) {
            navigator.navigateToTourDetails(tourViewModel, false)
        } else {
            view.showCantStartTwoToursError()
        }
    }

    override fun onMoreButtonClicked() {
        navigator.navigateToAboutThisApp()
    }

    override fun stopPresenting() {
        compositeDisposable.clear()
    }

    private fun fetchTours() {
        getTours()
            .map { tourMapper.mapToTourViewModels(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { view.showLoadingIndicator() }
            .subscribe(::onToursFetched, ::onToursFetchedFailed)
            .addTo(compositeDisposable)
    }

    private fun onToursFetched(tours: List<TourViewModel>) {
        if (tours.isNotEmpty()) {
            tours.map {
                if (it.state == TourState.STARTED && !isServiceRunning(ContinuousNotificationService::class.java)) {
                    it.state = TourState.STOPPED
                    updateTourStates(it)
                }
            }
            view.hideLoadingIndicator()
            view.hideErrorStateList()
            view.showTours(tours)
        } else {
            onToursFetchedFailed(null)
        }
    }

    private fun onToursFetchedFailed(throwable: Throwable?) {
        view.hideLoadingIndicator()
        view.showErrorStateList()
        Timber.e(throwable)
    }

    private fun updateTourStates(tourViewModel: TourViewModel) {
        updateTourStatus.invoke(tourViewModel.id, tourViewModel.state)
            .subscribeOn(Schedulers.io())
            .subscribe(
                { Timber.i("update tour state success") },
                { Timber.e("update tour state failed: $it") }
            )
            .addTo(compositeDisposable)
    }

    private fun syncContentfulData() {
        syncGenericContentData.execute()
            .subscribeOn(Schedulers.io())
            .subscribe(
                { fetchTours() },
                { Timber.e("content update failed: $it") }
            )
            .addTo(compositeDisposable)
    }
}