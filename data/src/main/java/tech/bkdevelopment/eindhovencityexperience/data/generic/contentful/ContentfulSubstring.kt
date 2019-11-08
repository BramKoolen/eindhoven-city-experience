package tech.bkdevelopment.eindhovencityexperience.data.generic.contentful

import javax.inject.Inject

class ContentfulSubstring @Inject constructor() {

    fun subStringContenfulRichTextItem(richTextItem: String): String {
        return richTextItem.substringAfter("value=").substringBefore(", nodeType=text}]")
    }

    fun subStringContentfulLocationLat(location: String): Double {
        return location.substringAfter("lat=").substringBefore("}").toDouble()
    }

    fun subStringContentfulLocationLong(location: String): Double {
        return location.substringAfter("{lon=").substringBefore(", lat=").toDouble()
    }
}