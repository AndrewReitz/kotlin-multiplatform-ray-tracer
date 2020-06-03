package trail.networking

import cash.andrew.kotlin.common.Success
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.headersOf
import kotlin.js.JsName
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertTrue

private const val JSON =
  """[{"name":"Afton Alps","status":"Closed","description":"...","fullDescription":"Originally Posted by Nate K Afton is not offering mountain biking at this time. Called club house on 8/16/2019 to ask given that their website states that they are \"Opening This Summer\". Employee said they currently do not offer mountain biking, though he wished they did, and would look into updating their website. https://www.aftonalps.com/explore-th...ountain-biking Nate K, Thanks for the update. This post was caught in the filters - Probably because it was a new user posting a URL. I accidentally deleted your login while blowing away about 100 or so spammers. Sorry about that. Please retry when you get chance. I think you can continue to be Nate K, but you'll need to re-register.","lastUpdated":"2019-08-19T09:20"},{"name":"Battle Creek","status":"Dry","description":"FROM FACEBOOK: Happy Friday, everyone! Just a quick heads up: Freight Train needs some touch up...","fullDescription":"FROM FACEBOOK: Happy Friday, everyone! Just a quick heads up: Freight Train needs some touch up work before the weekend. Your tireless trail crew will be tackling it this evening. We need to close Freight Train to safely do the work. Freight Train will be closed for the rest of the day beginning at approximately 6:15pm. The Hump and Cop Out will remain open. Get your Freight Train giggles in before then or tomorrow.","lastUpdated":"2020-04-24T06:54"},{"name":"Monticello","status":"Tacky","description":"Trail should be riding great this afternoon.","fullDescription":"Trail should be riding great this afternoon.","lastUpdated":"2020-05-14T13:52"},{"name":"Carver Lake","status":"Dry","description":"The deeper woods are still tacky but the rest is dry kitty litter, trail could use a good soaking.","fullDescription":"The deeper woods are still tacky but the rest is dry kitty litter, trail could use a good soaking.","lastUpdated":"2020-05-16T08:06"},{"name":"Cottage Grove Bike Park","status":"Dry","description":"Everything is open","fullDescription":"Everything is open","lastUpdated":"2020-05-11T16:33"},{"name":"Elm Creek","status":"Tacky","description":"Open...","fullDescription":"Open...","lastUpdated":"2020-05-14T15:22"},{"name":"Hillside Park","status":"Tacky","description":"Open!","fullDescription":"Open!","lastUpdated":"2020-05-13T14:12"},{"name":"Lake Rebecca","status":"Tacky","description":"Lake Rebecca Singletrack is now OPEN.","fullDescription":"Lake Rebecca Singletrack is now OPEN.","lastUpdated":"2020-05-14T12:40"},{"name":"Lebanon Hills","status":"Dry","description":"Great conditions. Tacky/Dry.","fullDescription":"Great conditions. Tacky/Dry.","lastUpdated":"2020-05-11T14:59"},{"name":"MN River Bottoms","status":"Tacky","description":"Rode Lyndale east to the Cedar bridge. Lots of sand so I recommend fat tires. And of course there'...","fullDescription":"Rode Lyndale east to the Cedar bridge. Lots of sand so I recommend fat tires. And of course there's the construction of the political vanity paved trail. Leave that area alone.","lastUpdated":"2020-05-06T08:14"},{"name":"Murphy Hanrehan","status":"Tacky","description":"Tonight it was tacky perfect in the wooded sections. The prairies were back to dry again. Well ...","fullDescription":"Tonight it was tacky perfect in the wooded sections. The prairies were back to dry again. Well see what the weekend brings us. The forecast shows somewhat significant rain. Stay tuned.","lastUpdated":"2020-05-14T21:28"},{"name":"Salem Hills","status":"Dry","description":"Trails in great shape.","fullDescription":"Trails in great shape.","lastUpdated":"2020-05-13T07:22"},{"name":"Terrace Oaks","status":"Dry","description":"Trail is in great shape.","fullDescription":"Trail is in great shape.","lastUpdated":"2020-05-12T14:46"},{"name":"Theodore Wirth","status":"Tacky","description":"All sections are now open. Tacky to dry.","fullDescription":"All sections are now open. Tacky to dry.","lastUpdated":"2020-05-15T11:12"},{"name":"7-Mile","status":"Fat Tires","description":"Trails are great. Fat tires or studs recommended. Some icy spots and the water crossings are startin...","fullDescription":"Trails are great. Fat tires or studs recommended. Some icy spots and the water crossings are starting to ice over. Great trails for evening rides. Bring your lights.","lastUpdated":"2017-12-09T09:45"},{"name":"Fort LeHillier","status":"Closed","description":"Fort LeHillier's one mile trail is not currently being maintained by MAMB, therefore, is closed. Ple...","fullDescription":"Fort LeHillier's one mile trail is not currently being maintained by MAMB, therefore, is closed. Please checkout our website or Facebook page for trail info on Kiwanis, Traverse des Sioux (in St. Peter) and Mt. Kato. Thank you. Mankatoareamountainbikers.org","lastUpdated":"2017-05-19T05:01"},{"name":"Holzinger Lodge","status":"Tacky","description":"Rode Saturday afternoon and the trail was in great shape!","fullDescription":"Rode Saturday afternoon and the trail was in great shape!","lastUpdated":"2019-05-12T20:08"},{"name":"Mt. Kato","status":"Tacky","description":"Mt. Kato is rocking this year!!! Thanks to John R. at Mt. Kato and MAMB volunteers, trails are gett...","fullDescription":"Mt. Kato is rocking this year!!! Thanks to John R. at Mt. Kato and MAMB volunteers, trails are getting maintained and sustained, features are getting added and the flow is getting updated. Only ${'$'}5 buys you a day pass to ride as much as you want...you won't be disappointed you did!","lastUpdated":"2019-06-30T11:44"},{"name":"Memorial Trail","status":"Tacky","description":"Trail is in great condition Rode Saturday with the kids, all the Blue is great. Someone has been we...","fullDescription":"Trail is in great condition Rode Saturday with the kids, all the Blue is great. Someone has been weed-wacking. I few more trips and it will be perfect Rode again Sunday and did a few laps of the old race course, the Black Diamonds (Minus DH) were is good shape. The lower area was a bit overgrown, but ALL ridable. Stairway was HOT as always, but cleaned it each time.","lastUpdated":"2019-07-15T09:36"},{"name":"Grand Rapids","status":"Damp","description":"Many new trails in the Tioga (Cohasset) Area. Worth the trip if you have a day.","fullDescription":"Many new trails in the Tioga (Cohasset) Area. Worth the trip if you have a day.","lastUpdated":"2019-10-26T11:00"},{"name":"Jail Trail","status":"Fat Tires","description":"Trail was groomed tonight. Should set up nicely by morning.","fullDescription":"Trail was groomed tonight. Should set up nicely by morning.","lastUpdated":"2019-12-01T22:13"},{"name":"Milaca","status":"Tacky","description":"Rode yesterday(6/20) late evening. Trails were in very good condition with very few damp spots. Litt...","fullDescription":"Rode yesterday(6/20) late evening. Trails were in very good condition with very few damp spots. Little to no overgrowth, where there is overgrowth its easily ride-able through/around it. Parking on the west side of the Rum River is small but worth it, take 23 to SW River Rd, first north road west of the high school.","lastUpdated":"2017-06-21T10:14"},{"name":"Hartley Park","status":"Dry","description":"Open for the season! Please always check the COGGS website (www.coggs.com) for most cur...","fullDescription":"Open for the season! Please always check the COGGS website (www.coggs.com) for most current, day-to-day trail conditions.","lastUpdated":"2017-05-10T11:31"},{"name":"Piedmont Trail","status":"Damp","description":"Please always check the COGGS website (www.coggs.com) for most current, day-to-day trail ...","fullDescription":"Please always check the COGGS website (www.coggs.com) for most current, day-to-day trail conditions. Currently *most* of Piedmont is open: all the hand-built trails are good to go. However the newer machine-built trails are still too damp: (thing includes Piedmont's Duluth Traverse segments: Skyline & Lower Burner). Use the Hutchinson Rd parking lot, in order to stay solely on the dry trails. It's likely all of the trails will soon open, but please be patient and wait until the COGGS trail steward gives the word.","lastUpdated":"2017-05-10T11:36"},{"name":"Whitetail Ridge","status":"Tacky","description":"Hero dirt. Just beginning to get sandy in the usual spots.","fullDescription":"Hero dirt. Just beginning to get sandy in the usual spots.","lastUpdated":"2020-05-07T09:19"},{"name":"Woolly Trail","status":"Damp","description":"I jumped the gun a bit. Trails are actually damp to tacky, good to go for a Mothers Day ride.","fullDescription":"I jumped the gun a bit. Trails are actually damp to tacky, good to go for a Mothers Day ride.","lastUpdated":"2020-05-10T08:09"},{"name":"Levis Trow","status":"Tacky","description":"Lots of great trails out here. Cliffhanger is one of a kind. Lots of leaves though.","fullDescription":"Lots of great trails out here. Cliffhanger is one of a kind. Lots of leaves though.","lastUpdated":"2019-10-26T10:58"}]"""

class HtmlMorcTrailRepositoryTest {

  @JsName("should_get_trails")
  @Test
  fun `should get trails`() = runTest {
    val client = HttpClient(MockEngine) {
      engine {
        addHandler { request ->
          when (request.url.fullUrl) {
            "https://mn-trail-info-service.herokuapp.com/" -> {
              val headers = headersOf("Content-Type", ContentType.Application.Json.toString())
              respond(JSON, headers = headers)
            }
            else -> error("Unhandled ${request.url.fullUrl}")
          }
        }
      }
    }

    val repo = HtmlMorcTrailRepository(client)

    val result = repo.getTrails()

    assertTrue { result is Success }
  }

  @Ignore
  @JsName("should_get_trails_functional")
  @Test
  fun `should get trails functional`() = runTest {
    val repo = HtmlMorcTrailRepository(HttpClient())
    val result = repo.getTrails()
    assertTrue { result is Success }
  }
}