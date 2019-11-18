package tech.bkdevelopment.eindhovencityexperience.domain.story

import com.nhaarman.mockito_kotlin.given
import io.reactivex.Observable
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import tech.bkdevelopment.eindhovencityexperience.domain.location.model.Location
import tech.bkdevelopment.eindhovencityexperience.domain.story.model.MediaType
import tech.bkdevelopment.eindhovencityexperience.domain.story.model.Story
import tech.bkdevelopment.eindhovencityexperience.domain.tour.GetTourById
import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.Tour
import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.TourState

@RunWith(MockitoJUnitRunner::class)
class GetNearestStoryTest {

    @Mock
    lateinit var getTourById: GetTourById

    @Mock
    lateinit var getDistanceBetweenTwoLocations: GetDistanceBetweenTwoLocations

    @InjectMocks
    lateinit var getNearestStory: GetNearestStory

    @Test
    fun `when executed, calculate the nearest story from all unfinished stories of a tour return the story and distance as observable pair`() {
        // Given
        val nearestStory = Story("s1", "", "", MediaType.TEXT, "", "", emptyList(), 51.4523512, 5.4507612, false)
        val tour = Tour(
            "",
            "abc",
            "",
            "",
            "",
            listOf(
                Story("s1", "", "", MediaType.TEXT, "", "", emptyList(), 51.4523512, 5.4507612, false),
                Story("s2", "", "", MediaType.TEXT, "", "", emptyList(), 51.4376414, 5.4611025, false),
                Story("s3", "", "", MediaType.TEXT, "", "", emptyList(),51.443273,5.5013126,false)
            ),
            "",
            "",
            null,
            null,
            "",
            emptyList(),
            TourState.TODO
        )
        given(getTourById("abc")).willReturn(Observable.just(tour))

        val test = getNearestStory("abc", Location(51.4508688,5.4531393)).test()
        test.assertValue(Pair(nearestStory,0))
    }
}