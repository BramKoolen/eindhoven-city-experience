package tech.bkdevelopment.eindhovencityexperience.presentation.tour.tourlist

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
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
    val remainingDistance: Int,
    val stories: List<StoryViewModel>,
    val extraTourItemLabel: String,
    val extraTourItemIconUrl: String?,
    val parkingAddress: AddressViewModel?,
    val startAddress: AddressViewModel?,
    val longDescription: String,
    val cafesOnThisRoute: List<AddressViewModel?>,
    val state: TourState
) : Parcelable