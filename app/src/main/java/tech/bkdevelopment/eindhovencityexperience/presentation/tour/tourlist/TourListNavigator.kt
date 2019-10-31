package tech.bkdevelopment.eindhovencityexperience.presentation.tour.tourlist

import android.app.Activity
import javax.inject.Inject

class TourListNavigator @Inject constructor(private val activity: Activity) :
    TourListContract.Navigator {

    override fun navigateToAboutThisApp() {
        //todo
    }

    override fun navigateToTourDetails(tourViewModel: TourViewModel) {
        //todo
    }
}