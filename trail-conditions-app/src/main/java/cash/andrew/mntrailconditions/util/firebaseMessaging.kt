package cash.andrew.mntrailconditions.util

import com.google.firebase.messaging.FirebaseMessaging

fun FirebaseMessaging.subscribeToTopicV2(topic: String) = subscribeToTopic("v2.${topic.spacesToHyphens()}")
fun FirebaseMessaging.unsubscribeFromTopicV2(topic: String) = unsubscribeFromTopic("v2.${topic.spacesToHyphens()}")
