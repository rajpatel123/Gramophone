package agstack.gramophone.ui.postdetails.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityPostDetailsBinding
import agstack.gramophone.ui.home.adapter.CommentsAdapter
import agstack.gramophone.ui.postdetails.PostDetailNavigator
import agstack.gramophone.ui.postdetails.viewmodel.PostDetailViewModel
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.ImagePicker
import agstack.gramophone.utils.ShareSheetPresenter
import agstack.gramophone.widget.FilePicker
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_post_details.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@AndroidEntryPoint
class PostDetailsActivity : BaseActivityWrapper<ActivityPostDetailsBinding,PostDetailNavigator,PostDetailViewModel>(),PostDetailNavigator {

    private val postDetailViewModel: PostDetailViewModel by viewModels()
    private var shareSheetPresenter: ShareSheetPresenter? = null
    private var cropImage: ActivityResultLauncher<CropImageContractOptions>? = null;
    private val filename = "image"
    private var imageUri: Uri? = null
    private var bitmapData: ByteArray? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.extras?.getString(Constants.POST_ID)?.let { postDetailViewModel.getPostDetails(it) }
        intent.extras?.getString(Constants.POST_ID)?.let { postDetailViewModel.getPostComments(it) }
        setUpToolBar(
            enableBackButton = true,
            "",
            R.drawable.ic_arrow_left
        )

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
        shareSheetPresenter = this?.let { ShareSheetPresenter(this) }
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

    private fun setImage(imageUri: Uri) {

        Glide.with(this)
            .load(imageUri)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .fitCenter()
            .into(viewDataBinding.postCommentImage)


        val bitmap = ImagePicker.getImageResized(this, imageUri)
        bitmap?.let { saveFilePath(it) }

        viewDataBinding.cvPostImage.visibility = View.VISIBLE

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
        postDetailViewModel.postImage.set(f)

    }

    override fun getLayoutID(): Int = R.layout.activity_post_details

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): PostDetailViewModel = postDetailViewModel
    override fun updatePostList(
        comments: CommentsAdapter,
        postDetailClicked: (commentId: String) -> Unit
    ) {

        rvComments.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvComments.setHasFixedSize(false)
        rvComments.adapter = comments
    }

    override fun onImageSet(url: String) {
        Glide.with(this).load(url).into(postImage)
    }

    override fun setLikeImage(icLiked: Int) {
        ivLike.setImageResource(icLiked)
    }

    override fun setBookMarkImage(icLiked: Int) {
        ivBookmark.setImageResource(icLiked)
    }

    private var imageSelectDialog = FilePicker()
    override fun showImageSelect(file: FilePicker, onCamera: () -> Unit, onGallery: () -> Unit) {
        imageSelectDialog = file
        imageSelectDialog.setOnOptionSelectedListener(onCamera, onGallery)
        imageSelectDialog.show(supportFragmentManager, "image")
    }

    override fun openCameraToCapture() {
        val cameraIntent = ImagePicker.getCameraIntent(this)
        cameraIntentLauncher.launch(cameraIntent)

    }

    override fun clearImage() {
        postDetailViewModel.postImage.set(null)
        viewDataBinding.cvPostImage.visibility= View.GONE
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
    override fun sharePost(link: String) {

        val whatsappIntent = Intent(Intent.ACTION_SEND)
        whatsappIntent.type = "text/plain"
        whatsappIntent.setPackage("com.whatsapp")
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, link)
        try {
            startActivity(whatsappIntent)
        } catch (ex: ActivityNotFoundException) {
            showToast("Whatsapp have not been installed.")
        }

    }

}