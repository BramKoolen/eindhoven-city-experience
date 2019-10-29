package tech.bkdevelopment.eindhovencityexperience.presentation.splash

import android.app.Activity
import tech.bkdevelopment.eindhovencityexperience.presentation.MainActivity
import javax.inject.Inject

class SplashNavigator @Inject constructor(private val activity: Activity) :
    SplashContract.Navigator {

    override fun navigateToTours() {
        activity.startActivity(MainActivity.createIntent(activity))
    }

    override fun close() {
        activity.finish()
    }
}