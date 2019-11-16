package tech.bkdevelopment.eindhovencityexperience.presentation.media

import tech.bkdevelopment.eindhovencityexperience.domain.story.model.Media
import javax.inject.Inject

class MediaMapper @Inject constructor() {

    fun mapToMediaViewModels(mediaItems: List<Media>): List<MediaViewModel> {
        return mediaItems.map { mapToMediaViewModel(it) }
    }

    private fun mapToMediaViewModel(mediaItem: Media): MediaViewModel {
        return MediaViewModel(
            mediaItem.title,
            mediaItem.thumbnailUrl,
            mediaItem.type,
            mediaItem.url,
            mediaItem.source
        )
    }
}
