package agstack.gramophone.view.activity


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.CreatePostsActivityBinding
import agstack.gramophone.ui.createnewpost.adapter.TagsCropAdapter
import agstack.gramophone.ui.createnewpost.model.AgriTag
import agstack.gramophone.ui.createnewpost.model.AgriTagListResult
import agstack.gramophone.ui.createnewpost.model.MySingleton
import agstack.gramophone.ui.createnewpost.model.PostDetailsModel
import agstack.gramophone.ui.createnewpost.view.*
import agstack.gramophone.ui.createpost.CreatePostNavigator
import agstack.gramophone.ui.createpost.viewmodel.CreatePostViewModel
import agstack.gramophone.ui.dialog.posts.BottomSheetCropsDialog
import agstack.gramophone.ui.home.view.HomeActivity
import agstack.gramophone.ui.home.view.fragments.market.model.CropData
import agstack.gramophone.ui.tagandmention.ExpandableTextView
import agstack.gramophone.ui.tagandmention.Tag
import agstack.gramophone.ui.tagandmention.TagAdapter
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.ImagePicker
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.app.Activity
import android.app.ProgressDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.amnix.xtension.extensions.inflater
import com.amnix.xtension.extensions.isNotNullOrEmpty
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.leocardz.link.preview.library.LinkPreviewCallback
import com.leocardz.link.preview.library.SourceContent
import com.leocardz.link.preview.library.TextCrawler
import com.tokenautocomplete.TagTokenizer
import com.tokenautocomplete.TokenCompleteTextView
import com.tokenautocomplete.TokenCompleteTextView.TokenListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.item_tags.view.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import java.util.regex.Pattern

//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;
@AndroidEntryPoint
class CreatePostActivity :
    BaseActivityWrapper<CreatePostsActivityBinding, CreatePostNavigator, CreatePostViewModel>(),
    CreatePostNavigator, View.OnClickListener, TokenListener<Tag?>,
    BottomSheetCropsDialog.OnSelectionDone {
    private val presenter: CreatePostViewModel by viewModels()
    var layoutManager: LinearLayoutManager? = null
    var hashMap: HashMap<String?, String?> = HashMap<String?, String?>()
    protected var imageView: ImageView? = null
    protected var progressBar: ProgressBar? = null
    protected var txtUrlTitleDescriptionPreview: ExpandableTextView? = null
    private var sharedImagePath: String? = null
    private var isFileFromDevice = false

    val REQUEST_CODE = 0x0000c0de
    private var imageUri: Uri? = null
    private var bitmapData: ByteArray? = null
    private val filename = "image"
    private var imageNo: Int = 0
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
    //   private var intent: Intent? = null
    private var selectedAgriTag: List<AgriTag>? = ArrayList()
    protected var imageContainer: RelativeLayout? = null
    protected var isImageAvailable = false
    private val isLoginCalled = false
    private val afterTextChangedCount = 0
    var addImageButton: ImageView? = null
    protected var textWatcher: TextWatcher? = null
    private var progressDialog: ProgressDialog? = null
    var gson = Gson()
    var json: String? = null
    private val imagesFiles: MutableList<File> = ArrayList()
    private var description: String? = null
    var myClipboard: ClipboardManager? = null

    var cameraIntentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data

                imageUri = ImagePicker.getImageFromResult(this, result.resultCode, data)
                startCropImageActivity(imageUri)


            }
        }



    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolBar(
            enableBackButton = true,
            getMessage(R.string.create_post),
            R.drawable.ic_cross
        )


        viewDataBinding.myToolbar.myToolbar.setBackgroundResource(R.color.brand_color)
        viewDataBinding.myToolbar.myToolbar.setTitleTextColor(
            ContextCompat.getColor(
                this,
                R.color.white
            )
        )
        viewDataBinding.myToolbar.myToolbar.navigationIcon =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_cross_white, null)
        if (MySingleton.getInstance().isOffTokenAutoComplete) {
            viewDataBinding?.descriptionEditText?.visibility = View.GONE
            viewDataBinding.hashSymbol?.visibility = View.GONE
            viewDataBinding.atSymbol?.visibility = View.GONE
            viewDataBinding.descriptionNoTokenEditText?.visibility = View.VISIBLE
        } else {
            viewDataBinding.descriptionNoTokenEditText?.visibility = View.GONE
            viewDataBinding.descriptionEditText?.visibility = View.VISIBLE
            viewDataBinding.hashSymbol?.visibility = View.VISIBLE
            viewDataBinding.atSymbol?.visibility = View.VISIBLE
        }
        presenter.description.set("")
        presenter.getCrops()
        // initReceiver()
        intiEasyImagePicker()
        //intent = getIntent()
        initiateTextWatcher()
        AgriTagListResult.getInstance().selectedTagList.clear()
        textCrawler = TextCrawler()
        progressDialog = ProgressDialog(this)
        txtUrlTitleDescriptionPreview = findViewById(R.id.urlTitleDescriptionText)
        addImageButton = findViewById(R.id.btnAddImageTitle)
        val actionBar = supportActionBar
        actionBar!!.setTitle(getString(R.string.create_post))
        if (intent.getExtras() != null) {
            if (intent.getExtras()!!.containsKey(Constants.sharedContent)) {
                MySingleton.getInstance().shareIntent
                handleShareOtherLink(MySingleton.getInstance().shareIntent)
                MySingleton.getInstance().shareIntent = null
            } else {
                handleShareOtherLink(intent)
            }
        } else {
            handleShareOtherLink(intent)
        }
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true)
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setTitle(getString(R.string.create_post))
        }
        viewDataBinding.buttonAddTag.setOnClickListener {
            launchTagList()
        }
        viewDataBinding.postButton?.setOnClickListener(View.OnClickListener {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return@OnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            if (selectedAgriTag != null && selectedAgriTag!!.size > 0) {
                callApi()
            } else {
                showAddTagAlert()
            }
        })

        viewDataBinding.hashSymbol?.setOnClickListener { view: View? ->
            viewDataBinding.descriptionEditText?.append("#")
            viewDataBinding.descriptionEditText?.requestFocus()
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.showSoftInput(
                viewDataBinding.descriptionEditText,
                InputMethodManager.SHOW_IMPLICIT
            )
        }


        viewDataBinding.dollerSymbol?.setOnClickListener { view: View? ->
            viewDataBinding.descriptionEditText?.append("$")
            viewDataBinding.descriptionEditText?.requestFocus()
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.showSoftInput(
                viewDataBinding.descriptionEditText,
                InputMethodManager.SHOW_IMPLICIT
            )
        }
        viewDataBinding.atSymbol?.setOnClickListener { view: View? ->
            viewDataBinding.descriptionEditText?.append("@")
            //            viewDataBinding.descriptionEditText?.setFocusable(true);
//            viewDataBinding.descriptionEditText?.setCursorVisible(true);
            viewDataBinding.descriptionEditText?.requestFocus()
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.showSoftInput(
                viewDataBinding.descriptionEditText,
                InputMethodManager.SHOW_IMPLICIT
            )
        }



        viewDataBinding.urlPreviewRemoveImageButton?.setOnClickListener {
            viewDataBinding.urlPreviewImageContainer?.visibility = View.GONE
            viewDataBinding.urlTitleDescriptionText?.text = ""
            viewDataBinding.urlPreviewImageView?.setImageResource(R.drawable.ic_stub)
            urlLink = null
            viewDataBinding.imageContainer?.visibility = View.VISIBLE
            openConfirmUrlPreviewOffDialog()
        }

        cropImage = registerForActivityResult(CropImageContract()) { result ->
            if (result.isSuccessful) {
                // use the returned uri
                if (ImagePicker.checkImageMinSize(result.cropRect!!)) {
                    val imageUri = result.uriContent
                    imageUri?.let {
                        when(imageNo){
                            Constants.IV_ONE->{
                                setImage(it, viewDataBinding.iveOne,"1")
                                viewDataBinding.ivPlusBig.visibility= View.GONE
                                viewDataBinding.ivDeletBig.visibility= View.VISIBLE

                            }
                            Constants.IV_TWO->{
                                setImage(it, viewDataBinding.ivTwo, "2")
                                viewDataBinding.ivPlusSmall1.visibility= View.GONE
                                viewDataBinding.ivDeleteSmall1.visibility= View.VISIBLE
                            }

                            Constants.IV_THREE->{
                                setImage(it, viewDataBinding.ivThree, "3")
                                viewDataBinding.ivPlusSmall2.visibility= View.GONE
                                viewDataBinding.ivDeleteSmall2.visibility= View.VISIBLE
                            }
                        }

                    }
                } else {
                    result?.uriContent?.let {
                        when(imageNo){
                            Constants.IV_ONE->{
                                setImage(it, viewDataBinding.iveOne, "1")
                                viewDataBinding.ivPlusBig.visibility= View.GONE
                                viewDataBinding.ivDeletBig.visibility= View.VISIBLE

                            }
                            Constants.IV_TWO->{
                                setImage(it, viewDataBinding.ivTwo, "2")
                                viewDataBinding.ivPlusSmall1.visibility= View.GONE
                                viewDataBinding.ivDeleteSmall1.visibility= View.VISIBLE
                            }

                            Constants.IV_THREE->{
                                setImage(it, viewDataBinding.ivThree, "3")
                                viewDataBinding.ivPlusSmall2.visibility= View.GONE
                                viewDataBinding.ivDeleteSmall2.visibility= View.VISIBLE
                            }
                        }
                    }
                }

            } else {
            }
        }


    }



    private fun intiEasyImagePicker() {

    }


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
        val f = File(this.externalCacheDir!!.absolutePath, key)
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
        presenter.listOfImages.put(key, f)
        viewDataBinding.postButton.setBackgroundResource(R.color.brand_color)
        viewDataBinding.postButton.setTextColor(
            ContextCompat.getColor(
                this@CreatePostActivity,
                R.color.white
            )
        )


    }

    private fun launchTagList() {
        val bottomSheet = BottomSheetCropsDialog(presenter.cropResponse, this)
        bottomSheet.show(
            getSupportFragmentManager(),
            getMessage(R.string.bottomsheet_tag)
        )
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
    private fun openConfirmUrlPreviewOffDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.confirm_url_preview_off_post)
            .setNegativeButton(R.string.button_title_cancel, null)
            .setPositiveButton(R.string.button_ok) { dialogInterface, i ->
                urlPreviewOff = true

                //       removePost(post);
            }
        builder.create().show()
    }

    private fun showAddTagAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.add_tag_message))
        builder.setTitle(getString(R.string.add_tag_title))
        builder.setPositiveButton(R.string.add_tag_title) { dialog, which ->
            launchTagList()
        }
        builder.setNegativeButton(R.string.button_title_cancel) { dialog, which ->
            dialog.dismiss()
            callApi()
        }
        builder.setCancelable(true)
        builder.create()
        builder.show()
    }

    private fun callApi() {
//        if (presenter!!.isProfileComplete) {
//            if (checkIfEmptyPost()) {
//                if (isPreviewFromIntent) {
//                    presenter!!.createUrlPreviewFromIntentPost(description, urlPreviewDescription, urlPreviewImage, selectedAgriTag as ArrayList<AgriTag>?, urlLink, postImageModel)
//                } else {
//                    presenter!!.createPost(description, selectedAgriTag as ArrayList<AgriTag>?, urlLink, postImageModel)
//                }
//            } else {
//                if (viewDataBinding.descriptionNoTokenEditText?.visibility == View.VISIBLE) {
//                    Snackbar.make(viewDataBinding.descriptionNoTokenEditText, getString(R.string.warning_empty_input), Snackbar.LENGTH_LONG).show()
//                } else {
//                    Snackbar.make(viewDataBinding.descriptionEditText, getString(R.string.warning_empty_input), Snackbar.LENGTH_LONG).show()
//                }
//            }
//        } else {
//            showProfileCompleteAlert()
//        }
    }

    private fun checkIfEmptyPost(): Boolean {
        if (viewDataBinding.descriptionNoTokenEditText?.visibility == View.VISIBLE && viewDataBinding.descriptionNoTokenEditText?.text != null) {
            description =
                viewDataBinding.descriptionNoTokenEditText?.text?.toString()?.trim { it <= ' ' }
        } else if (viewDataBinding.descriptionEditText?.visibility == View.VISIBLE && viewDataBinding.descriptionEditText?.text != null) {
            description = viewDataBinding.descriptionEditText?.text?.toString()?.trim { it <= ' ' }
        }
        return if (description != null && !description!!.isEmpty()) {
            true
        } else if (urlLink != null && !urlLink!!.isEmpty()) {
            true
        } else true
    }

    override fun onClick(v: View) {}


    override fun gotoSocialPage(postDetailsModel: PostDetailsModel) {
        if (sharedImageFile != null && sharedImageFile!!.exists()) {
            sharedImageFile!!.delete()
            sharedImageFile = null
            if (sharedImageBitmap != null) {
                sharedImageBitmap = null
            }
        }
        //GOTO Home
    }

    override fun populatePostDetails(postDetailsModel: PostDetailsModel) {
    }

    override fun showTags(selectedTagList: MutableList<CropData>) {
    }

    override fun getPostDetails() {
    }

    override fun loadImages(images: List<agstack.gramophone.ui.postdetails.model.Image>) {
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
    override fun openCameraToCapture() {
        this.imageNo = presenter.imageNo
        val cameraIntent = ImagePicker.getCameraIntent(this)
        cameraIntentLauncher.launch(cameraIntent)
    }

    override fun updateAddDeleteBtn(imageNo: Int) {
        when(imageNo){
            Constants.IV_ONE->{
                viewDataBinding.iveOne.setImageResource(R.drawable.preview_frame)
                viewDataBinding.ivPlusBig.visibility= View.VISIBLE
                viewDataBinding.ivDeletBig.visibility= View.GONE

            }
            Constants.IV_TWO->{
                viewDataBinding.ivTwo.setImageResource(R.drawable.preview_frame)
                viewDataBinding.ivPlusSmall1.visibility = View.VISIBLE
                viewDataBinding.ivDeleteSmall1.visibility = View.GONE
            }

            Constants.IV_THREE -> {
                viewDataBinding.ivThree.setImageResource(R.drawable.preview_frame)
                viewDataBinding.ivPlusSmall2.visibility = View.VISIBLE
                viewDataBinding.ivDeleteSmall2.visibility = View.GONE
            }
        }

        if (presenter.listOfImages.size < 1) {
            if (presenter.listOfImages.size < 1) {
                viewDataBinding.postButton.setBackgroundResource(R.color.btn_disabled)
                viewDataBinding.postButton.setTextColor(
                    ContextCompat.getColor(
                        this@CreatePostActivity,
                        R.color.blakish
                    )
                )
            }
        }
    }



    override fun populateTagSuggestionList(tags: Array<Tag>) {
        viewDataBinding.descriptionEditText?.setAdapter(TagAdapter(this, R.layout.tag_layout, tags))
    }

    override fun populateProblemList(tagArray: Array<Tag>) {
        viewDataBinding.descriptionEditText?.setAdapter(TagAdapter(this, R.layout.tag_layout, tagArray))
    }

    override fun getText() :String {
        return viewDataBinding.descriptionEditText.editableText.toString()
    }

    override fun setDesc(toString: String) {
    }

    override fun getDesc(): String? {
        return null
    }


    override fun populateHasTagList(tags: Array<Tag>) {
        try {
            viewDataBinding.descriptionEditText?.setAdapter(
                TagAdapter(
                    this,
                    R.layout.tag_layout,
                    tags
                )
            )
        }catch (ex:Exception){
            ex.printStackTrace()
        }
    }

    override fun enablePostButton() {
        viewDataBinding.postButton?.isEnabled = true
    }

    override fun disablePostButton() {
        viewDataBinding.postButton?.isEnabled = false
    }


    fun showTag(selectedTagList: MutableList<CropData>) {
        if (selectedTagList != null && selectedTagList?.size > 0) {
            viewDataBinding.finalTagLL.removeAllViews()
            selectedTagList.forEach {
                val view = inflater.inflate(R.layout.item_tags, null)
                view.tvTag.setText(it.cropName)
                viewDataBinding.finalTagLL.addView(view)
            }
        }


//        val tagsCropAdapter = TagsCropAdapter(selectedTagList)
//        viewDataBinding.finalTagRecyclerView.adapter = tagsCropAdapter
//        viewDataBinding.finalTagRecyclerView.layoutManager =
//            StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL)
//        //    getSelectedTagList(selectedTagList, createOnSelectedTagChangedListner());
    }

    fun initiateTextWatcher() {


        //  viewDataBinding.descriptionEditText?.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        if (viewDataBinding.descriptionNoTokenEditText?.visibility == View.VISIBLE) {
            textWatcher = object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (timer != null) timer!!.cancel()
                }

                override fun afterTextChanged(s: Editable) {
                    //avoid triggering event when text is too short
                    timer = Timer()
                    timer!!.schedule(object : TimerTask() {
                        override fun run() {
                            // TODO: do what you need here (refresh list)
                            // you will probably need to use
                            // runOnUiThread(Runnable action) for some specific
                            // actions
                            Log.i("Create Post", "handle input")
                            if (gramophoneTvUrl == null) {
                                var text = s.toString()
                                text = Html.fromHtml(text).toString()
                                handleEditTextInput(text)
                            }
                            timer = null
                        }
                    }, DELAY)
                }
            }
            viewDataBinding.descriptionNoTokenEditText?.addTextChangedListener(textWatcher)
        } else {
            textWatcher = object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    var text = s.toString()
                    if (text.isNotNullOrEmpty()){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            text = Html.fromHtml(text, 0).toString()
                        } else {
                            text = Html.fromHtml(text).toString()
                        }
                        viewDataBinding.postButton.setBackgroundResource(R.color.brand_color)
                        viewDataBinding.postButton.setTextColor(
                            ContextCompat.getColor(
                                this@CreatePostActivity,
                                R.color.white
                            )
                        )
                    }else{
                        if (presenter.listOfImages.size==0 ){
                            viewDataBinding.postButton.setBackgroundResource(R.color.brand_color)
                            viewDataBinding.postButton.setTextColor(
                                ContextCompat.getColor(
                                    this@CreatePostActivity,
                                    R.color.white
                                )
                            )
                        }

                    }


                    if (!startSuggestion) {
                        if (text != null && text?.length > 0 && (text[text?.length - 1] == '#' || text[text?.length - 1] == '@' || text[text?.length - 1] == '$')) {
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
                    if (text.isNotNullOrEmpty()){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            text = Html.fromHtml(text, 0).toString()
                        } else {
                            text = Html.fromHtml(text).toString()
                        }
                        viewDataBinding.postButton.setBackgroundResource(R.color.brand_color)
                        viewDataBinding.postButton.setTextColor(
                            ContextCompat.getColor(
                                this@CreatePostActivity,
                                R.color.white
                            )
                        )
                    }else{
                        if (presenter.listOfImages.size==0 ){
                            viewDataBinding.postButton.setBackgroundResource(R.color.brand_color)
                            viewDataBinding.postButton.setTextColor(
                                ContextCompat.getColor(
                                    this@CreatePostActivity,
                                    R.color.white
                                )
                            )
                        }

                    }

                    if (startSuggestion) {
                        if (startPosition != null && text?.length > startPosition!!) searchText =
                            text?.substring(startPosition!!)
                        if (searchText != null && searchText!![0] == '@' && searchText!!.length > 1) {
                            presenter!!.getMentionSuggestion(searchText!!.substring(1))
                        } else if (searchText != null && searchText!![0] == '#' && searchText!!.length > 1) {
                            presenter!!.getSearchSuggestion(searchText!!.substring(1))
                        }else if (searchText != null && searchText!![0] == '$' && searchText!!.length > 1) {
                            presenter!!.getProblems(searchText!!.substring(1))
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
            viewDataBinding.descriptionEditText?.performBestGuess(false)
            viewDataBinding.descriptionEditText?.preventFreeFormText(false)
            viewDataBinding.descriptionEditText?.setTokenizer(TagTokenizer(Arrays.asList('#', '@','$')))
            //    viewDataBinding.commentEditText?.setAdapter(new TagAdapter(this, R.layout.tag_layout, Tag.sampleTags()));
            viewDataBinding.descriptionEditText?.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Select)
            viewDataBinding.descriptionEditText?.threshold = 1
            viewDataBinding.descriptionEditText?.setTokenListener(this)
            viewDataBinding.descriptionEditText?.addTextChangedListener(textWatcher)
        }
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
                        initUrlPreview(descriptionUrl)
                    }
                }
            }
        }
    }

    private fun initUrlPreview(text: String) {
        try {
            textCrawler!!.makePreview(callback, text)
            viewDataBinding.urlPreviewImageContainer.visibility = View.VISIBLE
            viewDataBinding.urlPreviewProgressBar.visibility = View.VISIBLE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun pullLinks(text: String?): ArrayList<String> {
        val links = ArrayList<String>()
        //String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";
        val regex =
            "\\(?\\b(https?://|www[.]|ftp://)[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]"
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

    private val callback: LinkPreviewCallback = object : LinkPreviewCallback {
        private val linearLayout: LinearLayout? = null
        override fun onPre() {
            //  hideKeyboard();
            val currentImage: Bitmap? = null
            currentImageSet = null
            //showProgress();
        }

        override fun onPos(sourceContent: SourceContent, b: Boolean) {
            if (b || sourceContent.finalUrl == "") {
                /*
                  Inflating the content layout into Main View LinearLayout
                 */
                // urlLink=null;
                viewDataBinding.urlPreviewImageContainer.visibility = View.GONE
                viewDataBinding.urlPreviewProgressBar.visibility = View.GONE
                val failed = layoutInflater.inflate(
                    R.layout.failed,
                    linearLayout
                )
                val titleTextView = failed
                    .findViewById<TextView>(R.id.text)
                titleTextView.text = """
                    ${getString(R.string.failed_preview)}
                    ${sourceContent.finalUrl}
                    """.trimIndent()
                failed.setOnClickListener {
                    //                            releasePreviewArea();
                }
                previewDataFromIntentExtraValues
                // hideProgress();
            } else {
                if (sourceContent.cannonicalUrl != null && sourceContent.cannonicalUrl == Constants.IsUrlForPlayStore) {
                    previewDataFromIntentExtraValues
                } else {
                    viewDataBinding.urlPreviewImageContainer.visibility = View.VISIBLE
                    viewDataBinding.urlPreviewProgressBar.visibility = View.GONE
                    isPreviewFromIntent = false
                    if (sourceContent.title != null && sourceContent.description != null && sourceContent.finalUrl != null) {
                        txtUrlTitleDescriptionPreview!!.text = """
                            ${sourceContent.title}
                            ${sourceContent.description}
                            """.trimIndent()
                        if (txtUrlTitleDescriptionPreview != null) {
                            txtUrlTitleDescriptionPreview!!.visibility = View.VISIBLE
                            // finalDescription=String.valueOf(sourceContent.getTitle ()+"\n"+sourceContent.getDescription()+"\n"+"For mor detail Click on link");

                            //   finalDescription = txtUrlTitleDescriptionPreview.getText().toString();
                            urlLink = sourceContent.finalUrl.toString()
                            viewDataBinding.urlPreviewRemoveImageButton?.visibility = View.GONE
                            addImageButton!!.isEnabled = false
                            if (urlFromIntent != null) {
                                urlLink = urlFromIntent
                            }
                        }
                    } else if (sourceContent.finalUrl != null) {
                        // descriptionEditText?.setEnabled(false);
                        addImageButton!!.isEnabled = false
                        urlLink = sourceContent.finalUrl.toString()
                        if (urlFromIntent != null) {
                            urlLink = urlFromIntent
                        }
                    } else {
                        txtUrlTitleDescriptionPreview!!.visibility = View.GONE
                    }
                    currentImageSet = arrayOfNulls(sourceContent.images.size)
                    if (sourceContent.images.size > 0) {
                        val imageUrl1 = sourceContent.images[0]
                        try {
                            val url = URL(imageUrl1)
                            try {
                                val imageUri5 = url.toURI()
                                val imageUri8 = Uri.parse(imageUri5.toString())
                                if (imageUri8 == null) {
                                    showSnackBar("Image preview not find .Try again")
                                    //  hideProgress();
                                } else {
                                    loadUrlPreviewImage(imageUri8.toString())
                                    isImageFileFromHttpUrl = true
                                    // isFileFromDevice = true;
                                    sharedImagePath = imageUri8.toString()
                                    viewDataBinding.urlPreviewRemoveImageButton?.visibility =
                                        View.VISIBLE
                                    //   hideProgress();
                                }
                            } catch (e: Exception) {
                                showSnackBar(e.message)
                                e.printStackTrace()
                            }
                        } catch (e: MalformedURLException) {
                            showSnackBar(e.message)
                            e.printStackTrace()
                        }
                    }
                }
                // hideProgress();
            }
        }
    }

    private fun loadUrlPreviewImage(imageUrl: String?) {
        if (imageUrl != null && imageUrl.length > 0) {
            viewDataBinding.imageContainer.visibility = View.GONE
            viewDataBinding.urlPreviewImageContainer.visibility = View.VISIBLE
            viewDataBinding.urlPreviewImageView.visibility = View.VISIBLE
            Glide.with(this)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_stub)
                .into(viewDataBinding.urlPreviewImageView)
        }
    }// showSnackBar("No data available .Please try again");

    //  showSnackBar("No data available .Please try again");
    private val previewDataFromIntentExtraValues: Unit
        private get() {
            val item = clipData
            if (item != null) {
                try {
                    viewDataBinding.urlPreviewImageContainer.visibility = View.VISIBLE
                    viewDataBinding.urlPreviewProgressBar.visibility = View.GONE
                    var sharedImageUri: Uri? = null
                    if (item.uri != null) {
                        sharedImageUri = item.uri
                    } else if (imageFromIntent != null) {
                        sharedImageUri = Uri.parse(imageFromIntent)
                    }
                    txtUrlTitleDescriptionPreview!!.text = item.text?.toString()
                    viewDataBinding.urlPreviewRemoveImageButton?.visibility = View.GONE
                    if (sharedImageUri != null) {
                        urlPreviewImage = sharedImageUri.toString()
                        urlPreviewDescription = item.text?.toString()
                        isImageFileFromHttpUrl = true
                        loadUrlPreviewImage(sharedImageUri.toString())
                    }
                    isPreviewFromIntent = true
                    if (gramophoneTvUrl != null) {
                        urlLink = gramophoneTvUrl
                    } else {
                        if (urlFromIntent != null) {
                            urlLink = urlFromIntent
                        }
                    }
                    viewDataBinding.urlPreviewRemoveImageButton?.visibility = View.VISIBLE
                } catch (e: Exception) {
                    urlLink = null
                    //  showSnackBar("No data available .Please try again");
                    showSnackBar(getString(R.string.no_data_title))
                }
            } else {
                urlLink = null
                // showSnackBar("No data available .Please try again");
                showSnackBar(getString(R.string.no_data_title))
            }
        }

    fun showSnackBar(message: String?) {
        val snackbar = Snackbar.make(
            findViewById(android.R.id.content),
            message!!, Snackbar.LENGTH_LONG
        )
        snackbar.show()
    }

    fun showSnackBar(messageId: Int) {
        val snackbar = Snackbar.make(
            findViewById(android.R.id.content),
            messageId, Snackbar.LENGTH_LONG
        )
        snackbar.show()
    }

    fun showSnackBar(view: View?, messageId: Int) {
        val snackbar = Snackbar.make(view!!, messageId, Snackbar.LENGTH_LONG)
        snackbar.show()
    }


    private fun handleShareOtherLink(intent: Intent?) {
        var url: String? = null
        if (intent != null) {
            if (intent.action != null && intent.action == Intent.ACTION_SEND && intent.clipData != null) {
                val type = intent.type
                val item = intent.clipData!!.getItemAt(0)
                imageFromIntent = intent.getStringExtra("image")
                gramophoneTvUrl = intent.getStringExtra("gramoPhoneTv")
                clipData = item
                if (type != null && type.contains("image") && item != null) {
                    //isImageFromDevice(item)
                }
                if (!isFileFromDevice) {
//                    if(gr!=null)
//                    {
//                        url=gramophoneTv;
//                    }else {
                    val urlList: List<String> = pullLinks(item.toString())
                    if (urlList.size > 0) {
                        url = urlList[0]
                    }
                    if (url != null) {
                        if (containsURL(url)) {
                            urlFromIntent = url
                            val isPostFromOtherLink = true
                            initUrlPreview(url)
                        }
                    }
                }
            }
        }
    }


    private fun containsURL(content: String): Boolean {
        val REGEX = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"
        val p = Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE)
        val m = p.matcher(content)
        return m.find()
    }

    override fun onResume() {
        super.onResume()
        //  handleShareOtherLink(intent);

    }


    override fun onBackPressed() {
        super.onBackPressed()
        if (getIntent().action != null && getIntent().action == Intent.ACTION_VIEW) {
            val mode =
                SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.AppMode)
            launchHomePage()
        } else {
            finish()
        }
        super.onBackPressed()
    }

    private fun launchHomePage() {
        startActivity(Intent(this, HomeActivity::class.java))
        //intent!!.putExtra(IntentKeys.LaunchFragmentKey, Constants.SocialFragment)
        finish()
    }

    companion object {
        const val CREATE_NEW_POST_REQUEST = 11
        private val TAG = CreatePostActivity::class.java.simpleName
        private const val CHOOSER_PERMISSIONS_REQUEST_CODE = 7459
    }


    override fun onTokenAdded(token: Tag?) {
        startSuggestion = false
        searchText = null
        startPosition = null
    }

    override fun onTokenRemoved(token: Tag?) {}
    override fun onTokenIgnored(token: Tag?) {}
    override fun getLayoutID(): Int {
        return R.layout.create_posts_activity
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): CreatePostViewModel {
        return presenter
    }

    override fun onCropSelectionDone(cropList: MutableList<CropData>) {
        showTag(cropList)
        if (cropList.size > 0) {
            cropList.forEach {
                val tagMap = JSONObject()
                // tagMap.put("_id",it.cropId.toString())
                tagMap.put("tag", it.cropName.toString())
                presenter.tags.add(tagMap)

            }
        }
    }
}