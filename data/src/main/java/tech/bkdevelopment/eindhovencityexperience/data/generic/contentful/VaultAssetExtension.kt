package tech.bkdevelopment.eindhovencityexperience.data.generic.contentful

import com.contentful.vault.Asset

fun Asset.httpsUrl(): String? {
    return this.url()?.replace("http://", "https://")
}
