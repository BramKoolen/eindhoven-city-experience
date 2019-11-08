package tech.bkdevelopment.eindhovencityexperience.domain.story

import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import tech.bkdevelopment.eindhovencityexperience.domain.location.GetCurrentLocation
import tech.bkdevelopment.eindhovencityexperience.domain.location.model.Location
import tech.bkdevelopment.eindhovencityexperience.domain.story.model.Story
import tech.bkdevelopment.eindhovencityexperience.domain.tour.GetTourById
import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.Tour
import javax.inject.Inject
import kotlin.math.cos
import kotlin.math.sqrt

class GetNearestStory @Inject constructor(
    private val getTourById: GetTourById,
    private val getCurrentLocation: GetCurrentLocation
) {

    operator fun invoke(tourId: String): Observable<Pair<Story, Double>> {
        return Observables.combineLatest(
            getTourById(tourId),
            getCurrentLocation()
        )
            .map { (tour, currentLocation) -> getNearestStory(tour, currentLocation) }
    }

    private fun getNearestStory(tour: Tour, location: Location): Pair<Story, Double> {
        val notFinishedStories = tour.stories.filter { !it.completed }
        var nearestStoryPair: Pair<Story, Double> = Pair(
            tour.stories.first(),
            getDistanceInMeter(
                location.lat,
                location.long,
                tour.stories.first().lat,
                tour.stories.first().long
            )
        )
        notFinishedStories.forEach {
            val distance = getDistanceInMeter(location.lat, location.long, it.lat, it.long)
            if (distance < nearestStoryPair.second){
                nearestStoryPair = Pair(it,distance)
            }
        }
        return nearestStoryPair
    }

    private fun getDistanceInMeter(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371 // km
        val x = (lon2 - lon1) * cos((lat1 + lat2) / 2)
        val y = lat2 - lat1
        return sqrt(x * x + y * y) * r
    }
}