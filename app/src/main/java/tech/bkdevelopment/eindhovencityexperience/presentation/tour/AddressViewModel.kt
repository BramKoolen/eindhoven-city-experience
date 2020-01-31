package tech.bkdevelopment.eindhovencityexperience.presentation.tour

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddressViewModel(
    val name: String,
    val lat: Double,
    val long: Double
) : Parcelable