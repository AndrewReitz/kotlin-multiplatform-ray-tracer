package trail.networking

import cash.andrew.kotlin.common.Success
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.*
import kotlin.js.JsName
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertTrue

private const val JSON =
  """[{"trailName":"Battle Creek","trailId":"3b716fa1-af6b-4aa8-aff1-11ff473072d1","createdAt":1519053269271,"updatedBy":"us-east-2:3537f70a-1ff9-4118-9c85-b238c27281d0","state":"Minnesota","city":"St Paul","zipcode":"55119","trailStatus":null,"updatedAt":1586105879169,"longitude":"-93.0216788","description":null,"latitude":"44.936068","street":"Battle Creek Rd"},{"trailName":"Bertram Chain of Lakes","trailId":"2b741916-94ba-418e-b468-12bc0c14c2ff","createdAt":1519053311026,"updatedBy":"us-east-2:3537f70a-1ff9-4118-9c85-b238c27281d0","state":"Minnesota","city":"Monticello","zipcode":"55362","trailStatus":"Closed","updatedAt":1586105883771,"longitude":"-93.8574287","description":null,"latitude":"45.283094","street":"1744 90th Street NE"},{"trailName":"Carver Lake Park","trailId":"41ecd511-9274-45a0-957f-8cab16012e9f","createdAt":1519053213124,"updatedBy":"us-east-1:399937b7-68ae-49a0-bb4e-ababb1888974","state":"Minnesota","city":"Woodbury","zipcode":"55125","trailStatus":"Closed","updatedAt":1579959180012,"longitude":"-92.979422","description":null,"latitude":"44.90311699999999","street":"3175 Century Ave S"},{"trailName":"Cottage Grove Bike Park","trailId":"25e0fce0-9777-4623-9897-ae57fa2dcc39","createdAt":1519053610443,"updatedBy":"us-east-1:399937b7-68ae-49a0-bb4e-ababb1888974","state":"Minnesota","city":"Cottage Grove","zipcode":"55016","trailStatus":"Closed","updatedAt":1579959183394,"longitude":"-92.9699413","description":null,"latitude":"44.8473887","street":"7050 Meadow Grass Avenue South"},{"trailName":"Elm Creek","trailId":"d932e5fc-a4a4-4e33-820d-864556d0c0ec","createdAt":1519053163567,"updatedBy":"us-east-1:399937b7-68ae-49a0-bb4e-ababb1888974","state":"Minnesota","city":"Champlin","zipcode":"55316","trailStatus":"Closed","updatedAt":1579959187378,"longitude":"-93.4167322","description":null,"latitude":"45.1807175","street":"1827 W Hayden Lake Rd"},{"trailName":"Hillside Park","trailId":"ec5949d8-b11a-4b1c-a659-22fbf7577b64","createdAt":1519053651575,"updatedBy":"us-east-2:3537f70a-1ff9-4118-9c85-b238c27281d0","state":"Minnesota","city":"Elk River","zipcode":"55330","trailStatus":"Closed","updatedAt":1586105888102,"longitude":"-93.539913","description":null,"latitude":"45.299075","street":"10801 181st Ave NW"},{"trailName":"Lake Rebecca","trailId":"10e3fba6-063b-46af-a1af-22184be83cd1","createdAt":1519053687203,"updatedBy":"us-east-2:3537f70a-1ff9-4118-9c85-b238c27281d0","state":"Minnesota","city":"Rockford","zipcode":"55373","trailStatus":"Closed","updatedAt":1586105892319,"longitude":"-93.7589576","description":null,"latitude":"45.0717787","street":"9831 Rebecca Park Trail"},{"trailName":"Lebanon Hills","trailId":"2cce9ec0-a0aa-41ca-9348-9849803fe7b3","createdAt":1519053352023,"updatedBy":"us-east-1:399937b7-68ae-49a0-bb4e-ababb1888974","state":"Minnesota","city":"Eagan","zipcode":"55122","trailStatus":"Closed","updatedAt":1579959199991,"longitude":"-93.1898107","description":null,"latitude":"44.7822821","street":"4801 Johnny Cake Ridge Road"},{"trailName":"Minnesota River Trail","trailId":"ef3854d5-73b6-47c1-bf31-4efe21e4fee1","createdAt":1519053397303,"updatedBy":"us-east-1:399937b7-68ae-49a0-bb4e-ababb1888974","state":"Minnesota","city":"Bloomington","zipcode":"55420","trailStatus":"Closed","updatedAt":1579959204270,"longitude":"-93.2898827","description":null,"latitude":"44.8021249","street":"11115 Lyndale Ave S"},{"trailName":"Murphy Hanrehan","trailId":"071c3aba-f614-4801-b447-c9ba215c6482","createdAt":1519053439785,"updatedBy":"us-east-1:399937b7-68ae-49a0-bb4e-ababb1888974","state":"Minnesota","city":"Savage","zipcode":"55372","trailStatus":"Closed","updatedAt":1579959208771,"longitude":"-93.3434754","description":null,"latitude":"44.7189598","street":"15501 Murphy Lake Road"},{"trailName":"Salem Hills","trailId":"1bfd110a-bc33-4024-a62f-cad5702fac81","createdAt":1519053487375,"updatedBy":"us-east-2:f4aa6971-c488-4cd1-a811-89ca5e3560f6","state":"Minnesota","city":"Inver Grove Heights","zipcode":"55077","trailStatus":null,"updatedAt":1583336395337,"longitude":"-93.0745164","description":null,"latitude":"44.8676308","street":"1642 Upper 55th St"},{"trailName":"Terrace Oaks","trailId":"07ade61a-4969-45f8-be64-fe8b135376de","createdAt":1519053528171,"updatedBy":"us-east-1:399937b7-68ae-49a0-bb4e-ababb1888974","state":"Minnesota","city":"Burnsville","zipcode":"55337","trailStatus":"Closed","updatedAt":1579959217310,"longitude":"-93.2385644","description":null,"latitude":"44.7744386","street":"Terrace Oaks Park"},{"trailName":"Theodore Wirth","trailId":"4c82f3f0-9f45-4e16-a436-f490fbea81a4","createdAt":1519053568021,"updatedBy":"us-east-1:399937b7-68ae-49a0-bb4e-ababb1888974","state":"Minnesota","city":"Minneapolis","zipcode":"55422","trailStatus":"Closed","updatedAt":1579959221531,"longitude":"-93.325502","description":null,"latitude":"44.9923964","street":"1301 Theodore Wirth Parkway"}]"""

class MorcTrailRepositoryTest {

  @JsName("should_get_morc_trails")
  @Test
  fun `should get morc trails`() = runTest {
    val client = HttpClient(MockEngine) {
      engine {
        addHandler { request ->
          when (request.url.fullUrl) {
            "https://api.morcmtb.org/v1/trails" -> {
              val headers = headersOf("Content-Type", ContentType.Application.Json.toString())
              respond(JSON, headers = headers)
            }
            else -> error("Unhandled ${request.url.fullUrl}")
          }
        }
      }
    }

    val repo = MorcTrailRepository(client, "https://api.morcmtb.org/v1/trails")

    val a = repo.getTrails()

    assertTrue { a is Success }
  }

  @Ignore
  @JsName("should_get_morc_trails_functional_test")
  @Test
  fun `should get morc trails functional test`() = runTest {
    val a = MorcTrailRepository(
      HttpClient(),
      "http://localhost:8080/"
    ).getTrails()
    assertTrue { a is Success }
  }
}
