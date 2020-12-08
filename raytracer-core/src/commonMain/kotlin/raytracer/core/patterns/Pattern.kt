package raytracer.core.patterns

import raytracer.core.Color
import raytracer.core.shapes.Shape
import raytracer.math.Matrix
import raytracer.math.Point

interface Pattern {
    val transform: Matrix

    fun patternAtShape(shape: Shape, worldPoint: Point): Color {
        val objPoint = shape.transform.inverse() * worldPoint
        val patternPoint = transform.inverse() * objPoint
        return patternAt(patternPoint)
    }

    fun patternAt(point: Point): Color
}
