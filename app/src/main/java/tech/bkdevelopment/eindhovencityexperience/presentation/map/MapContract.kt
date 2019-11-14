package tech.bkdevelopment.eindhovencityexperience.presentation.map

import tech.bkdevelopment.eindhovencityexperience.domain.location.model.Location
import tech.bkdevelopment.eindhovencityexperience.presentation.story.StoryViewModel
import tech.bkdevelopment.eindhovencityexperience.presentation.tour.TourViewModel

interface MapContract {

    interface View {

        val tourId: String?
        var tour: TourViewModel?

        fun updateStories()
        fun zoomToLocation(location: Location)
        fun updateSelectedMarker(position: Int)
        fun showNotCloseEnoughToStoryDialog()
        fun showTourFinishedDialog()
        fun showStartTourDialog()
        fun checkLocationPermissions()
        fun showLocationPermissionDeniedExplanationDialog()
        fun updateCurrentLocationButton(visible: Boolean)
    }

    interface Presenter {

        fun startPresenting()
        fun onStoryClicked(story: StoryViewModel)
        fun onCurrentLocationButtonClicked()
        fun onStoryCardFocusChanged(position: Int)
        fun onStartTourClicked()
        fun onLocationPermissionGranted()
        fun onLocationPermissionDenied()
        fun onDialogSettingsButtonClicked()
        fun stopPresenting()
    }

    interface Navigator {

        fun navigateToStory(story: StoryViewModel)
        fun navigateToPermissionSettings()
    }
}