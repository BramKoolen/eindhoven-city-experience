package tech.bkdevelopment.eindhovencityexperience.domain.story

import io.reactivex.Observable
import tech.bkdevelopment.eindhovencityexperience.domain.location.model.Location
import tech.bkdevelopment.eindhovencityexperience.domain.story.model.Story
import tech.bkdevelopment.eindhovencityexperience.domain.tour.GetTourById
import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.Tour
import javax.inject.Inject
import kotlin.math.*

class GetNearestStory @Inject constructor(
    private val getTourById: GetTourById,
    private val getDistanceBetweenTwoLocations: GetDistanceBetweenTwoLocations) {

    operator fun invoke(tourId: String, currentLocation: Location): Observable<Pair<Story, Int>> {
        return getTourById(tourId)
            .map { getNearestStory(it, currentLocation) }
    }

    private fun getNearestStory(tour: Tour, location: Location): Pair<Story, Int> {
        val notFinishedStories = tour.stories.filter { !it.completed }
        var nearestStoryPair: Pair<Story, Int> = Pair(
            tour.stories.first(),
            getDistanceBetweenTwoLocations(
                location.lat,
                location.long,
                tour.stories.first().lat,
                tour.stories.first().long
            )
        )
        notFinishedStories.forEach {
            val distance = getDistanceBetweenTwoLocations(location.lat, location.long, it.lat, it.long)
            if (distance < nearestStoryPair.second){
                nearestStoryPair = Pair(it,distance)
            }
        }
        return nearestStoryPair
    }
}