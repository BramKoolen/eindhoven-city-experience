package tech.bkdevelopment.eindhovencityexperience.presentation.media

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import tech.bkdevelopment.eindhovencityexperience.domain.story.model.MediaType

@Parcelize
data class MediaViewModel(
    val title: String,
    val thumbnailUrl: String?,
    val type: MediaType,
    val url: String?,
    val source: String
) : Parcelable