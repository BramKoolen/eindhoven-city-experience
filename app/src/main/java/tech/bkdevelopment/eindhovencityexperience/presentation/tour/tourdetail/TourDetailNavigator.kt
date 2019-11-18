package tech.bkdevelopment.eindhovencityexperience.presentation.tour.tourdetail

import android.app.Activity
import tech.bkdevelopment.eindhovencityexperience.EceApplication
import tech.bkdevelopment.eindhovencityexperience.presentation.map.MapActivity
import tech.bkdevelopment.eindhovencityexperience.presentation.tour.TourViewModel
import tech.bkdevelopment.eindhovencityexperience.presentation.tour.tourlist.TourListActivity
import javax.inject.Inject

class TourDetailNavigator @Inject constructor(private val activity: Activity) :
    TourDetailContract.Navigator {

    override fun navigateToTourList() {
        activity.startActivity(TourListActivity.createIntent(activity))
    }

    override fun navigateToMap(tour: TourViewModel, launchedFromNotification: Boolean) {
        activity.startActivity(MapActivity.createIntent(activity, tour.id, launchedFromNotification))
    }

    override fun navigateToPermissionSettings() {
        activity.startActivity(EceApplication.createSettingsIntent(activity))
    }
}