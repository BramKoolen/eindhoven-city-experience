package tech.bkdevelopment.eindhovencityexperience.presentation.story

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import tech.bkdevelopment.eindhovencityexperience.domain.story.model.StoryType

@Parcelize
data class StoryViewModel(
    val id: String,
    val title: String,
    val summaryText: String,
    val storyType: StoryType,
    val summary: String,
    val description: String,
    val mediaUrlList: List<String>?,
    val lat: Double,
    val long: Double,
    var completed: Boolean,
    var distanceToCurrentLocation: Int
) : Parcelable