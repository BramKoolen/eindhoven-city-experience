package tech.bkdevelopment.eindhovencityexperience.presentation.story

import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import tech.bkdevelopment.eindhovencityexperience.domain.story.UpdateStoryToComplete
import tech.bkdevelopment.eindhovencityexperience.presentation.media.MediaViewModel
import timber.log.Timber
import javax.inject.Inject

class StoryPresenter @Inject constructor(
    private val view: StoryContract.View,
    private val navigator: StoryContract.Navigator,
    private val updateStoryToComplete: UpdateStoryToComplete): StoryContract.Presenter {

    private var disposable: Disposable? = null

    override fun startPresenting() {
        view.story?.id?.let { updateStoryToComplete(it) }
    }

    override fun onMediaItemClicked(mediaItem: MediaViewModel) {
        navigator.navigateToAudioPlayer()
    }

    override fun stopPresenting() {
        disposable?.dispose()
    }

    private fun updateStoryToComplete(storyId: String){
        disposable?.dispose()
        disposable = updateStoryToComplete.invoke(storyId)
            .subscribeOn(Schedulers.io())
            .subscribe(
                { Timber.i("update story success") },
                { Timber.e("update story failed: $it") }
            )
    }
}