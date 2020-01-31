package tech.bkdevelopment.eindhovencityexperience.data.sync.contentful

import android.content.Context
import com.contentful.java.cda.CDAClient
import com.contentful.vault.SyncConfig
import com.contentful.vault.Vault
import dagger.Binds
import dagger.Module
import dagger.Provides
import tech.bkdevelopment.eindhovencityexperience.data.BuildConfig
import tech.bkdevelopment.eindhovencityexperience.data.generic.dagger.DataContext
import tech.bkdevelopment.eindhovencityexperience.domain.sync.GenericContentSyncManager
import javax.inject.Singleton

@Module(includes = [ContentfulModule.Bindings::class])
class ContentfulModule {

    @Provides
    @Singleton
    fun provideCDAClient(): CDAClient {
        return CDAClient
            .builder()
            .setSpace(BuildConfig.SPACE_ID)
            .setToken(BuildConfig.SPACE_ACCESS_TOKEN)
            .build()
    }

    @Provides
    @Singleton
    fun provideVault(@DataContext context: Context): Vault {
        return Vault.with(context, ContentfulSpace::class.java)
    }

    @Provides
    @Singleton
    fun provideSyncConfig(cdaClient: CDAClient): SyncConfig {
        return SyncConfig.builder()
            .setClient(cdaClient)
            .build()
    }

    @Module
    interface Bindings {

        @Binds
        fun bindDataSyncManager(contentfulSyncManager: ContentfulSyncManager): GenericContentSyncManager
    }
}