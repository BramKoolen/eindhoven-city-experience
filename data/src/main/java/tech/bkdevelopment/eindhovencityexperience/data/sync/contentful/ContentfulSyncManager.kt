package tech.bkdevelopment.eindhovencityexperience.data.sync.contentful

import android.content.Context
import com.contentful.vault.SyncCallback
import com.contentful.vault.SyncConfig
import com.contentful.vault.SyncResult
import com.contentful.vault.Vault
import io.reactivex.Completable
import tech.bkdevelopment.eindhovencityexperience.data.generic.dagger.DataContext
import tech.bkdevelopment.eindhovencityexperience.domain.sync.GenericContentSyncManager
import javax.inject.Inject

class ContentfulSyncManager @Inject constructor(
    @DataContext private val context: Context,
    private val syncConfig: SyncConfig
) : GenericContentSyncManager {

    override fun sync(): Completable {
        return Completable.create {
            Vault.with(context, ContentfulSpace::class.java)
                .requestSync(syncConfig, object : SyncCallback() {
                    override fun onResult(result: SyncResult) {
                        if (result.isSuccessful) {
                            it.onComplete()
                        } else {
                            it.onError(result.error())
                        }
                    }
                })
        }
    }
}