extra["keystorePassword"] = project.run {
    if (hasProperty("cash.andrew.mntrail.keystorePassword")) {
        properties["cash.andrew.mntrail.keystorePassword"]
    } else System.getenv("MN_TRAIL_INFO_KEYSTORE_PASSWORD") ?: ""
}


extra["aliasKeyPassword"] = project.run {
    if (hasProperty("cash.andrew.mntrail.aliasKeyPassword")) {
        properties["cash.andrew.mntrail.aliasKeyPassword"]
    } else System.getenv("MN_TRAIL_INFO_ALIAS_KEY_PASSWORD") ?: ""
}


extra["storeKeyAlias"] = project.run {
    if (hasProperty("cash.andrew.mntrail.storeKeyAlias")) {
        properties["cash.andrew.mntrail.storeKeyAlias"]
    } else System.getenv("MN_TRAIL_INFO_STORE_KEY_ALIAS") ?: ""
}

extra["keystoreLocation"] = project.run {
    if (hasProperty("cash.andrew.mntrail.keystoreLocation")) {
        properties["cash.andrew.mntrail.keystoreLocation"]
    } else System.getenv("MN_TRAIL_INFO_KEYSTORE_LOCATION") ?: ""
}
