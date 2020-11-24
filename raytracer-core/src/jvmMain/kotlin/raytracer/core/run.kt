package raytracer.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

actual fun run(body: suspend CoroutineScope.() -> Unit) = runBlocking{ body() }
