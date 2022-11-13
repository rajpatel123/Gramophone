package agstack.gramophone.ui.comments.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityCommentsBinding
import agstack.gramophone.ui.comments.CommentNavigator
import agstack.gramophone.ui.comments.model.Data
import agstack.gramophone.ui.comments.viewModel.CommentsViewModel
import agstack.gramophone.ui.home.adapter.CommentsAdapter
import agstack.gramophone.utils.Constants.POST_ID
import agstack.gramophone.utils.ImagePicker
import agstack.gramophone.widget.FilePicker
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.amnix.xtension.extensions.hideKeyboard
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_comments.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@AndroidEntryPoint
class CommentsActivity :
    BaseActivityWrapper<ActivityCommentsBinding, CommentNavigator, CommentsViewModel>(),
    CommentNavigator {
    private val commentsViewModel: CommentsViewModel by viewModels()
    private var cropImage: ActivityResultLauncher<CropImageContractOptions>? = null;
    private val filename = "image"
    private var imageUri: Uri? = null
    private var bitmapData: ByteArray? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        commentsViewModel.getComments(intent.extras?.getString(POST_ID)!!)
        tvCommentBottom.requestFocus()
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager;
        imm.showSoftInput(tvCommentBottom, InputMethodManager.SHOW_IMPLICIT);


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
    }

    private fun setImage(imageUri: Uri) {

        Glide.with(this)
            .load(imageUri)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .fitCenter()
            .into(viewDataBinding.postImage)


        val bitmap = ImagePicker.getImageResized(this, imageUri)
        bitmap?.let { saveFilePath(it) }

        viewDataBinding.cvPostImage.visibility = VISIBLE

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
        commentsViewModel.postImage.set(f)

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
        commentsViewModel.postImage.set(null)
        viewDataBinding.cvPostImage.visibility= GONE
    }

    override fun populateCommentData(data: Data) {
        viewDataBinding.tvCommentBottom.setText(data.text)
    }

    override fun showImage(it: Data) {
       viewDataBinding.ivFullImage.hideKeyboard()

        Glide.with(this)
        .load(it.image)
        .into(viewDataBinding.ivFullImage)
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

    override fun getLayoutID(): Int = R.layout.activity_comments

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): CommentsViewModel = commentsViewModel


    override fun updateCommentsList(
        commentsAdapter: CommentsAdapter,
        onDeleteComment: (data: Data) -> Unit,
        onEditCommentMenuClicked: (data: Data) -> Unit,
        onImageViewClciked: (data: Data) -> Unit
    ) {
        commentsAdapter.onImageViewClciked = onImageViewClciked
        commentsAdapter.onDeleteComment = onDeleteComment
        commentsAdapter.onEditCommentMenuClicked = onEditCommentMenuClicked
        rvCommentsDialog?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvCommentsDialog?.setHasFixedSize(false)
        rvCommentsDialog?.adapter = commentsAdapter
    }

    override fun finishActivity() {
        finish()
    }


}