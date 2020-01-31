package tech.bkdevelopment.eindhovencityexperience.data.story.contentful

import com.contentful.vault.Asset
import com.contentful.vault.ContentType
import com.contentful.vault.Field
import com.contentful.vault.Resource


@ContentType("media")
class MediaResponse : Resource() {

    @JvmField
    @Field
    var title: String? = null

    @JvmField
    @Field
    var thumbnail: Asset? = null

    @JvmField
    @Field
    var type: String? = null

    @JvmField
    @Field
    var mediaItem: Asset? = null

    @JvmField
    @Field
    var source: String? = null
}