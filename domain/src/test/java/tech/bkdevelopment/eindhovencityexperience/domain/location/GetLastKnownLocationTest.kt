package tech.bkdevelopment.eindhovencityexperience.domain.location

import com.nhaarman.mockito_kotlin.given
import io.reactivex.Observable
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import tech.bkdevelopment.eindhovencityexperience.domain.location.data.LocationProvider
import tech.bkdevelopment.eindhovencityexperience.domain.location.model.Location

@RunWith(MockitoJUnitRunner::class)
class GetLastKnownLocationTest{

    @Mock
    lateinit var locationProvider: LocationProvider

    @InjectMocks
    lateinit var getLastKnownLocation: GetLastKnownLocation

    @Test
    fun `when executed, return te last known location`(){
        // Given
        val location = Location(1.1,1.1)
        given(locationProvider.getLastKnownLocation()).willReturn(Observable.just(location))

        // When -> Then
        val test = getLastKnownLocation().test()
        test.assertValue(location)
    }
}
