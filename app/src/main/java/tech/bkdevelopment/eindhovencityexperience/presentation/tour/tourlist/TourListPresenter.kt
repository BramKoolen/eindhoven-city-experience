package tech.bkdevelopment.eindhovencityexperience.presentation.tour.tourlist

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import tech.bkdevelopment.eindhovencityexperience.domain.tour.GetTours
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
    private val isServiceRunning: IsServiceRunning
) : TourListContract.Presenter {

    private var disposable: Disposable? = null

    override fun startPresenting() {
        fetchTours()
    }

    override fun onErrorStateButtonClicked() {
        view.hideErrorStateList()
        fetchTours()
    }

    override fun onTourClicked(tourViewModel: TourViewModel) {
        if(!isServiceRunning(ContinuousNotificationService::class.java) || tourViewModel.state == TourState.STARTED) {
            navigator.navigateToTourDetails(tourViewModel)
        }
        else{
            view.showCantStartTwoToursError()
        }
    }

    override fun onMoreButtonClicked() {
        navigator.navigateToAboutThisApp()
    }

    override fun stopPresenting() {
        disposable?.dispose()
    }

    private fun fetchTours() {
        disposable?.dispose()
        disposable = getTours()
            .map { tourMapper.mapToTourViewModels(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { view.showLoadingIndicator() }
            .subscribe(::onToursFetched, ::onToursFetchedFailed)
    }

    private fun onToursFetched(tours: List<TourViewModel>) {
        if (tours.isNotEmpty()) {
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
}