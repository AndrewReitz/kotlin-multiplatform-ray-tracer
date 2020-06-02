import kotlin.test.Test
import kotlin.test.assertEquals

class StringsKtTest {
  @Test
  fun `should convert to screaming snake case`() {
    assertEquals("TEST", "test".toScreamingSnakeCase())
    assertEquals("PASCAL_CASE", "pascalCase".toScreamingSnakeCase())
    assertEquals("FIREBASE_PROJECT_ID", "firebase.projectId".toScreamingSnakeCase())
  }
}
