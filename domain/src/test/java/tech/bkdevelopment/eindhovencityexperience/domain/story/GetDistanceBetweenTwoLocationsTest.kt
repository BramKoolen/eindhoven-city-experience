package tech.bkdevelopment.eindhovencityexperience.domain.story

import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetDistanceBetweenTwoLocationsTest{

    @InjectMocks
    lateinit var getDistanceBetweenTwoLocations: GetDistanceBetweenTwoLocations

    @Test
    fun `when executed, calculate distance between two coordinates and return distance`(){

        assertEquals(getDistanceBetweenTwoLocations( 51.450739,5.456509,51.45143539108606,5.453897911433518), 197)
    }
}