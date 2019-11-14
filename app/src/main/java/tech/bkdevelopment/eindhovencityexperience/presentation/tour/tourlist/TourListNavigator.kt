package tech.bkdevelopment.eindhovencityexperience.presentation.tour.tourlist

import android.app.Activity
import tech.bkdevelopment.eindhovencityexperience.presentation.tour.TourViewModel
import tech.bkdevelopment.eindhovencityexperience.presentation.tour.tourdetail.TourDetailActivity
import javax.inject.Inject

class TourListNavigator @Inject constructor(private val activity: Activity) :
    TourListContract.Navigator {

    override fun navigateToAboutThisApp() {
        //TODO
    }

    override fun navigateToTourDetails(tourViewModel: TourViewModel) {
        activity.startActivity(TourDetailActivity.createIntent(activity, tourViewModel.id))
    }
}