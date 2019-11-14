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
        fun checkLocationPermissions()
    }

    interface Presenter {

        fun startPresenting()
        fun onStartStopTourButtonClicked()
        fun onMapButtonClicked()
        fun onDialogSettingsButtonClicked()
        fun onDialogStopTourButtonClicked()
        fun onLocationPermissionGranted()
        fun onLocationPermissionDenied()
        fun stopPresenting()
    }

    interface Navigator {

        fun navigateToMap(tour: TourViewModel)
        fun navigateToPermissionSettings()
    }
}