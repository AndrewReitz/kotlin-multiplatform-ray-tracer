package raytracer.core

import kotlinx.coroutines.CoroutineScope

expect fun runBlocking(body: suspend CoroutineScope.() -> Unit)
