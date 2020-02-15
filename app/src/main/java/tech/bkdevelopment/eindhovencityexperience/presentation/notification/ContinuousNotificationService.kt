package tech.bkdevelopment.eindhovencityexperience.presentation.notification

import android.content.Context
import android.content.Intent
import android.os.IBinder
import dagger.android.DaggerService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import tech.bkdevelopment.eindhovencityexperience.R
import tech.bkdevelopment.eindhovencityexperience.domain.location.ObserveCurrentLocation
import tech.bkdevelopment.eindhovencityexperience.domain.location.model.Location
import tech.bkdevelopment.eindhovencityexperience.domain.story.GetNearestStory
import tech.bkdevelopment.eindhovencityexperience.domain.story.model.Story
import tech.bkdevelopment.eindhovencityexperience.presentation.notification.continousnotification.ContinuousNotification
import tech.bkdevelopment.eindhovencityexperience.presentation.notification.continousnotification.ContinuousNotificationBuilder
import tech.bkdevelopment.eindhovencityexperience.presentation.notification.oudsidegeofencearea.OudsideGeofenceAreaNotificationBuilder
import tech.bkdevelopment.eindhovencityexperience.presentation.story.StoryMapper
import tech.bkdevelopment.eindhovencityexperience.presentation.tour.TourViewModel
import timber.log.Timber
import javax.inject.Inject

class ContinuousNotificationService : DaggerService() {

    var tour: TourViewModel? = null

    @Inject
    lateinit var continuousNotificationBuilder: ContinuousNotificationBuilder

    @Inject
    lateinit var oudsideGeofenceAreaNotificationBuilder: OudsideGeofenceAreaNotificationBuilder

    @Inject
    lateinit var getNearestStory: GetNearestStory

    @Inject
    lateinit var storyMapper: StoryMapper

    @Inject
    lateinit var observeCurrentLocation: ObserveCurrentLocation

    private var compositeDisposable = CompositeDisposable()

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        tour = intent?.getParcelableExtra(TOUR_EXTRA) as TourViewModel?
        if (tour != null) {
            startForeground(
                ONGOING_NOTIFICATION_ID,
                continuousNotificationBuilder.createNotification(
                    (this),
                    ContinuousNotification(
                        tour!!.title,
                        getString(R.string.continuous_notification_body_placeholder),
                        false,
                        tour!!.id,
                        tour!!.stories[0]
                    )
                )
            )
            observeCurrentLocation()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun observeCurrentLocation() {
        observeCurrentLocation.invoke()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { onCurrentLocationUpdateFetched(it) }
            .addTo(compositeDisposable)

    }

    private fun onCurrentLocationUpdateFetched(currentLocation: Location) {
        tour?.id?.let {
            getNearestStory(it, currentLocation)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::onNearestStoryFetched, ::onNearestStoryFetchedFailed)
                .addTo(compositeDisposable)
        }
    }

    private fun onNearestStoryFetched(nearestStory: Pair<Story, Int>) {
        when {
            //nearestStory.second in WARNING_DISTANCE_TO_STORY until MAX_DISTANCE_TO_STORY ->  oudsideGeofenceAreaNotificationBuilder.createNotification(this, tour?.id!!)
            nearestStory.second >= MAX_DISTANCE_TO_STORY -> onDestroy()
            else -> mapToContinuousNotification(nearestStory)?.let {
                continuousNotificationBuilder.updateNotification(
                    this, ONGOING_NOTIFICATION_ID,
                    it
                )
            }
        }
    }

    private fun onNearestStoryFetchedFailed(throwable: Throwable) {
        Timber.i(throwable)
    }

    private fun mapToContinuousNotification(nearestStory: Pair<Story, Int>): ContinuousNotification? {
        val notificationBody: String
        val isUnlocked: Boolean
        if (nearestStory.second <= DISTANCE_TO_UNLOCK_STORY) {
            notificationBody = getString(R.string.continuous_notification_body_unlocked)
            isUnlocked = true
        } else {
            notificationBody = getString(
                R.string.continuous_notification_body_locked,
                nearestStory.second.toString()
            )
            isUnlocked = false
        }
        return tour?.title?.let {
            ContinuousNotification(
                it,
                notificationBody,
                isUnlocked,
                tour!!.id,
                storyMapper.mapToStoryViewModel(nearestStory.first)
            )
        }
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        stopForeground(true)
        super.onDestroy()
    }

    companion object {

        private const val ONGOING_NOTIFICATION_ID = 12345
        private const val DISTANCE_TO_UNLOCK_STORY = 10
        private const val MAX_DISTANCE_TO_STORY = 10000
        private const val WARNING_DISTANCE_TO_STORY = 2500

        const val TOUR_EXTRA = "intentTourExtra"

        fun createForegroundServiceIntent(context: Context, tour: TourViewModel?): Intent {
            return Intent(context, ContinuousNotificationService::class.java).apply {
                putExtra(TOUR_EXTRA, tour)
            }
        }
    }
}