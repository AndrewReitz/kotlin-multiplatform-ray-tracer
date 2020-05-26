extra["consumerKey"] = when {
  hasProperty("cash.andrew.mntrail.consumerKey") -> properties["cash.andrew.mntrail.consumerKey"]
  else -> System.getenv("MN_TRAIL_INFO_CONSUMER_KEY") ?: ""
}

extra["consumerSecret"] = when {
  hasProperty("cash.andrew.mntrail.consumerSecret") -> properties["cash.andrew.mntrail.consumerSecret"]
  else -> System.getenv("MN_TRAIL_INFO_CONSUMER_SECRET") ?: ""
}

extra["accessTokenKey"] = when {
  hasProperty("cash.andrew.mntrail.accessTokenKey") -> properties["cash.andrew.mntrail.accessTokenKey"]
  else -> System.getenv("MN_TRAIL_INFO_ACESS_TOKEN_KEY") ?: ""
}

extra["accessTokenSecret"] = when {
  hasProperty("cash.andrew.mntrail.accessTokenSecret") -> properties["cash.andrew.mntrail.accessTokenSecret"]
  else -> System.getenv("MN_TRAIL_INFO_ACESS_TOKEN_SECRET") ?: ""
}
