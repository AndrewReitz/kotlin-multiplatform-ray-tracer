package raytracer.parsing

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okio.Okio
import org.omg.CORBA.Any
import java.io.File

class Parser {

    private val jsonAdapter: JsonAdapter<Map<String, Any>> = Moshi.Builder()
            .build()
            .adapter(Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java))

    fun parse(file: File) {
        val buffer = Okio.buffer(Okio.source(file))
        val map = jsonAdapter.fromJson(buffer)
    }
}