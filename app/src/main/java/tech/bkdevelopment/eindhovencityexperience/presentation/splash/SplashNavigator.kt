package tech.bkdevelopment.eindhovencityexperience.presentation.splash

import android.app.Activity
import tech.bkdevelopment.eindhovencityexperience.presentation.tour.tourlist.TourListActivity
import javax.inject.Inject

class SplashNavigator @Inject constructor(private val activity: Activity) :
    SplashContract.Navigator {

    override fun navigateToTours() {
        activity.startActivity(TourListActivity.createIntent(activity))
    }

    override fun close() {
        activity.finish()
    }
}