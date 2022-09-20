package agstack.gramophone.utils

import android.webkit.MimeTypeMap
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream
import java.io.IOException


class FileUploadRequestBody(private val file: File) : RequestBody() {
    override fun contentType(): MediaType? {
        val fileName = file.name
        val extension = fileName.substring(fileName.lastIndexOf(".") + 1)
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        return if (mimeType != null) mimeType.toMediaTypeOrNull() else null
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return file.length()
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        val buffer = ByteArray(BYTE_COUNT)
        val `in` = FileInputStream(file)
        var uploaded: Long = 0
        var read: Int
        while (`in`.read(buffer).also { read = it } != -1) {
            uploaded += read.toLong()
            sink.write(buffer, 0, read)
        }
        `in`.close()
    }

    companion object {
        private const val BYTE_COUNT = 4096
    }
}