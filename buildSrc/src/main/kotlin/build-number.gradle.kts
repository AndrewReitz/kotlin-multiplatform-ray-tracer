extra["buildNumber"] = when {
    hasProperty("cash.andrew.mntrail.buildNumber") -> properties["cash.andrew.mntrail.buildNumber"]
    else -> System.getenv("LIGHT_ALARM_BUILD_NUMBER") ?: ""
}
