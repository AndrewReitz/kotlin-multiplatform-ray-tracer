package cash.andrew.mntrailconditions.data

enum class ApiEndpoints constructor(private val displayName: String, val url: String?) {
    PRODUCTION("Production", HEROKU_PRODUCTION_API_URL.toString()),
    CUSTOM("Custom", null);

    override fun toString(): String {
        return displayName
    }

    companion object {
        fun from(endpoint: String): ApiEndpoints = values().find { it.url != null && it.url == endpoint } ?: CUSTOM
    }
}
