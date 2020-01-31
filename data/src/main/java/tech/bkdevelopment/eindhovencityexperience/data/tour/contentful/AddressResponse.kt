package tech.bkdevelopment.eindhovencityexperience.data.tour.contentful

import com.contentful.vault.ContentType
import com.contentful.vault.Field
import com.contentful.vault.Resource

@ContentType("address")
class AddressResponse : Resource() {

    @JvmField
    @Field
    var name: String? = null

    @JvmField
    @Field
    var location: String? = null
}