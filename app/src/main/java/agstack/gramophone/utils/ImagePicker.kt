package agstack.gramophone.utils

import agstack.gramophone.BuildConfig
import agstack.gramophone.R
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import com.amnix.xtension.extensions.toByteArray
import java.io.File
import java.io.FileNotFoundException

class ImagePicker {

    companion object {

        private const val TEMP_IMAGE_NAME = "pickImageResult.jpeg"
        private var isCamera = false
        private const val DEFAULT_MIN_WIDTH_QUALITY = 500


        fun getCameraIntent(context: Context): Intent? {

        // Determine Uri of camera image to save.
        val outputFileUri: Uri? = ImagePicker.getCaptureImageOutputUri(context)

        outputFileUri?.let {
           // it.toFile()
            val file: File = File(ImagePicker.getRealPathFromURI(outputFileUri, context))
            if (file.exists()) file.delete()
        }
        val allIntents: MutableList<Intent> = ArrayList()
        val packageManager = context.packageManager

        // collect all camera intents
        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val listCam = packageManager.queryIntentActivities(captureIntent, 0)
        for (res in listCam) {
            val intent = Intent(captureIntent)
            intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
            intent.setPackage(res.activityInfo.packageName)
//            if (outputFileUri != null) {
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
//            }
            allIntents.add(intent)
        }

        // collect all gallery intents
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        val listGallery = packageManager.queryIntentActivities(galleryIntent, 0)

        // priority to only gallery app if not present all app related to image
        val onlyGalleryIntents: List<Intent>? =
            ImagePicker.getGalleryIntent(listGallery, galleryIntent)

        onlyGalleryIntents?.let {
            for (intent in onlyGalleryIntents) {
                if (intent != null) {
                    allIntents.add(intent)
                }
            }
        }

        // the main intent is the last in the list (fucking android) so pickup the useless one
        var mainIntent = allIntents[allIntents.size - 1]
        for (intent in allIntents) {
            if (intent.component!!.className == "com.android.documentsui.DocumentsActivity") {
                mainIntent = intent
                break
            }
        }
        allIntents.remove(mainIntent)

        // Create a chooser from the main intent
        val chooserIntent =
            Intent.createChooser(mainIntent, context.getString(R.string.select_image_title))

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toTypedArray())
        return chooserIntent
    }

        fun getGallaryIntent(context: Context): Intent? {

        // Determine Uri of camera image to save.
        val outputFileUri: Uri? = ImagePicker.getCaptureImageOutputUri(context)

        outputFileUri?.let {
           // it.toFile()
            val file: File = File(ImagePicker.getRealPathFromURI(outputFileUri, context))
            if (file.exists()) file.delete()
        }
        val allIntents: MutableList<Intent> = ArrayList()
        val packageManager = context.packageManager

        // collect all gallery intents
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        val listGallery = packageManager.queryIntentActivities(galleryIntent, 0)

        // priority to only gallery app if not present all app related to image
        val onlyGalleryIntents: List<Intent>? =
            ImagePicker.getGalleryIntent(listGallery, galleryIntent)

        onlyGalleryIntents?.let {
            for (intent in onlyGalleryIntents) {
                if (intent != null) {
                    allIntents.add(intent)
                }
            }
        }

        // the main intent is the last in the list (fucking android) so pickup the useless one
        var mainIntent = allIntents[allIntents.size - 1]
        for (intent in allIntents) {
            if (intent.component!!.className == "com.android.documentsui.DocumentsActivity") {
                mainIntent = intent
                break
            }
        }
        allIntents.remove(mainIntent)

        // Create a chooser from the main intent
        val chooserIntent =
            Intent.createChooser(mainIntent, context.getString(R.string.select_image_title))

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toTypedArray())
        return chooserIntent
    }

        /**
         * Get URI to image received from capture  by camera.
         */
        private fun getCaptureImageOutputUri(context: Context): Uri? {
            var outputFileUri: Uri? = null
            val getImage = context.externalCacheDir
            if (getImage != null) {
                outputFileUri = Uri.fromFile(File(getImage.absolutePath, TEMP_IMAGE_NAME))
            }
            return outputFileUri
        }


        private fun getRealPathFromURI(contentUri: Uri, activity: Context): String? {
            //  Uri contentUri = Uri.parse(contentURI);
            val cursor = activity.contentResolver.query(contentUri, null, null, null, null)
            return if (cursor == null) {
                contentUri.path
            } else {
                cursor.moveToFirst()
                val idx = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                cursor.getString(idx)
            }
        }


        fun getGalleryIntent(
            listGallery: List<ResolveInfo>?,
            gallerysIntent: Intent?
        ): List<Intent>? {
            val allGalleryIntents: MutableList<Intent> = java.util.ArrayList()
            if (listGallery != null) {
                for (res in listGallery) {
                    val packageName = res.activityInfo.packageName.toString()
                    val activityInfoName = res.activityInfo.name.toString()
                    val intent = Intent(gallerysIntent)
                    intent.component =
                        ComponentName(res.activityInfo.packageName, res.activityInfo.name)
                    intent.setPackage(res.activityInfo.packageName)
                    if (packageName.contains("gallery") || activityInfoName.contains("gallery")) {
                        allGalleryIntents.clear()
                        allGalleryIntents.add(intent)
                        break
                    } else {
                        allGalleryIntents.add(intent)
                    }
                }
            }
            return allGalleryIntents
        }


        fun getImageFromResult(
            context: Context,
            resultCode: Int,
            imageReturnedIntent: Intent?
        ): Uri? {

            val imageFile: File = ImagePicker.getTempFile(context)!!
            if (resultCode == Activity.RESULT_OK) {
                val selectedImage: Uri?
                ImagePicker.isCamera =
                    imageReturnedIntent == null || imageReturnedIntent.data == null ||
                            imageReturnedIntent.data.toString().contains(imageFile.toString())
                selectedImage = if (ImagePicker.isCamera) {
                    FileProvider.getUriForFile(
                        context!!,
                        BuildConfig.APPLICATION_ID + ".provider", imageFile
                    )

                } else {
                    imageReturnedIntent!!.data
                }
                if (!imageFile.exists()){
                    imageFile.createNewFile()
                    val imageByteArray = imageReturnedIntent?.extras?.getParcelable<Bitmap>("data")?.toByteArray(Bitmap.CompressFormat.JPEG)
                    if (imageByteArray != null) imageFile.writeBytes(imageByteArray)
                }
                return selectedImage
            }
            return null
        }

        private fun getTempFile(context: Context): File? {
            return File(context.externalCacheDir, ImagePicker.TEMP_IMAGE_NAME)
        }


        fun checkImageMinSize(rect: Rect): Boolean {
            return rect.height() >= Constants.Profile.MIN_AVATAR_SIZE && rect.width() >= Constants.Profile.MIN_AVATAR_SIZE
        }


        fun getImageResized(context: Context, selectedImage: Uri): Bitmap? {
            var bm: Bitmap?
            val sampleSizes = intArrayOf(5, 3, 2, 1)
            var i = 0
            do {
                bm = ImagePicker.decodeBitmap(context, selectedImage, sampleSizes[i])
                // Log.i(TAG, "resizer: new bitmap width = " + bm.getWidth());
                i++
            } while (bm?.width ?: 0 <= DEFAULT_MIN_WIDTH_QUALITY && i < sampleSizes.size)
            return bm
        }


        private fun decodeBitmap(context: Context, theUri: Uri, sampleSize: Int): Bitmap? {
            val options = BitmapFactory.Options()
            options.inSampleSize = sampleSize
            var fileDescriptor: AssetFileDescriptor? = null
            try {
                fileDescriptor = context.contentResolver.openAssetFileDescriptor(theUri, "r")
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            return if (fileDescriptor != null) {
                BitmapFactory.decodeFileDescriptor(fileDescriptor.fileDescriptor, null, options)
            } else null
        }


    }

}