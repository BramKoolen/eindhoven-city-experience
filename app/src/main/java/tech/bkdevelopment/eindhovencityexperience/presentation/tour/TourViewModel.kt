package tech.bkdevelopment.eindhovencityexperience.presentation.tour

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.TextColor
import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.TourState
import tech.bkdevelopment.eindhovencityexperience.presentation.story.StoryViewModel

@Parcelize
data class TourViewModel(
    val id: String,
    val title: String,
    val smallDescription: String,
    val thumbnailUrl: String?,
    val iconUrl: String?,
    val remainingTourTimeInMinutes: Int,
    val remainingDistanceInMeters: Int,
    var stories: List<StoryViewModel>,
    val extraTourItemLabel: String,
    val extraTourItemIconUrl: String?,
    val textColorProgress: TextColor,
    val parkingAddress: AddressViewModel?,
    val startAddress: AddressViewModel?,
    val longDescription: String,
    val cafesOnThisRoute: List<AddressViewModel>,
    var state: TourState
) : Parcelable