package tech.bkdevelopment.eindhovencityexperience.domain.tour

import com.nhaarman.mockito_kotlin.given
import io.reactivex.Observable
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import tech.bkdevelopment.eindhovencityexperience.domain.tour.data.TourRepository
import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.TextColor
import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.Tour
import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.TourState

@RunWith(MockitoJUnitRunner::class)
class GetTourByIdTest {

    @Mock
    lateinit var repository: TourRepository

    @InjectMocks
    lateinit var getTourById: GetTourById

    @Test
    fun `when executed, return tour by id`() {

        val tour = Tour("", "abc", "", "", "", emptyList(), "", "", TextColor.BLACK, null, null, "", emptyList(), TourState.TODO)
        given(repository.fetchTourById("abc")).willReturn(Observable.just(tour))

        val test = getTourById("abc").test()
        test.assertValue(tour)

    }

}