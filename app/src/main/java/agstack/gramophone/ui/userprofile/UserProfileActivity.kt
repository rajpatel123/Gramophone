package agstack.gramophone.ui.userprofile

import agstack.gramophone.BR
import agstack.gramophone.BuildConfig
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.UserProfileActivityBinding
import agstack.gramophone.ui.userprofile.model.PostImageModel
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.ImagePicker
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import agstack.gramophone.widget.FilePicker
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

private var imageUri: Uri? = null
private var bitmapData: ByteArray? = null


@AndroidEntryPoint
class UserProfileActivity :
    BaseActivityWrapper<UserProfileActivityBinding, UserProfileNavigator, UserProfileViewModel>(),
    UserProfileNavigator {

    private val userProfileViewModel: UserProfileViewModel by viewModels()
    private var cropImage: ActivityResultLauncher<CropImageContractOptions>? = null;
    private val filename = "image"
    var postImageModel = PostImageModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.order_delivered_text)

        userProfileViewModel.setProfilePic()

        cropImage = registerForActivityResult(CropImageContract()) { result ->
            if (result.isSuccessful) {
                // use the returned uri
                if (ImagePicker.checkImageMinSize(result.cropRect!!)) {
                    val imageUri = result.uriContent
                    imageUri?.let { setImage(it) }
                } else {
                    result?.uriContent?.let {
                        setImage(it)
                    }
                }

            } else {
            }
        }

        if (intent.hasExtra(Constants.ADDRESSOBJECT)){
            val properties = Properties()
            properties.addAttribute(
                "Customer_Id",
                SharedPreferencesHelper.instance?.getString(
                    SharedPreferencesKeys.CUSTOMER_ID
                )!!)

                .setNonInteractive()

            sendMoEngageEvent("KA_Community_Add_Address_Click",properties)
        }
    }

    override fun onResume() {
        super.onResume()
        userProfileViewModel.getUsersData()
    }

    override fun refreshPage() {
        userProfileViewModel.getUsersData()
    }

    private fun setImage(imageUri: Uri) {

        Glide.with(this)
            .load(imageUri)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .fitCenter()
            .into(viewDataBinding.cvProfilePic)


        val bitmap = ImagePicker.getImageResized(this, imageUri)
        bitmap?.let { saveFilePath(it) }

    }

    private fun saveFilePath(bitmap: Bitmap) {
        val f = File(this.externalCacheDir!!.absolutePath, filename)
        try {
            f.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        try {
            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)

            bitmapData = bos.toByteArray()

            //write the bytes in file
            val fos = FileOutputStream(f)
            fos.write(bitmapData)
            fos.flush()
            fos.close()
        } catch (e: Exception) {
            externalCacheDir
            e.printStackTrace()
        }
        postImageModel?.postImage = f
        mViewModel?.updateProfile(null, null, null, f)
    }


    var cameraIntentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data

                imageUri = ImagePicker.getImageFromResult(this, result.resultCode, data)
                startCropImageActivity(imageUri)


            }
        }


    private fun startCropImageActivity(currentImageUri: Uri?) {

        if (currentImageUri == null) {
            return
        }
        cropImage?.launch(
            options(uri = currentImageUri) {
                setGuidelines(CropImageView.Guidelines.ON)
                setCropMenuCropButtonTitle(getString(R.string.ok))
                setOutputCompressFormat(Bitmap.CompressFormat.PNG)

            }
        )

    }


    private var imageSelectDialog = FilePicker()
    override fun showImageSelect(file: FilePicker, onCamera: () -> Unit, onGallery: () -> Unit) {
        imageSelectDialog = file
        imageSelectDialog.setOnOptionSelectedListener(onCamera, onGallery)
        imageSelectDialog.show(supportFragmentManager, "image")
    }

    override fun getLayoutID(): Int {
        return R.layout.user_profile_activity
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): UserProfileViewModel {
        return userProfileViewModel
    }


    override fun openCameraToCapture() {
        val cameraIntent = ImagePicker.getCameraIntent(this)
        cameraIntentLauncher.launch(cameraIntent)

    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (permissions.contains("android.permission.CAMERA")) {
                openCameraToCapture()
            }
        }
    }

    override fun sendSaveProfileImageMoengageEvent() {
        val properties = Properties()
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(this, "KA_Save_Profile_Image", properties)
    }

    override fun setImage(profileImage: String) {
        Glide.with(this)
            .load(profileImage)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .fitCenter()
            .into(viewDataBinding.cvProfilePic)
    }


}