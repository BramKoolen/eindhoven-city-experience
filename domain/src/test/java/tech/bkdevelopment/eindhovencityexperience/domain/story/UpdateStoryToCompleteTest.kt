package tech.bkdevelopment.eindhovencityexperience.domain.story

import com.nhaarman.mockito_kotlin.given
import io.reactivex.Completable
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import tech.bkdevelopment.eindhovencityexperience.domain.story.data.StoryRepository

@RunWith(MockitoJUnitRunner::class)
class UpdateStoryToCompleteTest {

    @Mock
    lateinit var repository: StoryRepository

    @InjectMocks
    lateinit var updateStoryToComplete: UpdateStoryToComplete

    @Test
    fun `when executed, update story to complete and return completable`() {
        // Given
        given(repository.updateStoryStatusToComplete("abc")).willReturn(Completable.complete())

        // When
        val test = updateStoryToComplete.invoke("abc").test()

        // Then
        test.assertComplete()
    }
}