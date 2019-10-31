package tech.bkdevelopment.eindhovencityexperience.presentation.tour.tourlist

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import tech.bkdevelopment.eindhovencityexperience.domain.tour.GetTours
import timber.log.Timber
import javax.inject.Inject

class TourListPresenter @Inject constructor(
    private val view: TourListContract.View,
    private val navigator: TourListContract.Navigator,
    private val getTours: GetTours,
    private val tourMapper: TourMapper
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
        navigator.navigateToTourDetails(tourViewModel)
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
            .doOnSubscribe { view.showLoadingPlaceHolder() }
            .subscribe(::onToursFetched, ::onToursFetchedFailed)
    }

    private fun onToursFetched(tours: List<TourViewModel>) {
        view.hideLoadingPlaceHolder()
        view.hideErrorStateList()
        view.showTours(tours)
    }

    private fun onToursFetchedFailed(throwable: Throwable) {
        view.hideLoadingPlaceHolder()
        view.showErrorStateList()
        Timber.e(throwable)
    }
}