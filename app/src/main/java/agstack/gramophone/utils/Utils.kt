package agstack.gramophone.utils

import android.content.Context
import java.io.IOException

object Utils {
    fun getJsonFromAssets(context: Context, fileName: String?): String? {
        val jsonString: String
        jsonString = try {
            val `is` = context.assets.open(fileName!!)
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, "UTF-8")
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return jsonString
    }
}