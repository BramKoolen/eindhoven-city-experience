package tech.bkdevelopment.eindhovencityexperience.domain.sync

import io.reactivex.Completable

interface GenericContentSyncManager {

    fun sync(): Completable
}