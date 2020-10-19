package cash.andrew.mntrailconditions.data

enum class ApiEndpoints constructor(private val displayName: String, val url: String?) {
    PRODUCTION("Production", MORC_PRODUCTION_URL.toString()),
    STAGING("Staging", "https://us-central1-mn-trail-functions.cloudfunctions.net/trail-aggregator-staging"),
    LOCAL("Local", "http://localhost:8080/"),
    LOCAL_EMULATOR("Local Emulator", "http://10.0.2.2:8080/"),
    CUSTOM("Custom", null);

    override fun toString(): String {
        return displayName
    }

    companion object {
        fun from(endpoint: String): ApiEndpoints = values().find { it.url != null && it.url == endpoint } ?: CUSTOM
    }
}
