package agstack.gramophone.ui.createpost.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityCreatePostBinding
import agstack.gramophone.ui.createnewpost.model.PostDetailsModel
import agstack.gramophone.ui.createpost.CreatePostNavigator
import agstack.gramophone.ui.createpost.viewmodel.CreatePostViewModel
import agstack.gramophone.ui.dialog.LanguageBottomSheetFragment
import agstack.gramophone.ui.tagandmention.Tag
import agstack.gramophone.ui.tagandmention.TagAdapter
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.ImagePicker
import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
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
import com.leocardz.link.preview.library.TextCrawler
import com.tokenautocomplete.TagTokenizer
import com.tokenautocomplete.TokenCompleteTextView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import java.util.regex.Pattern

@AndroidEntryPoint
class CreatePostActivity: BaseActivityWrapper<ActivityCreatePostBinding, CreatePostNavigator, CreatePostViewModel>(),
CreatePostNavigator, TokenCompleteTextView.TokenListener<Tag?>,
LanguageBottomSheetFragment.LanguageUpdateListener {
    val REQUEST_CODE = 0x0000c0de
    private var imageUri: Uri? = null
    private var bitmapData: ByteArray? = null
    private val filename = "image"
    private var imageNo: Int = 0
    protected var textWatcher: TextWatcher? = null
    private var startSuggestion = false
    private var searchText: String? = null
    private var startPosition: Int? = null
    private var timer: Timer? = Timer()
    private var gramophoneTvUrl: String? = null
    private var mLastClickTime: Long = 0
    private val DELAY: Long = 1000 // in ms
    private var urlLink: String? = null
    private var urlFromIntent: String? = null
    private var textCrawler: TextCrawler? = null
    private var currentImageSet: Array<Bitmap?>?=null
    private var clipData: ClipData.Item? = null
    private var imageFromIntent: String? = null
    private var isPreviewFromIntent = false
    private var urlPreviewImage: String? = null
    private var urlPreviewDescription: String? = null
    private var urlPreviewOff = false
    private var isImageFileFromHttpUrl = false
    private var sharedImageFile: File? = null
    private var sharedImageBitmap: Bitmap? = null
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

        initiateTextWatcher()

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

    override fun populateTagSuggestionList(tags: Array<Tag>) {
        viewDataBinding!!.descriptionEditText?.setAdapter(TagAdapter(this, R.layout.tag_layout, tags))

    }

    override fun populateHasTagList(tags: Array<Tag>) {
        viewDataBinding!!.descriptionEditText?.setAdapter(TagAdapter(this, R.layout.tag_layout, tags))
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


    fun initiateTextWatcher() {
            textWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    var text = s.toString()
                    if (text != null) {
                        text = Html.fromHtml(text).toString()
                    }
                    if (!startSuggestion) {
                        if (text != null && text?.length > 0 && (text[text?.length - 1] == '#' || text[text?.length - 1] == '@')) {
                            startSuggestion = true
                            startPosition = text?.length - 1
                        } else if (text != null && text?.length > 0 && text[text?.length - 1] == ' ') {
                            startSuggestion = false
                            searchText = null
                            startPosition = null
                        }
                    } else {
                        if (text != null && text?.length > 0 && text[text?.length - 1] == ' ') {
                            startSuggestion = false
                            searchText = null
                            startPosition = null
                        } else if (startPosition != null && text?.length > 0 && text?.length - 1 < startPosition!!) {
                            startSuggestion = false
                            searchText = null
                            startPosition = null
                        }
                    }
                    if (timer != null) timer!!.cancel()
                }

                override fun afterTextChanged(s: Editable) {
                    //avoid triggering event when text is too short wait if he has paused typing
                    var text = s.toString()
                    text = Html.fromHtml(text).toString()
                    if (startSuggestion) {
                        if (startPosition != null && text?.length > startPosition!!) searchText = text?.substring(startPosition!!)
                        if (searchText != null && searchText!![0] == '@' && searchText!!.length > 1) {
                            createPostViewModel!!.getMentionSuggestion(searchText!!.substring(1))
                        } else if (searchText != null && searchText!![0] == '#' && searchText!!.length > 1) {
                            createPostViewModel!!.getSearchSuggestion(searchText!!.substring(1))
                        }
                    } else {
                        timer = null
                        timer = Timer()
                        val finalText = text
                        timer!!.schedule(object : TimerTask() {
                            override fun run() {
                                // you will probably need to use
                                // runOnUiThread(Runnable action) for some specific
                                // actions
                                Log.i("Create Post", "handle input")
                                if (gramophoneTvUrl == null) {
                                    handleEditTextInput(finalText)
                                }
                                timer = null
                            }
                        }, DELAY)
                    }
                }
            }
            viewDataBinding!!.descriptionEditText?.performBestGuess(false)
            viewDataBinding!!.descriptionEditText?.preventFreeFormText(false)
            viewDataBinding!!.descriptionEditText?.setTokenizer(TagTokenizer(Arrays.asList('#', '@')))
            //    binding?.commentEditText?.setAdapter(new TagAdapter(this, R.layout.tag_layout, Tag.sampleTags()));
            viewDataBinding!!.descriptionEditText?.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Select)
            viewDataBinding!!.descriptionEditText?.threshold = 1
            viewDataBinding!!.descriptionEditText?.setTokenListener(this)
            viewDataBinding!!.descriptionEditText?.addTextChangedListener(textWatcher)

    }


    fun handleEditTextInput(text: String?) {
        if (urlLink == null && !urlPreviewOff) {
            // afterTextChangedCount++;
            //  String text = Html.fromHtml(text).toString();
            var descriptionUrl: String? = null
            if (text != null) {
                val urlList: List<String> = pullLinks(text)
                if (urlList.size > 0) {
                    descriptionUrl = urlList[0]
                    if (descriptionUrl != null) {
                        urlFromIntent = descriptionUrl
                        val isPreviewFromEditText = true
                       // initUrlPreview(descriptionUrl)
                    }
                }
            }
        }
    }



    fun pullLinks(text: String?): ArrayList<String> {
        val links = ArrayList<String>()
        //String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";
        val regex = "\\(?\\b(https?://|www[.]|ftp://)[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]"
        val p = Pattern.compile(regex)
        val m = p.matcher(text)
        while (m.find()) {
            var urlStr = m.group()
            if (urlStr.startsWith("(") && urlStr.endsWith(")")) {
                urlStr = urlStr.substring(1, urlStr.length - 1)
            }
            links.add(urlStr)
        }
        return links
    }




    override fun enablePostButton() {
    }

    override fun disablePostButton() {
    }

    override fun gotoSocialPage(post: PostDetailsModel) {
    }

    override fun populatePostDetails(postDetailsModel: PostDetailsModel) {
    }

    override fun onTokenAdded(token: Tag?) {
        startSuggestion = false
        searchText = null
        startPosition = null
    }

    override fun onTokenRemoved(token: Tag?) {
    }

    override fun onTokenIgnored(token: Tag?) {
    }
}