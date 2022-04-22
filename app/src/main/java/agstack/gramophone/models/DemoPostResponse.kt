package agstack.gramophone.models

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class DemoPostResponse(
    @Json(name = "data")
    val data: List<DemoPost>?
)