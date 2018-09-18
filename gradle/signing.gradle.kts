// load in keystore values if available otherwise load in debug values
extra["keystorePassword"] = when {
    project.hasProperty("cash.andrew.mntrail.keystorePassword") -> properties["cash.andrew.mntrail.keystorePassword"]
    else -> System.getenv("MN_TRAIL_INFO_KEYSTORE_PASSWORD") ?: "android"
}


extra["aliasKeyPassword"] = when {
    project.hasProperty("cash.andrew.mntrail.aliasKeyPassword") -> project.properties["cash.andrew.mntrail.aliasKeyPassword"]
    else -> System.getenv("MN_TRAIL_INFO_ALIAS_KEY_PASSWORD") ?: "android"
}


extra["storeKeyAlias"] = when {
    project.hasProperty("cash.andrew.mntrail.storeKeyAlias") -> project.properties["cash.andrew.mntrail.storeKeyAlias"]
    else -> System.getenv("MN_TRAIL_INFO_STORE_KEY_ALIAS") ?: "android"
}

extra["keystoreLocation"] = when {
    hasProperty("cash.andrew.mntrail.keystoreLocation") -> properties["cash.andrew.mntrail.keystoreLocation"]
    else -> System.getenv("MN_TRAIL_INFO_KEYSTORE_LOCATION") ?: "keys/debug.keystore"
}
