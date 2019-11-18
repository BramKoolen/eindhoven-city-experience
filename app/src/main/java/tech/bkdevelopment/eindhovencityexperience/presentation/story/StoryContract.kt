package tech.bkdevelopment.eindhovencityexperience.presentation.story

import tech.bkdevelopment.eindhovencityexperience.presentation.media.MediaViewModel

interface StoryContract {

    interface View {

        val story: StoryViewModel?
    }

    interface Presenter {

        fun startPresenting()
        fun onMediaItemClicked(mediaItem: MediaViewModel)
        fun stopPresenting()
    }

    interface Navigator {

        fun navigateToAudioPlayer()
        fun navigateToPhotoViewer()
        fun navigateToVideoPlayer()
    }
}