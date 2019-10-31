package tech.bkdevelopment.eindhovencityexperience.domain.sync

import io.reactivex.Completable
import javax.inject.Inject

class SyncGenericContentData @Inject constructor(private val genericContentSyncManager: GenericContentSyncManager) {

    fun execute(): Completable {
        return genericContentSyncManager.sync()
    }
}