package tech.bkdevelopment.eindhovencityexperience.data.tour

import com.contentful.vault.Vault
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import tech.bkdevelopment.eindhovencityexperience.data.generic.room.RoomEceDatabase
import tech.bkdevelopment.eindhovencityexperience.data.story.room.CompletedStory
import tech.bkdevelopment.eindhovencityexperience.data.sync.contentful.ContentfulSpace
import tech.bkdevelopment.eindhovencityexperience.data.tour.contentful.TourContentfulMapper
import tech.bkdevelopment.eindhovencityexperience.data.tour.contentful.TourResponse
import tech.bkdevelopment.eindhovencityexperience.data.tour.room.RoomTourMapper
import tech.bkdevelopment.eindhovencityexperience.data.tour.room.TourStatus
import tech.bkdevelopment.eindhovencityexperience.domain.story.model.Story
import tech.bkdevelopment.eindhovencityexperience.domain.tour.data.TourRepository
import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.Tour
import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.TourState
import javax.inject.Inject

class ContentfulRoomTourRepository @Inject constructor(
    private val vault: Vault,
    private val contentfulMapper: TourContentfulMapper,
    private val roomTourMapper: RoomTourMapper,
    private val database: RoomEceDatabase
) : TourRepository {

    override fun fetchTours(): Observable<List<Tour>> {
        return Observables.combineLatest(
            fetchContentfulTours(),
            fetchRoomTours(),
            fetchRoomStories()
        )
            .map { (contentfulTours, tourStatusList, stories) ->
                setTourStatus(
                    contentfulTours,
                    tourStatusList,
                    stories
                )
            }
    }

    override fun fetchTourById(tourId: String): Observable<Tour> {
        return fetchTours().map { it.firstOrNull { it.id == tourId } }
    }

    override fun updateTourStatus(tourId: String, tourState: TourState): Completable {
        return database.roomToursDao().updateTourStatus(roomTourMapper.mapToRoomTourStatus(tourId,tourState))
    }

    private fun setTourStatus(
        contentfulTours: List<Tour>,
        tourStatusList: List<TourStatus>,
        stories: List<CompletedStory>
    ): List<Tour> {
        tourStatusList.map { tourStatus ->
            contentfulTours.find { it.id == tourStatus.id }?.state =
                convertTourStatusStringtoEnum(tourStatus.status)
        }
        contentfulTours.map { setStoryStatus(it.stories, stories) }
        return contentfulTours
    }

    private fun setStoryStatus(
        stories: List<Story>,
        completedStoryList: List<CompletedStory>
    ): List<Story> {
        completedStoryList.map { completedStory ->
            stories.find { it.id == completedStory.id }?.completed = true
        }
        return stories
    }

    private fun fetchContentfulTours(): Observable<List<Tour>> {
        return vault.observe(TourResponse::class.java)
            .all(ContentfulSpace.ENGLISH_LOCALE_NAME)
            .toList()
            .map { contentfulMapper.mapToTours(it) }
            .toObservable()
    }

    private fun fetchRoomTours(): Observable<List<TourStatus>> {
        return Observable.just(database.roomToursDao().getTourStatusList())
    }

    private fun fetchRoomStories(): Observable<List<CompletedStory>> {
        return Observable.just(database.roomStoryDao().getCompletedStoryList())
    }

    private fun convertTourStatusStringtoEnum(tourStatus: String): TourState {
        return when (tourStatus?.toLowerCase()) {
            "todo" -> TourState.TODO
            "done" -> TourState.DONE
            "started" -> TourState.STARTED
            "stopped" -> TourState.STOPPED
            else -> TourState.TODO
        }
    }
}