package raytracer.core

import kotlinx.coroutines.CoroutineScope

expect fun run(body: suspend CoroutineScope.() -> Unit)
