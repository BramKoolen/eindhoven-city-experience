package tech.bkdevelopment.eindhovencityexperience.domain.tour.model

import tech.bkdevelopment.eindhovencityexperience.domain.story.model.Story

data class Tour(
    val id: String,
    val title: String,
    val smallDescription: String,
    val thumbnailUrl: String?,
    val iconUrl: String?,
    val stories: List<Story>,
    val extraTourItemLabel: String,
    val extraTourItemIconUrl: String?,
    val progressTextColor: TextColor,
    val parkingAddress: Address?,
    val startAddress: Address?,
    val longDescription: String,
    val cafesOnThisRoute: List<Address>,
    var state: TourState
)