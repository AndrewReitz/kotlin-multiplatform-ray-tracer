package raytracer.core

import raytracer.math.Point
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

class PointLightTest {
  @JsName("A_point_light_has_a_position_and_intensity")
  @Test
  fun `A point light has a position and intensity`() {
    val position = Point(0, 0, 0)
    val intensity = Color(1, 1, 1)
    val light = PointLight(position, intensity)

    assertEquals(actual = light.position, expected = position)
    assertEquals(actual = light.intensity, expected = intensity)
  }
}
