package tech.bkdevelopment.eindhovencityexperience.domain.tour

import com.nhaarman.mockito_kotlin.given
import io.reactivex.Completable
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import tech.bkdevelopment.eindhovencityexperience.domain.tour.data.TourRepository
import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.TourState

@RunWith(MockitoJUnitRunner::class)
class UpdateTourStatusTest {

    @Mock
    lateinit var repository: TourRepository

    @InjectMocks
    lateinit var updateTourStatus: UpdateTourStatus

    @Test
    fun `when executed, update the tour status and return completable`() {
        // Given
        given(repository.updateTourStatus("", TourState.STARTED)).willReturn(Completable.complete())

        // When
        val test = updateTourStatus("", TourState.STARTED).test()

        // Then
        test.assertComplete()
    }
}