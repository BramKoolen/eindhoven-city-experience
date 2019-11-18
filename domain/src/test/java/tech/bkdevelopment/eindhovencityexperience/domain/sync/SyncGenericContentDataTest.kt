package tech.bkdevelopment.eindhovencityexperience.domain.sync

import com.nhaarman.mockito_kotlin.given
import io.reactivex.Completable
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SyncGenericContentDataTest{

    @Mock
    lateinit var genericContentSyncManager: GenericContentSyncManager

    @InjectMocks
    lateinit var syncGenericContentData: SyncGenericContentData

    @Test
    fun `when executed, sync generic content data and return completable`(){

        // Given
        given(genericContentSyncManager.sync()).willReturn(Completable.complete())

        // When
        val test = syncGenericContentData.execute().test()

        // Then
        test.assertComplete()
    }
}