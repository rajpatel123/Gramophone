package agstack.gramophone.utils

import android.content.Context
import android.os.Environment
import android.text.SpannableString
import android.text.style.BulletSpan
import java.io.File
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


object Utility {
    private const val MONTH_DATE_YEAR_FORMAT = "MMM dd, yyyy" /*"Jun 21, 2022"*/
    private const val DATE_MONTH_YEAR_FORMAT = "dd-MMM-yyyy"  /*05-Jul-2022*/

    fun List<String>.toBulletedList(): CharSequence {
        return SpannableString(this.joinToString("\n")).apply {
            this@toBulletedList.foldIndexed(0) { index, acc, span ->
                val end = acc + span.length + if (index != this@toBulletedList.size - 1) 1 else 0
                this.setSpan(BulletSpan(16), acc, end, 0)
                end
            }
        }
    }


    @Throws(IOException::class)
    fun getPictureFile(
        context: Context,
    ): File {
        val pictureFile = System.currentTimeMillis().toString() + ".jpg"
        val storageDir = getExternalFileDir(context)
        return File(storageDir?.path + File.separator + pictureFile)
    }


    private fun getExternalFileDir(context: Context?): File? {
        return context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    }

    fun getFormattedDate(
        dateString: String,
    ): String {
        try {
            // Creating date format
            val dateFormat: DateFormat = SimpleDateFormat(DATE_MONTH_YEAR_FORMAT, Locale.ENGLISH)
            val format = SimpleDateFormat(MONTH_DATE_YEAR_FORMAT, Locale.ENGLISH)
            val date: Date = format.parse(dateString)!!

            // Formatting Date according to the given format
            return dateFormat.format(date).toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dateString
    }
}