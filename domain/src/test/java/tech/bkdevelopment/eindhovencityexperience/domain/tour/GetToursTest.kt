package tech.bkdevelopment.eindhovencityexperience.domain.tour

import com.nhaarman.mockito_kotlin.given
import io.reactivex.Observable
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import tech.bkdevelopment.eindhovencityexperience.domain.tour.data.TourRepository
import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.Address
import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.Tour
import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.TourState

@RunWith(MockitoJUnitRunner::class)
class GetToursTest {

    @Mock
    lateinit var repository: TourRepository

    @InjectMocks
    lateinit var getTours: GetTours

    @Test
    fun `when executed, return a list with remote tours combined with local tours and sort on tour state en alphabet`(){
        //Given
        val address = Address("","",0.0,0.0)
        val toursUnsorted = listOf(
            Tour("", "bbb","","","",0, emptyList(),"","",address,address,"", emptyList(),TourState.STOPPED),
            Tour("", "abc","","","",0, emptyList(),"","",address,address,"", emptyList(),TourState.TODO),
            Tour("", "ccc","","","",0, emptyList(),"","",address,address,"", emptyList(),TourState.STARTED),
            Tour("", "aaa","","","",0, emptyList(),"","",address,address,"", emptyList(),TourState.TODO))

        val toursSorted = listOf(
            Tour("", "ccc","","","",0, emptyList(),"","",address,address,"", emptyList(),TourState.STARTED),
            Tour("", "bbb","","","",0, emptyList(),"","",address,address,"", emptyList(),TourState.STOPPED),
            Tour("", "aaa","","","",0, emptyList(),"","",address,address,"", emptyList(),TourState.TODO),
            Tour("", "abc","","","",0, emptyList(),"","",address,address,"", emptyList(),TourState.TODO))

        given(repository.fetchTours()).willReturn(Observable.just(toursUnsorted))

        // When -> Then
        val testObserver = getTours().test()
        testObserver.assertValue(toursSorted)
    }
}