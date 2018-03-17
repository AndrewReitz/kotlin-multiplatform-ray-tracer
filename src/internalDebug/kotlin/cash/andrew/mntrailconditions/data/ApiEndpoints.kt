package cash.andrew.mntrailconditions.data

import cash.andrew.mntrailconditions.data.api.ApiModule

enum class ApiEndpoints private constructor(val displayName: String, val url: String?) {
    PRODUCTION("Production", ApiModule.PRODUCTION_API_URL.toString()),
    CUSTOM("Custom", null);

    override fun toString(): String {
        return displayName
    }

    companion object {
        fun from(endpoint: String): ApiEndpoints = values().find { it.url != null && it.url == endpoint } ?: CUSTOM
    }
}
