package tech.bkdevelopment.eindhovencityexperience.domain.story.model

data class Story(
    val id: String,
    val title: String,
    val summaryText: String,
    val storyType: StoryType,
    val summary: String,
    val description: String,
    val mediaUrlList: List<String>?,
    val lat: Double,
    val long: Double,
    var completed: Boolean
)