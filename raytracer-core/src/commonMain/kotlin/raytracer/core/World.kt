package raytracer.core

import raytracer.math.Float3
import raytracer.math.Matrix
import raytracer.math.Point

data class World(
  val lights: List<PointLight> = emptyList(),
  val objects: List<Sphere> = emptyList()
) {

  fun intersect(ray: Ray): Intersections = Intersections(
    objects.asSequence()
      .map { ray.intersects(it) }
      .flatten()
      .sortedBy { if (it.time >= 0f) it.time else Float.MAX_VALUE }
      .toList()
  )

  fun shadeHit(comps: Computation): Color = lights.map {
    comps.obj.material.lighting(
      light = it,
      position = comps.point,
      eyev = comps.eyev,
      normalv = comps.normalv
    )
  }.reduce { sum, color ->
    sum + color
  }

  fun colorAt(ray: Ray): Color {
    val hit = intersect(ray).firstOrNull() ?: return Color.Black
    val comps = hit.prepareComputations(ray)
    return shadeHit(comps)
  }

  companion object {
    val default
      get() = World(
        lights = listOf(
          PointLight(
            position = Point(-10, -10, -10),
            intensity = Color(1, 1, 1)
          )
        ),
        objects = listOf(
          Sphere(
            material = Material(
              color = Color(
                red = 0.8,
                green = 1.0,
                blue = 0.6
              ),
              diffuse = 0.7f,
              specular = 0.2f
            )
          ),
          Sphere(
            transform = Matrix.scaling(0.5, 0.5, 0.5)
          )
        )
      )
  }
}