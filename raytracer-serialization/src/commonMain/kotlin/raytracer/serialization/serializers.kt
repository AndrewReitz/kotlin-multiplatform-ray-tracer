package raytracer.serialization

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import raytracer.core.patterns.CheckerPattern
import raytracer.core.patterns.GradientPattern
import raytracer.core.patterns.Pattern
import raytracer.core.patterns.RingPattern
import raytracer.core.patterns.StripePattern
import raytracer.core.shapes.Plane
import raytracer.core.shapes.Shape
import raytracer.core.shapes.Sphere

private val commonSerializersModule = SerializersModule {
    polymorphic(Shape::class) {
        subclass(Plane::class)
        subclass(Sphere::class)
    }
    polymorphic(Pattern::class) {
        subclass(RingPattern::class)
        subclass(CheckerPattern::class)
        subclass(GradientPattern::class)
        subclass(StripePattern::class)
    }
}

val JSON = Json {
    serializersModule = commonSerializersModule
}
