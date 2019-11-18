package tech.bkdevelopment.eindhovencityexperience.presentation.story

import android.app.Activity
import tech.bkdevelopment.eindhovencityexperience.presentation.map.MapActivity
import javax.inject.Inject

class StoryNavigator @Inject constructor(private val activity: Activity): StoryContract.Navigator {

    override fun navigateToMap(tourId: String, launchedFromNotification: Boolean) {
        activity.startActivity(MapActivity.createIntent(activity,tourId,launchedFromNotification))
    }

    override fun navigateToAudioPlayer() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun navigateToPhotoViewer() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun navigateToVideoPlayer() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}