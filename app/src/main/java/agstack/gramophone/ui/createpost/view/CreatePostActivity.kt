package agstack.gramophone.ui.createpost.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityCreatePostBinding
import agstack.gramophone.ui.createpost.CreatePostNavigator
import agstack.gramophone.ui.createpost.viewmodel.CreatePostViewModel
import agstack.gramophone.ui.dialog.LanguageBottomSheetFragment
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.ImagePicker
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.google.zxing.integration.android.IntentIntegrator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@AndroidEntryPoint
class CreatePostActivity: BaseActivityWrapper<ActivityCreatePostBinding, CreatePostNavigator, CreatePostViewModel>(),
CreatePostNavigator,
LanguageBottomSheetFragment.LanguageUpdateListener {
    val REQUEST_CODE = 0x0000c0de
    private var imageUri: Uri? = null
    private var bitmapData: ByteArray? = null
    private val filename = "image"
    private var imageNo: Int = 0
    private var cropImage: ActivityResultLauncher<CropImageContractOptions>? = null;
    private val createPostViewModel: CreatePostViewModel by viewModels()
    var qrLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val result =
                    IntentIntegrator.parseActivityResult(REQUEST_CODE, result.resultCode, data)
                tvCodeApplied.text =
                    getMessage(R.string.referral_code) + result.contents + getMessage(R.string.applied)
//                .referralCode.set(result.contents)
                rlHaveReferralCode.visibility = View.GONE
                rlAppliedCode.visibility = View.VISIBLE
            }
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
                setOutputCompressFormat(Bitmap.CompressFormat.PNG)
            }
        )

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        cropImage = registerForActivityResult(CropImageContract()) { result ->
            if (result.isSuccessful) {
                // use the returned uri
                if (ImagePicker.checkImageMinSize(result.cropRect!!)) {
                    val imageUri = result.uriContent
                    imageUri?.let {
                        when(imageNo){
                            Constants.IV_ONE->{
                                setImage(it, viewDataBinding.iveOne,"1")
                                viewDataBinding.ivPlusBig.visibility=GONE
                                viewDataBinding.ivDeletBig.visibility= VISIBLE

                            }
                            Constants.IV_TWO->{
                                setImage(it, viewDataBinding.ivTwo, "2")
                                viewDataBinding.ivPlusSmall1.visibility=GONE
                                viewDataBinding.ivDeleteSmall1.visibility= VISIBLE
                            }

                            Constants.IV_THREE->{
                                setImage(it, viewDataBinding.ivThree, "3")
                                viewDataBinding.ivPlusSmall2.visibility=GONE
                                viewDataBinding.ivDeleteSmall2.visibility= VISIBLE
                            }
                        }

                    }
                } else {
                    result?.uriContent?.let {
                        when(imageNo){
                            Constants.IV_ONE->{
                                setImage(it, viewDataBinding.iveOne, "1")
                                viewDataBinding.ivPlusBig.visibility=GONE
                                viewDataBinding.ivDeletBig.visibility= VISIBLE

                            }
                            Constants.IV_TWO->{
                                setImage(it, viewDataBinding.ivTwo, "2")
                                viewDataBinding.ivPlusSmall1.visibility=GONE
                                viewDataBinding.ivDeleteSmall1.visibility= VISIBLE
                            }

                            Constants.IV_THREE->{
                                setImage(it, viewDataBinding.ivThree, "3")
                                viewDataBinding.ivPlusSmall2.visibility=GONE
                                viewDataBinding.ivDeleteSmall2.visibility= VISIBLE
                            }
                        }
                    }
                }

            } else {
            }
        }
    }

    override fun getLayoutID(): Int = R.layout.activity_create_post

    override fun getViewModel(): CreatePostViewModel = createPostViewModel

    override fun onLanguageUpdate() = Unit

    override fun getBindingVariable(): Int = BR.viewModel

    private fun setImage(imageUri: Uri, image: ImageView, key: String) {


        Glide.with(this)
            .load(imageUri)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .fitCenter()
            .into(image)


        val bitmap = ImagePicker.getImageResized(this, imageUri)
        bitmap?.let { saveFilePath(it,key) }

    }

    private fun saveFilePath(bitmap: Bitmap,key:String) {
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
        createPostViewModel.listOfImages.put(key,f)
    }

    override fun openCameraToCapture() {
        this.imageNo = createPostViewModel.imageNo
        val cameraIntent = ImagePicker.getCameraIntent(this)
        cameraIntentLauncher.launch(cameraIntent)

    }

    override fun updateAddDeleteBtn(imageNo: Int) {
        when(imageNo){
            Constants.IV_ONE->{
                viewDataBinding.iveOne.setImageResource(R.drawable.preview_frame)
                viewDataBinding.ivPlusBig.visibility=VISIBLE
                viewDataBinding.ivDeletBig.visibility= GONE

            }
            Constants.IV_TWO->{
                viewDataBinding.ivTwo.setImageResource(R.drawable.preview_frame)
                viewDataBinding.ivPlusSmall1.visibility=VISIBLE
                viewDataBinding.ivDeleteSmall1.visibility= GONE
            }

            Constants.IV_THREE->{
                viewDataBinding.ivThree.setImageResource(R.drawable.preview_frame)
                viewDataBinding.ivPlusSmall2.visibility=VISIBLE
                viewDataBinding.ivDeleteSmall2.visibility= GONE
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (permissions.contains("android.permission.CAMERA")) {
                openCameraToCapture()
            }
        }
    }

}