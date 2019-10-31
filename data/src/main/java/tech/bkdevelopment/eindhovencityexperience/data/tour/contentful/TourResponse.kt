package tech.bkdevelopment.eindhovencityexperience.data.tour.contentful

import com.contentful.vault.Asset
import com.contentful.vault.ContentType
import com.contentful.vault.Field
import com.contentful.vault.Resource

@ContentType("tour")
class TourResponse : Resource() {

    @JvmField
    @Field
    var title: String? = null

    @JvmField
    @Field
    var description: String? = null

    @JvmField
    @Field
    var thumbnail: Asset? = null

    @JvmField
    @Field
    var icon: Asset? = null

    @JvmField
    @Field
    var time: Int? = null

    @JvmField
    @Field
    var extraTourItemText: String? = null

    @JvmField
    @Field
    var extraTourItemIcon: Asset? = null

    @JvmField
    @Field
    var parkingAddress: Resource? = null

    @JvmField
    @Field
    var startAddress: Resource? = null

    @JvmField
    @Field
    var longDescription: String? = null

    @JvmField
    @Field
    var cafes: List<@JvmSuppressWildcards Resource>? = null

    @JvmField
    @Field
    var story: List<@JvmSuppressWildcards Resource>? = null
}