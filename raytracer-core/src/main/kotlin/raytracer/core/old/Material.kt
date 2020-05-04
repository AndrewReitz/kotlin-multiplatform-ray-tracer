package raytracer.core.old

import raytracer.core.Color

interface Material {
    val reflection: Double
    val color: Color
}