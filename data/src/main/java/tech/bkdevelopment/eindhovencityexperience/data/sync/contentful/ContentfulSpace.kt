package tech.bkdevelopment.eindhovencityexperience.data.sync.contentful

import com.contentful.vault.Space
import tech.bkdevelopment.eindhovencityexperience.data.BuildConfig
import tech.bkdevelopment.eindhovencityexperience.data.story.contentful.StoryResponse
import tech.bkdevelopment.eindhovencityexperience.data.sync.contentful.ContentfulSpace.Companion.DATABASE_VERSION
import tech.bkdevelopment.eindhovencityexperience.data.sync.contentful.ContentfulSpace.Companion.ENGLISH_LOCALE_NAME
import tech.bkdevelopment.eindhovencityexperience.data.tour.contentful.AddressResponse
import tech.bkdevelopment.eindhovencityexperience.data.tour.contentful.TourResponse

@Space(
    value = BuildConfig.SPACE_ID,
    models = [
        AddressResponse::class,
        StoryResponse::class,
        TourResponse::class],
    locales = [ENGLISH_LOCALE_NAME],
    dbVersion = DATABASE_VERSION
)
class ContentfulSpace {

    companion object {

        internal const val ENGLISH_LOCALE_NAME = "en-US"
        internal const val DATABASE_VERSION = 1
    }
}