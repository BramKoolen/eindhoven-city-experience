package tech.bkdevelopment.eindhovencityexperience.domain.tour

import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.Tour
import javax.inject.Inject
import kotlin.math.roundToInt

class GetRemainingTourTime @Inject constructor(private val getRemainingTourDistance: GetRemainingTourDistance) {

    operator fun invoke(tour: Tour): Int {
        return (getRemainingTourDistance(tour).toDouble() / METERS_PER_HOUR * 60).roundToInt()
    }

    companion object {

        private const val METERS_PER_HOUR = 4000
    }
}