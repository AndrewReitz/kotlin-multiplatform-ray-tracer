package raytracer.core

import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

class MaterialTest {
  @JsName("The_default_material")
  @Test
  fun `The default material`() {
    val m = Material()
    assertEquals(actual = m.color, expected = Color(1, 1, 1))
    assertEquals(actual = m.ambient, expected = 0.1f)
    assertEquals(actual = m.diffuse, expected = 0.9f)
    assertEquals(actual = m.specular, expected = 0.9f)
    assertEquals(actual = m.shininess, expected = 200f)
  }
}
