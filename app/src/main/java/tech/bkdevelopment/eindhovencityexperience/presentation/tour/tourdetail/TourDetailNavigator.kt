package tech.bkdevelopment.eindhovencityexperience.presentation.tour.tourdetail

import android.app.Activity
import tech.bkdevelopment.eindhovencityexperience.EceApplication
import tech.bkdevelopment.eindhovencityexperience.presentation.map.MapActivity
import tech.bkdevelopment.eindhovencityexperience.presentation.tour.TourViewModel
import javax.inject.Inject

class TourDetailNavigator @Inject constructor(private val activity: Activity) :
    TourDetailContract.Navigator {

    override fun navigateToMap(tour: TourViewModel) {
        activity.startActivity(MapActivity.createIntent(activity, tour.id))
    }

    override fun navigateToPermissionSettings() {
        activity.startActivity(EceApplication.createSettingsIntent(activity))
    }
}