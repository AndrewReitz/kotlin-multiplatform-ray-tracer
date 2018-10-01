package raytracer.core

interface Shape {
    val shader: Shader

    fun isHit(ray: Ray, t: T): Boolean
}