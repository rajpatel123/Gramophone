package agstack.gramophone.utils

import agstack.gramophone.R
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.SpannableString
import android.text.style.BulletSpan
import android.util.Log
import android.widget.Toast
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.io.*
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
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



    fun generateQR(content: String?, size: Int): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val barcodeEncoder = BarcodeEncoder()
            bitmap = barcodeEncoder.encodeBitmap(
                content,
                BarcodeFormat.QR_CODE, size, size
            )
        } catch (e: WriterException) {
            Log.e("generateQR()", e.message!!)
        }
        return bitmap
    }



    fun saveImage(context: Context,finalBitmap: Bitmap?) {
        var imagePath = "/Gramophone"
        var generator = Random()
        var n = 10000
        n = generator.nextInt(n)
        var fname: String = "Gramophone-qr-code-" + n + ".jpg"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveFileAboveQ(context,finalBitmap,imagePath,fname)
        }else {
            saveFileBelowQ(finalBitmap,imagePath,fname)
        }
        Toast.makeText(context, R.string.download_to_gallery, Toast.LENGTH_SHORT).show()
    }


    private fun saveFileAboveQ(context: Context,finalBitmap: Bitmap?,imagePath:String,fileName:String) {
        val resolver: ContentResolver? = context?.contentResolver
        val contentValues = ContentValues()
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES+imagePath)
        val imageUri: Uri? = resolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        val out: OutputStream?=resolver?.openOutputStream(imageUri!!)
        finalBitmap?.compress(Bitmap.CompressFormat.JPEG, 90, out);
        out?.flush();
        out?.close();
    }

    private fun saveFileBelowQ(finalBitmap: Bitmap?,imagePath:String,fileName:String) {
        var root = Environment.getExternalStorageDirectory().toString();

        var myDir = File(root + imagePath);
        if (!myDir.exists()) {
            myDir.mkdirs()
        }

        var file = File(myDir, fileName)
        if (file.exists())
            file.delete();
        try {
            var out = FileOutputStream(file);
            finalBitmap?.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (e: Exception) {
            e.printStackTrace();
        }


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

    fun bitmapToUri(inContext: Context,bitmap: Bitmap?): Uri? {
        val bytes = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path: String = MediaStore.Images.Media.insertImage(
            inContext.getContentResolver(),
            bitmap,
            "Title",
            null
        )
        return Uri.parse(path)


    }

    fun getErrorMessage(data: ResponseBody?): String? {
        val errorBody = data?.string()?.let { JSONObject(it) }
        return errorBody?.optString(Constants.GP_API_MESSAGE)
    }
}