package tech.bkdevelopment.eindhovencityexperience.presentation.tour.tourdetail

import android.app.Activity
import javax.inject.Inject

class TourDetailNavigator @Inject constructor(private val activity: Activity) :
    TourDetailContract.Navigator {

    override fun navigateToMap() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun navigateToPermissionSettings() {
        activity.startActivity(TourDetailActivity.createSettingsIntent(activity))
    }
}