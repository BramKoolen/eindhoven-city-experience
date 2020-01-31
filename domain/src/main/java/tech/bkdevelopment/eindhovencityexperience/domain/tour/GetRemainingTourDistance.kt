package tech.bkdevelopment.eindhovencityexperience.domain.tour

import tech.bkdevelopment.eindhovencityexperience.domain.story.GetDistanceBetweenTwoLocations
import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.Tour
import javax.inject.Inject

class GetRemainingTourDistance @Inject constructor(private val getDistanceBetweenTwoLocations: GetDistanceBetweenTwoLocations) {

    operator fun invoke(tour: Tour): Int {
        val unfinishedStories = tour.stories.filter { !it.completed }
        if (unfinishedStories.size >= 2) {
            var remainingTourDistance = 0
            var counter = 0
            repeat(unfinishedStories.size) {
                if (counter != unfinishedStories.lastIndex ) {
                    remainingTourDistance = getDistanceBetweenTwoLocations(
                        unfinishedStories[counter].lat, unfinishedStories[counter].long,
                        unfinishedStories[counter + 1].lat, unfinishedStories[counter + 1].long
                    )
                    counter++
                }
            }
            return remainingTourDistance
        } else {
            return 0
        }
    }
}