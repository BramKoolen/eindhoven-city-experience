package tech.bkdevelopment.eindhovencityexperience.presentation.story

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import tech.bkdevelopment.eindhovencityexperience.domain.story.model.MediaType
import tech.bkdevelopment.eindhovencityexperience.presentation.media.MediaViewModel

@Parcelize
data class StoryViewModel(
    val id: String,
    val title: String,
    val summaryText: String,
    val storyType: MediaType,
    val summary: String,
    val description: String,
    val mediaItems: List<MediaViewModel>?,
    val lat: Double,
    val long: Double,
    var completed: Boolean,
    var distanceToCurrentLocation: Int
) : Parcelable