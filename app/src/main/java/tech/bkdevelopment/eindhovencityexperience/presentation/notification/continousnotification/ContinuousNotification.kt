package tech.bkdevelopment.eindhovencityexperience.presentation.notification.continousnotification

data class ContinuousNotification(
    val title: String,
    val body: String,
    val isUnlocked: Boolean
)