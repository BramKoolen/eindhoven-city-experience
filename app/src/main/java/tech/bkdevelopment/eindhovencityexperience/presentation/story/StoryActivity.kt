package tech.bkdevelopment.eindhovencityexperience.presentation.story

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_story.*
import tech.bkdevelopment.eindhovencityexperience.R
import javax.inject.Inject

class StoryActivity : DaggerAppCompatActivity(), StoryContract.View {

    @Inject
    lateinit var presenter: StoryContract.Presenter

    override val story: StoryViewModel?
        get() = intent.getParcelableExtra(STORY_EXTRA) as StoryViewModel

    private val storyMediaAdapter by lazy {
        StoryMediaAdapter().apply {
            onMediaItemClicked = presenter::onMediaItemClicked
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story)

        setSupportActionBar(storyToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        story?.mediaItems?.let {
            storyHeaderSlider.sliderAdapter = storyMediaAdapter
            storyMediaAdapter.mediaViewModels = story?.mediaItems!!
        }

        fillStoryLayout()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        presenter.startPresenting()
    }

    override fun onStop() {
        presenter.stopPresenting()
        super.onStop()
    }

    override fun onBackPressed() {
        if (intent.getBooleanExtra(LAUNCH_FROM_NOTIFICATION_EXTRA, false) && intent.getStringExtra(TOUR_ID_EXTRA) != null) {
            presenter.onBackPressedLaunchedFromNotification(intent.getStringExtra(TOUR_ID_EXTRA),true)
        } else {
            super.onBackPressed()
        }
    }

    private fun fillStoryLayout() {
        storyTitle.text = story?.title
        storyDescription.text = story?.summary
        storyBody.text = story?.description
    }

    companion object {

        private const val TOUR_ID_EXTRA = "intentTourIdExtra"
        private const val STORY_EXTRA = "intentStoryExtra"
        private const val LAUNCH_FROM_NOTIFICATION_EXTRA = "intentLaunchFromNotificationExtra"

        fun createIntent(
            context: Context,
            tourId: String?,
            story: StoryViewModel,
            launchFromNotification: Boolean
        ): Intent {
            return Intent(context, StoryActivity::class.java).apply {
                putExtra(TOUR_ID_EXTRA, tourId)
                putExtra(STORY_EXTRA, story)
                putExtra(LAUNCH_FROM_NOTIFICATION_EXTRA, launchFromNotification)
            }
        }
    }
}
