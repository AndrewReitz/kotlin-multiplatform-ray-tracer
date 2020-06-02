package cash.andrew.mntrailconditions.util

import com.google.firebase.messaging.FirebaseMessaging

// todo remove after 四
fun String.toTopicName() = spacesToHyphens()

fun FirebaseMessaging.subscribeToTopicV2(topic: String) = subscribeToTopic("v2.${topic.toTopicName()}")
fun FirebaseMessaging.unsubscribeFromTopicV2(topic: String) = unsubscribeFromTopic("v2.${topic.toTopicName()}")
