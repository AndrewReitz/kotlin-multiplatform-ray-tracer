extra["buildNumber"] = when {
    hasProperty("cash.andrew.mntrail.buildNumber") -> properties["cash.andrew.mntrail.buildNumber"]
    else -> System.getenv("MN_TRAIL_INFO_BUILD_NUMBER") ?: ""
}