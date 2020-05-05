package trail.networking

import kotlinx.coroutines.CoroutineScope

expect fun runTest(body: suspend CoroutineScope.() -> Unit)
