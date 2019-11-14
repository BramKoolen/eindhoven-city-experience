package tech.bkdevelopment.eindhovencityexperience.presentation.map

import android.app.Activity
import tech.bkdevelopment.eindhovencityexperience.EceApplication
import tech.bkdevelopment.eindhovencityexperience.presentation.story.StoryViewModel
import javax.inject.Inject

class MapNavigator @Inject constructor(private val activity: Activity) : MapContract.Navigator {

    override fun navigateToStory(story: StoryViewModel) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun navigateToPermissionSettings() {
        activity.startActivity(EceApplication.createSettingsIntent(activity))
    }
}