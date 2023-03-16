package agstack.gramophone.utils

import agstack.gramophone.R
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Environment.DIRECTORY_PICTURES
import android.os.Environment.getExternalStoragePublicDirectory
import android.provider.MediaStore
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.BulletSpan
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


object Utility {
    public const val MONTH_DATE_YEAR_FORMAT = "MMM dd, yyyy" /*"Jun 21, 2022"*/
    public const val DATE_MONTH_YEAR_FORMAT = "dd-MMM-yyyy"  /*05-Jul-2022*/
    public const val YEAR_MONTH_DATA_FORMAT = "yyyy-MM-dd"  /*2022-10-10*/
    public const val YEAR_MONTH_DATA_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"  /*2022-11-19 19:18:00*/
    public const val HOUR_MIN_12_TIME_FORMAT = "hh:mm a"  /*12:08 PM*/
    public const val HOUR_MIN_SECOND_TIME_FORMAT = "HH:mm:ss"  /*09:18:46*/
    public const val ORDER_PLACED_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"  /*2001-07-04T12:08:56*/
    public const val COMMUNITY_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"  /*2001-07-04T12:08:56*/

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
        requiredFormat: String,
        currentFormat: String,
    ): String {
        try {
            // Creating date format
            val dateFormat: DateFormat = SimpleDateFormat(requiredFormat)
            val format = SimpleDateFormat(currentFormat)
            val date: Date = format.parse(dateString)!!

            // Formatting Date according to the given format
            return dateFormat.format(date).toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dateString
    }

    fun getEnglishFormattedDate(
        dateString: String,
        requiredFormat: String,
        currentFormat: String,
    ): String {
        try {
            // Creating date format
            val dateFormat: DateFormat = SimpleDateFormat(requiredFormat, Locale.ENGLISH)
            val format = SimpleDateFormat(currentFormat, Locale.ENGLISH)
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

//   val path: String = MediaStore.Images.Media.insertImage(
//            inContext.getContentResolver(),
//            bitmap,
//            "Title",
//            null
//        )
//        return Uri.parse(path)



          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            return saveImageInQ(inContext,bitmap)
        else return saveImageInLegacy(inContext,bitmap)


    }

    private fun saveImageInLegacy(inContext: Context,bitmap: Bitmap?): Uri? {
        val appContext = inContext
        val filename = "IMG_${System.currentTimeMillis()}.jpg"
        val directory = getExternalStoragePublicDirectory(DIRECTORY_PICTURES)
        val file = File(directory, filename)
        val outStream = FileOutputStream(file)
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, outStream)
        outStream.flush()
        outStream.close()
        MediaScannerConnection.scanFile(appContext, arrayOf(file.absolutePath),
            null, null)
        return FileProvider.getUriForFile(appContext, "${appContext.packageName}.provider",
            file)

    }

    private fun saveImageInQ(inContext: Context,bitmap: Bitmap?) :Uri?{

        val filename = "IMG_${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null
        var imageUri: Uri? = null
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            put(MediaStore.Video.Media.IS_PENDING, 1)
        }

        //use application context to get contentResolver
        val contentResolver = inContext.contentResolver

        contentResolver.also { resolver ->
            imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = imageUri?.let { resolver.openOutputStream(it) }
        }

        fos?.use { bitmap?.compress(Bitmap.CompressFormat.JPEG, 70, it) }

        contentValues.clear()
        contentValues.put(MediaStore.Video.Media.IS_PENDING, 0)
        imageUri?.let { contentResolver.update(it, contentValues, null, null) }

        return imageUri

    }

    fun getErrorMessage(data: ResponseBody?): String? {
        val errorBody = data?.string()?.let { JSONObject(it) }
        var messgae: String = ""
        messgae = errorBody?.optString(Constants.GP_API_MESSAGE).toString()
        if (TextUtils.isEmpty(messgae)) {
            messgae = errorBody?.optString(Constants.MESSAGE).toString()
        }
        return messgae
    }

    fun getCurrentDate(): String? {
        val date = Date()
        val formatter = SimpleDateFormat(COMMUNITY_DATE_FORMAT)
        return formatter.format(date)
    }


    fun getShowingDate(date: Date): String? {
        val formatter = SimpleDateFormat(COMMUNITY_DATE_FORMAT)
        return formatter.format(date).plus("Z")
    }

    fun getShowingFromStringDate(date: String): String? {
        val dateObj = Date(date)
        val formatter = SimpleDateFormat(COMMUNITY_DATE_FORMAT)
        return formatter.format(dateObj).plus("Z")

    }

    fun getShowingFromStringDate1(date: String): String? {
        val str_date = date
        val formatter: DateFormat = SimpleDateFormat("dd-MMM-yyyy")
        val dateString = formatter.parse(str_date)

        val formatter2 = SimpleDateFormat(COMMUNITY_DATE_FORMAT)
        return formatter2.format(dateString).plus("Z")

    }

    fun getShowingFromStringSplitDate(sowingDate: String): Any? {
        val dateStr = sowingDate.split("-")
        val cal =  Calendar.getInstance()
        cal.set(Calendar.YEAR, dateStr[0].toInt())
        cal.set(Calendar.MONTH, dateStr[1].toInt())
        cal.set(Calendar.DAY_OF_MONTH, dateStr[2].toInt())
        val formatter = SimpleDateFormat(COMMUNITY_DATE_FORMAT)
        return formatter.format(cal.time).plus("Z")
    }
}