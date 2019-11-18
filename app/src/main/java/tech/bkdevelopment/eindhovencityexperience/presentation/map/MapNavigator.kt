package tech.bkdevelopment.eindhovencityexperience.presentation.map

import android.app.Activity
import tech.bkdevelopment.eindhovencityexperience.EceApplication
import tech.bkdevelopment.eindhovencityexperience.presentation.story.StoryActivity
import tech.bkdevelopment.eindhovencityexperience.presentation.story.StoryViewModel
import tech.bkdevelopment.eindhovencityexperience.presentation.tour.tourdetail.TourDetailActivity
import javax.inject.Inject

class MapNavigator @Inject constructor(private val activity: Activity) : MapContract.Navigator {

    override fun navigateToTourDetail(tourId: String, launchedFromNotification: Boolean) {
        activity.startActivity(TourDetailActivity.createIntent(activity,tourId,launchedFromNotification))
    }

    override fun navigateToStory(story: StoryViewModel, launchedFromNotification: Boolean) {
        activity.startActivity(StoryActivity.createIntent(activity,null, story,launchedFromNotification))
    }

    override fun navigateToPermissionSettings() {
        activity.startActivity(EceApplication.createSettingsIntent(activity))
    }
}