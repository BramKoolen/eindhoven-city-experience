package tech.bkdevelopment.eindhovencityexperience.presentation.notification.continousnotification

import tech.bkdevelopment.eindhovencityexperience.presentation.story.StoryViewModel

data class ContinuousNotification(
    val title: String,
    val body: String,
    val isUnlocked: Boolean,
    val tourId: String,
    val story: StoryViewModel
)