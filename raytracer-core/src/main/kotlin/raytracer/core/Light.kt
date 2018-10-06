package raytracer.core

import raytracer.math.Vector3

data class Light(
    val position: Vector3,
    val intensity: Vector3
)