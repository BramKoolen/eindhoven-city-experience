package tech.bkdevelopment.eindhovencityexperience.domain.story

import javax.inject.Inject
import kotlin.math.*

class GetDistanceBetweenTwoLocations @Inject constructor() {

    operator fun invoke(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Int {
        val r = 6371 // Radius of the earth

        val latDistance = Math.toRadians(lat2 - lat1)
        val lonDistance = Math.toRadians(lon2 - lon1)
        val a =
            sin(latDistance / 2) * sin(latDistance / 2) + (cos(Math.toRadians(lat1)) * cos(
                Math.toRadians(lat2)
            )
                    * sin(lonDistance / 2) * sin(lonDistance / 2))
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        var distance = r.toDouble() * c * 1000.0 // convert to meters

        distance = distance.pow(2.0) + 0.0.pow(2.0)
        return sqrt(distance).roundToInt()
    }
}