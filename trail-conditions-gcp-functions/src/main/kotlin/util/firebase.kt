package util

import kotlin.js.Promise

data class ServiceAccount(
  val clientEmail: String,
  val privateKey: String,
  val projectId: String
)

@JsModule("firebase-admin")
@Suppress("ClassName")
external object admin {
  val apps: Array<dynamic>?
  fun initializeApp(credentials: Credentials)
  fun messaging(): Messaging

  object credential {
    fun cert(serviceAccount: ServiceAccount): Cert
  }
}

data class Credentials(
  val credential: Cert
)

external class Cert

data class Message(
  val notification: Notification,
  val android: Android,
  val topic: String
) {
  data class Android(val ttl: Number)
  data class Notification(
    val title: String,
    val body: String
  )
}

external class Messaging {
  fun send(message: Message): Promise<dynamic>
}
