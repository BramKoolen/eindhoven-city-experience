package tech.bkdevelopment.eindhovencityexperience.data.story.contentful

import com.contentful.vault.ContentType
import com.contentful.vault.Field
import com.contentful.vault.Resource

@ContentType("story")
class StoryResponse : Resource() {

    @JvmField
    @Field
    var title: String? = null

    @JvmField
    @Field
    var summaryText: String? = null

    @JvmField
    @Field
    var storyType: String? = null

    @JvmField
    @Field
    var summary: String? = null

    @JvmField
    @Field
    var description: String? = null

    @JvmField
    @Field
    var media: List<@JvmSuppressWildcards Resource>? = null

    @JvmField
    @Field
    var location: String? = null
}