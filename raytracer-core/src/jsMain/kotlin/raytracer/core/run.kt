package raytracer.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise

actual fun runBlocking(body: suspend CoroutineScope.() -> Unit): dynamic =
    GlobalScope.promise { body() }
