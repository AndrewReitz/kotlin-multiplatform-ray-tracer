package trail.networking.model

import trail.networking.twitter.TwitterAccount
import trail.networking.twitter.TwitterAccount.CuyunaTwitter
import trail.networking.twitter.TwitterAccount.HartleyTwitter
import trail.networking.twitter.TwitterAccount.LesterParkTwitter
import trail.networking.twitter.TwitterAccount.MissionCreekTwitter
import trail.networking.twitter.TwitterAccount.PiedmontTrailTwitter
import trail.networking.twitter.TwitterAccount.PokegamaTrailTwitter

enum class KnownTrail(
  val trailName: String,
  val facebookUrl: String?,
  val mtbProjectUrl: String,
  val website: String,
  val otherNames: Set<String> = emptySet(),
  val twitterAccount: TwitterAccount? = null
) {

  BattleCreek(
    trailName = "Battle Creek",
    facebookUrl = "https://www.facebook.com/battlecreekmountainbike",
    mtbProjectUrl = "https://www.mtbproject.com/trail/7028008/battle-creek-mountain-bike-trail",
    website = "https://www.morcmtb.org/trail/battle-creek/"
  ),

  BertramLakes(
    trailName = "Bertram Lakes",
    facebookUrl = "https://www.facebook.com/BertramSingletrack",
    mtbProjectUrl = "https://www.mtbproject.com/trail/3505586/bertram-chain-of-lakes",
    website = "http://www.morcmtb.org/bertram",
    otherNames = setOf("Monticello", "Bertram Chain of Lakes")
  ),

  CarverLakePark(
    trailName = "Carver Lake Park",
    facebookUrl = "https://www.facebook.com/mtbcarverlake/",
    mtbProjectUrl = "https://www.mtbproject.com/trail/3520161/carver-lake-park",
    website = "http://www.morcmtb.org/carver-lake",
    otherNames = setOf("Carver Lake")
  ),

  CottageGroveBikePark(
    trailName = "Cottage Grove Bike Park",
    facebookUrl = "https://www.facebook.com/cottagegrovebp/",
    mtbProjectUrl = "https://www.mtbproject.com/trail/7000927/west-draw-bike-park",
    website = "http://www.morcmtb.org/cottage-grove-bike-park/"
  ),

  ElmCreek(
    trailName = "Elm Creek",
    facebookUrl = "https://www.facebook.com/ElmCreekSingletrack",
    mtbProjectUrl = "https://www.mtbproject.com/directory/8015292/elm-creek-park-reserve",
    website = "http://www.morcmtb.org/elm-creek"
  ),

  HillsidePark(
    trailName = "Hillside Park",
    facebookUrl = "https://www.facebook.com/HillsideParkSingletrack/",
    mtbProjectUrl = "https://www.mtbproject.com/trail/3680808/hillside",
    website = "http://www.morcmtb.org/hillside-park"
  ),

  LakeRebecca(
    trailName = "Lake Rebecca",
    facebookUrl = "https://www.facebook.com/LakeRebeccaSingletrack/",
    mtbProjectUrl = "https://www.mtbproject.com/directory/8014842/lake-rebecca-park-reserve",
    website = "http://www.morcmtb.org/lake-rebecca"
  ),

  LebanonHills(
    trailName = "Lebanon Hills",
    facebookUrl = "https://www.facebook.com/lebanonhills/",
    mtbProjectUrl = "https://www.mtbproject.com/directory/8011629/lebanon-hills-regional-park",
    website = "http://www.morcmtb.org/lebanon-hills"
  ),

  MNRiverBottoms(
    trailName = "Minnesota River Trail",
    facebookUrl = null,
    mtbProjectUrl = "https://www.mtbproject.com/trail/7016269/minnesota-river-bottoms",
    website = "http://www.morcmtb.org/minnesota-river-trail/",
    otherNames = setOf("MN River Bottoms")
  ),

  MurphyHanrehan(
    trailName = "Murphy Hanrehan",
    facebookUrl = "https://www.facebook.com/MurphyHanrehanSingletrack/",
    mtbProjectUrl = "https://www.mtbproject.com/directory/8011510/murphy-hanrehan-park-reserve",
    website = "http://www.morcmtb.org/murphy-hanrehan"
  ),

  SalemHills(
    trailName = "Salem Hills",
    facebookUrl = "https://www.facebook.com/Salem-Hills-Single-Track-1580089522261838/",
    mtbProjectUrl = "https://www.mtbproject.com/directory/8016604/salem-hills-park",
    website = "http://www.morcmtb.org/salem-hills"
  ),

  SunfishLakePark(
    trailName = "Sunfish Lake Park",
    facebookUrl = "https://www.facebook.com/SunfishMTB",
    mtbProjectUrl = "https://www.mtbproject.com/trail/6114344/lake-elmo-park-preserve",
    website = "https://sminc-lake-elmo.org/trail-info/", // TODO update when morc has more info
  ),

  TerraceOaks(
    trailName = "Terrace Oaks",
    facebookUrl = "https://www.facebook.com/Terrace-Oaks-Singletrack-Trail-1385424115111259",
    mtbProjectUrl = "https://www.mtbproject.com/trail/4350735/terrace-oaks-trail",
    website = "http://www.morcmtb.org/terrace-oaks"
  ),

  TheodoreWirth(
    trailName = "Theodore Wirth",
    facebookUrl = "https://www.facebook.com/theowirthsingletrack",
    mtbProjectUrl = "https://www.mtbproject.com/directory/8014425/theodore-wirth-park",
    website = "http://www.morcmtb.org/theodore-wirth"
  ),

  Cuyuna(
    trailName = "Cuyuna Lakes Mountain Bike Trails",
    facebookUrl = "https://www.facebook.com/cuyunalakesmtb/",
    mtbProjectUrl = "https://www.mtbproject.com/directory/8011506/cuyuna-lakes",
    website = "https://www.cuyunalakesmtb.com/",
    twitterAccount = CuyunaTwitter
  ),

  HartleyPark(
    trailName = "Hartley Park",
    facebookUrl = null,
    mtbProjectUrl = "https://www.mtbproject.com/directory/8012227/hartley-park",
    website = "https://www.coggs.com/hartley-trails",
    twitterAccount = HartleyTwitter
  ),

  LesterPark(
    trailName = "Lester Park",
    facebookUrl = null,
    mtbProjectUrl = "https://www.mtbproject.com/directory/8012228/lester-park",
    website = "https://www.coggs.com/lester-trail",
    twitterAccount = LesterParkTwitter
  ),

  MissionCreek(
    trailName = "Mission Creek",
    facebookUrl = null,
    mtbProjectUrl = "https://www.mtbproject.com/directory/8012224/mission-creek",
    website = "https://www.coggs.com/mission-creek-trails",
    twitterAccount = MissionCreekTwitter
  ),

  Piedmont(
    trailName = "Piedmont",
    facebookUrl = null,
    mtbProjectUrl = "https://www.mtbproject.com/directory/8012226/piedmont-brewer",
    website = "https://www.coggs.com/piedmont-trails",
    twitterAccount = PiedmontTrailTwitter
  ),

  Pokegama(
    trailName = "Pokegama",
    facebookUrl = null,
    mtbProjectUrl = "mtbproject.com/trail/7011768/pokegama-trail",
    website = "https://www.coggs.com/pokegama-trails",
    twitterAccount = PokegamaTrailTwitter
  );

  companion object {
    val coggsTrails = listOf(
      HartleyPark,
      LesterPark,
      MissionCreek,
      Piedmont,
      Pokegama
    )

    val morcTrails = listOf(
      BattleCreek,
      BertramLakes,
      CarverLakePark,
      CottageGroveBikePark,
      ElmCreek,
      HillsidePark,
      LakeRebecca,
      LebanonHills,
      MNRiverBottoms,
      MurphyHanrehan,
      SalemHills,
      SunfishLakePark,
      TerraceOaks,
      TheodoreWirth
    )
  }
}
