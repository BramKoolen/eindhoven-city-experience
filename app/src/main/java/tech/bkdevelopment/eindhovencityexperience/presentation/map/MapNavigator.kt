package tech.bkdevelopment.eindhovencityexperience.presentation.map

import android.app.Activity
import tech.bkdevelopment.eindhovencityexperience.EceApplication
import tech.bkdevelopment.eindhovencityexperience.presentation.story.StoryActivity
import tech.bkdevelopment.eindhovencityexperience.presentation.story.StoryViewModel
import javax.inject.Inject

class MapNavigator @Inject constructor(private val activity: Activity) : MapContract.Navigator {

    override fun navigateToStory(story: StoryViewModel) {
        activity.startActivity(StoryActivity.createIntent(activity, story))
    }

    override fun navigateToPermissionSettings() {
        activity.startActivity(EceApplication.createSettingsIntent(activity))
    }
}