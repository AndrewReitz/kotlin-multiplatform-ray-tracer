package raytracer.core

import kotlinx.serialization.Serializable

@Serializable
data class Scene(val camera: Camera, val world: World) {
    suspend fun render() = camera.render(world)
}
