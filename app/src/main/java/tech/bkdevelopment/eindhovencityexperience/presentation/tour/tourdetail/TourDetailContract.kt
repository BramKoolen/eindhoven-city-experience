package tech.bkdevelopment.eindhovencityexperience.presentation.tour.tourdetail

import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.TourState
import tech.bkdevelopment.eindhovencityexperience.presentation.tour.TourViewModel

interface TourDetailContract {

    interface View {

        val tourId: String?
        var tour: TourViewModel?

        fun showTourState(tourState: TourState)
        fun showLocationPermissionDeniedExplanationDialog()
        fun showCantStartTourDialog()
        fun showStopTourDialog()
    }

    interface Presenter {

        fun startPresenting()
        fun onStartStopTourButtonClicked()
        fun onMapButtonClicked()
        fun onDialogSettingsButtonClicked()
        fun onDialogStopTourButtonClicked()
        fun stopPresenting()
    }

    interface Navigator {

        fun navigateToMap()
        fun navigateToPermissionSettings()
    }
}