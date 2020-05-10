package trails.gradle

data class GcpTarget(
    val name: String,
    val trigger: String,
    val runtime: String = "nodejs8",
    val flags: List<String> = emptyList()
) {
    fun toArgumentList(): Array<String> {
        val args = mutableListOf(
            "gcloud", "functions", "deploy", name, "--runtime", runtime, "--trigger-${trigger}"
        )
        flags.forEach {
            args += it
        }

        return args.toTypedArray()
    }
}

open class GcpExtension {
    val targets: MutableList<GcpTarget> = mutableListOf()
}
