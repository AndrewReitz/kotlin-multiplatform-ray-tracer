package raytracer.console

import kotlinx.coroutines.CoroutineScope

expect fun run(body: suspend CoroutineScope.() -> Unit)
