package tech.bkdevelopment.eindhovencityexperience.presentation.tour.tourlist

interface TourListContract {

    interface View {

        fun showLoadingIndicator()
        fun hideLoadingIndicator()
        fun showErrorStateList()
        fun hideErrorStateList()
        fun showTours(tourViewModels: List<TourViewModel>)
    }

    interface Presenter {

        fun startPresenting()
        fun onErrorStateButtonClicked()
        fun onTourClicked(tourViewModel: TourViewModel)
        fun onMoreButtonClicked()
        fun stopPresenting()
    }

    interface Navigator {

        fun navigateToAboutThisApp()
        fun navigateToTourDetails(tourViewModel: TourViewModel)
    }
}