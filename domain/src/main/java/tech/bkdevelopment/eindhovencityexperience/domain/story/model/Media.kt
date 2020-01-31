package tech.bkdevelopment.eindhovencityexperience.domain.story.model

data class Media(
    val id: String,
    val title: String,
    val thumbnailUrl: String?,
    val type: MediaType,
    val url: String?,
    val source: String
)