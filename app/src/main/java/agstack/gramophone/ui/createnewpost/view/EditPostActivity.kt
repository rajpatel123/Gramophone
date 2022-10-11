package agstack.gramophone.view.activity

//import com.theartofdev.edmodo.cropper.CropImage
import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityEditPostBinding
import agstack.gramophone.ui.createnewpost.view.*
import agstack.gramophone.ui.createnewpost.view.model.GpApiResponseData
import agstack.gramophone.ui.createpost.CreatePostNavigator
import agstack.gramophone.ui.createpost.viewmodel.CreatePostViewModel
import agstack.gramophone.ui.tagandmention.ExpandableTextView
import agstack.gramophone.ui.tagandmention.Tag
import agstack.gramophone.ui.tagandmention.TagAdapter
import agstack.gramophone.utils.Constants
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.google.android.material.snackbar.Snackbar
import com.leocardz.link.preview.library.LinkPreviewCallback
import com.leocardz.link.preview.library.SourceContent
import com.leocardz.link.preview.library.TextCrawler
import com.tokenautocomplete.TagTokenizer
import com.tokenautocomplete.TokenCompleteTextView
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import java.util.regex.Pattern

class EditPostActivity :
    BaseActivityWrapper<ActivityEditPostBinding, CreatePostNavigator, CreatePostViewModel>(),
    CreatePostNavigator, View.OnClickListener, TokenCompleteTextView.TokenListener<Tag?> {
    private var binding: ActivityEditPostBinding? = null
    private val presenter: CreatePostViewModel by viewModels()
    var layoutManager: LinearLayoutManager? = null
    private var postId: String? = null
    var hashMap: HashMap<String?, String?> = HashMap<String?, String?>()
    private var imageUri: Uri? = null
    private var pictureMode = 0
    private var selectedAgriTag: List<AgriTag> = ArrayList<AgriTag>()
    private var textCrawler: TextCrawler? = null
    protected var txtUrlTitleDescriptionPreview: ExpandableTextView? = null
    private var currentImageSet: Array<Bitmap?>? = null
    private var urlLink: String? = null
    private var urlFromIntent: String? = null

    //   private var intent: Intent? = null
    private var afterTextChangedCount = 0
    private var isImageFileFromHttpUrl = false
    private val attemptToLoadSelectedTag = false
    private val isTagSaved = false
    private var post: PostDetailsModel? = null
    var addImageButton: ImageView? = null
    protected var textWatcher: TextWatcher? = null
    private var progressDialog: ProgressDialog? = null
    private var description: String? = null
    private var isPreviewFromIntent = false
    private var urlPreviewImage: String? = null
    private var urlPreviewDescription: String? = null
    private var urlPreviewTitle: String? = null
    private var timer: Timer? = Timer()
    private val DELAY: Long = 1000 // in ms
    private var startSuggestion = false
    private var searchText: String? = null
    private var startPosition: Int? = null
    private var startSetText = false
    private var cropImage: ActivityResultLauncher<CropImageContractOptions>? = null;

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (MySingleton.getInstance().isOffTokenAutoComplete()) {
            binding!!.descriptionEditText.visibility = View.GONE
            binding!!.hashSymbol.visibility = View.GONE
            binding!!.atSymbol.visibility = View.GONE
            binding!!.descriptionNoTokenEditText.visibility = View.VISIBLE
        } else {
            binding!!.descriptionNoTokenEditText.visibility = View.GONE
            binding!!.descriptionEditText.visibility = View.VISIBLE
            binding!!.descriptionEditText.performBestGuess(false)
            binding!!.descriptionEditText.preventFreeFormText(false)
            binding!!.descriptionEditText.setTokenizer(TagTokenizer(Arrays.asList('#', '@')))
            //    binding.commentEditText.setAdapter(new TagAdapter(this, R.layout.tag_layout, Tag.sampleTags()));
            binding!!.descriptionEditText.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Select)
            binding!!.descriptionEditText.threshold = 1
            binding!!.descriptionEditText.setTokenListener(this)
            binding!!.hashSymbol.visibility = View.VISIBLE
            binding!!.atSymbol.visibility = View.VISIBLE
        }

        intent = getIntent()
        postId = intent.getStringExtra(Constants.POST_ID)
        textCrawler = TextCrawler()
        txtUrlTitleDescriptionPreview =
            findViewById<ExpandableTextView>(R.id.urlTitleDescriptionText)
        addImageButton = findViewById(R.id.btnAddImageTitle)
        if (postId != null) {
            presenter?.getPostDetails("" + postId)
        }
        binding?.buttonAddTag?.setOnClickListener {

            //TODO add code for get crop tags
//            hashMap.clear()
//            hashMap[getString(R.string.analytic_post_id)] = postId
//            GramophoneApplication.getInstance().setMixPanelDataWithScreenName(hashMap, getString(R.string.analytic_add_tag), getString(R.string.analytic_tag_list_screen_name), null, TAG)
//            val intent = Intent(this@EditPostActivity, TagListActivity::class.java)
//            startActivityForResult(intent, Constants.GET_SELECTED_TAGS)
        }
        binding!!.postButton.setOnClickListener {
            if (post != null && post?.getTags() != null && post?.getTags()!!.size > 0) {
                callApi()
            } else {
                showAddTagAlert()
            }
        }
        binding!!.hashSymbol.setOnClickListener { view: View? ->
            binding!!.descriptionEditText.append("#")
            binding!!.descriptionEditText.requestFocus()
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding!!.descriptionEditText, InputMethodManager.SHOW_IMPLICIT)
        }
        binding!!.atSymbol.setOnClickListener { view: View? ->
            binding!!.descriptionEditText.append("@")
            binding!!.descriptionEditText.requestFocus()
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding!!.descriptionEditText, InputMethodManager.SHOW_IMPLICIT)
        }
        binding!!.urlPreviewRemoveImageButton.setOnClickListener {

        }

    }


    private fun showAddTagAlert() {
//        val builder = AlertDialog.Builder(this)
//        builder.setMessage(getString(R.string.add_tag_message))
//        builder.setTitle(getString(R.string.add_tag_title))
//        builder.setPositiveButton(R.string.add_tag_title) { dialog, which ->
//            launchTagList()
//        }
//        builder.setNegativeButton(R.string.button_title_cancel) { dialog, which ->
//            dialog.dismiss()
//            callApi()
//        }
//        builder.setCancelable(true)
//        builder.create()
//        builder.show()
    }

    override fun enablePostButton() {
        viewDataBinding.postButton.isEnabled = true
    }

    override fun disablePostButton() {
        viewDataBinding.postButton.isEnabled = false
    }

    //updatePostApi call
    private fun callApi() {
//        hashMap.clear()
//        hashMap[getString(R.string.analytic_post_id)] = postId
//        GramophoneApplication.getInstance().setMixPanelDataWithScreenName(hashMap, getString(R.string.analytic_edit_post), null, null, TAG)
//        if (presenter!!.isProfileComplete()) {
//            if (binding!!.descriptionNoTokenEditText.visibility == View.VISIBLE) {
//                if (binding!!.descriptionNoTokenEditText.text != null) {
//                    val description = binding!!.descriptionNoTokenEditText.text.toString()
//
//                    Log.d("Raj",description)
//                    if (checkIfEmptyPost()) {
//                        showProgressBar()
//                        if (isPreviewFromIntent && post != null) {
//                            presenter?.updatePostUrlFromIntent(postId, urlPreviewTitle, urlPreviewDescription, urlPreviewImage, post?.getTags(), removedImages, description, urlLink, postImageModel)
//                        } else {
//                            if (post != null) {
//                                presenter?.updatePost(postId, post?.getTags(), removedImages, description, urlLink, postImageModel)
//                            }
//                        }
//                    } else {
//                        Snackbar.make(binding!!.descriptionNoTokenEditText, getString(R.string.warning_empty_input), Snackbar.LENGTH_LONG).show()
//                    }
//                }
//            } else {
//                if (binding!!.descriptionEditText.text != null) {
//                    val description = binding!!.descriptionEditText.text.toString()
//                    if (checkIfEmptyPost()) {
//                        showProgressBar()
//                        if (isPreviewFromIntent && post != null) {
//                            presenter?.updatePostUrlFromIntent(postId, urlPreviewTitle, urlPreviewDescription, urlPreviewImage, post?.getTags(), removedImages, description, urlLink, postImageModel)
//                        } else {
//                            if (post != null) {
//                                presenter?.updatePost(postId, post?.getTags(), removedImages, description, urlLink, postImageModel)
//                            }
//                        }
//                    } else {
//                        Snackbar.make(binding!!.descriptionEditText, getString(R.string.warning_empty_input), Snackbar.LENGTH_LONG).show()
//                    }
//                }
//            }
//        } else {
//            showProfileCompleteAlert()
//        }
    }

    private fun checkIfEmptyPost(): Boolean {
        try {
            if (binding!!.descriptionNoTokenEditText.visibility == View.VISIBLE && binding!!.descriptionNoTokenEditText.text != null) {
                description =
                    binding!!.descriptionNoTokenEditText.text.toString().trim { it <= ' ' }
            } else if (binding!!.descriptionEditText.visibility == View.VISIBLE && binding!!.descriptionEditText.text != null) {
                description = binding!!.descriptionEditText.text.toString().trim { it <= ' ' }
            }
            if (description != null && !description!!.isEmpty()) {
                return true
            } else if (urlLink != null && !urlLink!!.isEmpty()) {
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    private fun ContainsUrl(text: String?): Boolean {
        var contains = false
        var descriptionUrl: String? = null
        if (text != null && text.length > 10) {
            val urlList: List<String> = pullLinks(text)
            if (urlList.size > 0) {
                descriptionUrl = urlList[0]
                if (descriptionUrl != null) {
                    contains = true
                }
            }
        }
        return contains
    }

    override fun onClick(v: View) {}


    override fun gotoSocialPage(post: PostDetailsModel) {
//        hashMap.clear()
//        hashMap[getString(R.string.analytic_post_id)] = postId
//        val bundle = Bundle()
//        bundle.putSerializable(IntentKeys.PostDataKey, post)
//        val intent = Intent()
//        intent.putExtra(IntentKeys.PostDataKey, bundle)
//        setResult(RESULT_OK, intent)
//        finish()
    }


    override fun populatePostDetails(postDetailsModel: PostDetailsModel) {
        if (postDetailsModel != null) {
            post = postDetailsModel
            fillPostDetail(postDetailsModel)
        }
    }


    override fun hideProgressBar() {
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }

    private fun fillPostDetail(post: PostDetailsModel) {
        if (post?.getTags() != null) {
            AgriTagListResult.getInstance().setSelectedTagList(post?.getTags())
            selectedAgriTag = post?.getTags()
            showTag(post?.getTags())
        } else {
            AgriTagListResult.getInstance().getSelectedTagList().clear()
        }
        uploadUrlPreviewData(post?.getUrlPreviewMeta())

        if (post?.getDescription() != null) {
            setTagText(post?.getDescription())
            if (binding!!.descriptionNoTokenEditText.visibility == View.VISIBLE) {  //binding.descriptionNoTokenEditText.setText(post?.getDescription());
                binding!!.descriptionNoTokenEditText.setSelection(binding!!.descriptionNoTokenEditText.text.length)
            } else {
                //  binding.descriptionEditText.setText(post?.getDescription());
                binding!!.descriptionEditText.setSelection(binding!!.descriptionEditText.text.length)
            }
            if (containsURL(post?.getDescription())) {
                initiateTextWatcher()
            }
        }
    }


    private fun uploadUrlPreviewData(urlPreviewModel: UrlPreviewModel?) {
        if (urlPreviewModel != null) {
            if (urlPreviewModel.getUrl() != null && !urlPreviewModel.getUrl()
                    .isEmpty() && urlPreviewModel.getUrl() != ""
            ) {
                binding!!.urlPreviewImageContainer.visibility = View.VISIBLE
                binding!!.btnAddImageTitle.isEnabled = false
                //    textWatcher=null;
                binding!!.imageContainer.visibility = View.GONE
                urlLink = urlPreviewModel.getUrl()
                //  descriptionEditText.setText(post?.getDescription());
                binding!!.removeImageButton.isEnabled = false
                binding!!.removeImageButton.visibility = View.GONE
                binding!!.urlPreviewRemoveImageButton.isEnabled = false
                binding!!.urlPreviewRemoveImageButton.visibility = View.GONE
                //  binding.imageView.setEnabled(false);
                binding!!.btnAddImageTitle.isEnabled = false
                if (urlPreviewModel.getDescription() != null) {
                    binding!!.urlTitleDescriptionText.text =
                        urlPreviewModel.getTitle() + "\n" + urlPreviewModel.getDescription()
                    binding!!.urlTitleDescriptionText.visibility = View.VISIBLE
                } else {
                    binding!!.urlTitleDescriptionText.visibility = View.GONE
                }
                isPreviewFromIntent = true
                urlPreviewDescription = urlPreviewModel.getDescription()
                urlPreviewTitle = urlPreviewModel.getTitle()
                val sharedUrl: String = urlPreviewModel.getUrl()
                //   Log.i(TAG, sharedUrl);
                if (urlPreviewModel.getImage() != null && urlPreviewModel.getImage().length > 0) {
                    urlPreviewImage = urlPreviewModel.getImage()
                    binding!!.imageContainer.visibility = View.GONE
                    binding!!.urlPreviewImageContainer.visibility = View.VISIBLE
                    binding!!.imageView.visibility = View.GONE
                    binding!!.urlPreviewImageView.visibility = View.VISIBLE
                    val sharedImageUrl: String = urlPreviewModel.getImage()
                    loadUrlPreviewImage(sharedImageUrl)
                }
            } else {
                initiateTextWatcher()
                binding!!.btnAddImageTitle.isEnabled = true
                binding!!.urlPreviewImageContainer.visibility = View.GONE
            }
        } else {
            initiateTextWatcher()
            binding!!.btnAddImageTitle.isEnabled = true
            binding!!.urlPreviewImageContainer.visibility = View.GONE
        }
    }

    private fun loadUrlPreviewImage(imageUrl: String?) {
        if (imageUrl != null && imageUrl.length > 0) {
            binding!!.imageContainer.visibility = View.GONE
            binding!!.urlPreviewImageContainer.visibility = View.VISIBLE
            binding!!.urlPreviewImageView.visibility = View.VISIBLE
            Glide.with(this)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_stub)
                .into(binding!!.urlPreviewImageView)
        }
    }

    private fun setTagText(commentText: String) {
        if (binding!!.descriptionNoTokenEditText.visibility != View.VISIBLE) {
            val list: MutableList<Tag>? = pullTags(commentText)
            if (list != null) {
                val text = Html.fromHtml(commentText).toString()
                startSetText = true
                binding!!.descriptionEditText.setText("")
                var start = 0
                var end = 0
                var startIgnore = false
                for (i in 0 until text.length) {
                    if (end > 0 && end == i) {
                        end = 0
                        start = 0
                        startIgnore = false
                    } else {
                        if (!startIgnore) {
                            if (list.size > 0 && (text[i] == '#' || text[i] == '@')) {
                                val tagValue = list[0].tag
                                var subString = ""
                                if (text.length == tagValue.length + i + 1) {
                                    // Character character=text.charAt(tagValue.length() + i + 1);
                                    subString = text.substring(i + 1, tagValue.length + i + 1)
                                }
                                if (text.length > tagValue.length + i + 1 && (text[tagValue.length + i + 1] == ' ' || text[tagValue.length + i + 1] == ',')) {
                                    val character = text[tagValue.length + i + 1]
                                    subString = text.substring(i + 1, tagValue.length + i + 1)
                                }
                                if (tagValue == subString) {
                                    start = i
                                    end = list[0].tag.length + i
                                    binding!!.descriptionEditText.addObjectSync(list[0])

                                    list?.removeAt(0)
                                    startIgnore = true
                                } else {
                                    val a = text[i]
                                    binding!!.descriptionEditText.append(a.toString())
                                }
                            } else {
                                val a = text[i]
                                binding!!.descriptionEditText.append(a.toString())
                            }
                        }
                    }
                }
                startSetText = false
            } else {
                binding!!.descriptionEditText.setText(Html.fromHtml(commentText).toString())
            }
        } else {
            binding!!.descriptionNoTokenEditText.setText(Html.fromHtml(commentText).toString())
        }
    }

    fun pullTags(text: String?): ArrayList<Tag>? {
        val links = ArrayList<Tag>()
        //String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";
        //  String regex = "\\(?\\b(https?://|www[.]|ftp://)[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";
        val regex = "<a[^>]*>(.*?)</a>"
        //String regex = ">[#|@](.*?)<";
        val p = Pattern.compile(regex)
        val m = p.matcher(text)
        while (m.find()) {
            val urlStr = m.group()
            var url: String? = null
            var tagName: String? = null
            var prefix: Char? = null
            // String regex1 = "<a[^>]*>(.*?)</a>";
            val regex1 = "\"(.*?)\""
            val p1 = Pattern.compile(regex1)
            val m1 = p1.matcher(urlStr)
            while (m1.find()) {
                val urlStr2 = m1.group()
                url = urlStr2.substring(1, urlStr2.length - 1)
            }
            val regex2 = ">[#|@](.*?)<"
            val p2 = Pattern.compile(regex2)
            val m2 = p2.matcher(urlStr)
            while (m2.find()) {
                var urlStr3 = m2.group()
                if (urlStr3.startsWith(">") && urlStr3.endsWith("<")) {
                    urlStr3 = urlStr3.substring(1, urlStr3.length - 1)
                    prefix = urlStr3[0]
                    tagName = urlStr3.substring(1, urlStr3.length)
                }
            }
            if (tagName != null) {
                if (prefix == '#') {
                    val tag = Tag(null, prefix, tagName, url, null, null)
                    links.add(tag)
                } else if (prefix == '@') {
                    val profileBaseUrl = Constants.SearchUrl + Constants.ProfileUrlPARAMETER
                    if (url!!.contains(profileBaseUrl)) {
                        val uuid = url.substring(profileBaseUrl.length)
                        if (uuid != null) {
                            val tag = Tag(null, prefix, tagName, url, uuid, null)
                            links.add(tag)
                        }
                    }
                }
            }
        }
        return if (links.size > 0) {
            links
        } else null
    }


    private fun arePermissionsGranted(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) return false
        }
        return true
    }

    private fun requestPermissionsCompat(permissions: Array<String>, requestCode: Int) {
        ActivityCompat.requestPermissions(this, permissions, requestCode)
    }

    //    void getCameraPermission() {
    //        manager.requestPermissions(Manifest.permission.CAMERA);
    //    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == Constants.GET_SELECTED_TAGS && resultCode == RESULT_OK) {
//            selectedAgriTag = AgriTagListResult.getInstance().getSelectedTagList()
//            if (post != null) {
//                post?.setTags(selectedAgriTag)
//            }
//            showTag(selectedAgriTag)
//        }
    }

    protected fun startCropImageActivity(currentImageUri: Uri?) {
        if (currentImageUri == null) {
            return
        }
//        CropImage.activity(currentImageUri)
//                .setGuidelines(CropImageView.Guidelines.ON)
//                .setFixAspectRatio(false)
//                .setAutoZoomEnabled(true) //  .setAspectRatio(Constants.post?.MAX_WIDTH_POST_SIZE, Constants.post?.MIN_HEIGHT_POST_SIZE)
//                .setMinCropWindowSize(Constants.post?.MIN_WIDTH_POST_SIZE, Constants.post?.MIN_HEIGHT_POST_SIZE) //    .setMaxCropResultSize(Constants.post?.MAX_WIDTH_POST_SIZE,Constants.post?.MAX_HEIGHT_POST_SIZE)
//                .start(this)
        cropImage?.launch(
            options(uri = currentImageUri) {
//                    setGuidelines(CropImageView.Guidelines.ON)
//                    setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                setScaleType(CropImageView.ScaleType.FIT_CENTER)
                setCropShape(CropImageView.CropShape.RECTANGLE)
                setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                setAspectRatio(1, 1)
                setMaxZoom(4)
                setAutoZoomEnabled(true)
                setMultiTouchEnabled(true)
                setCenterMoveEnabled(true)
                setShowCropOverlay(true)
                setAllowFlipping(true)
                setSnapRadius(3f)
                setTouchRadius(48f)
                setInitialCropWindowPaddingRatio(0.1f)
                setBorderLineThickness(3f)
                setBorderLineColor(Color.argb(170, 255, 255, 255))
                setBorderCornerThickness(2f)
                setBorderCornerOffset(5f)
                setBorderCornerLength(14f)
                setBorderCornerColor(Color.WHITE)
                setGuidelinesThickness(1f)
                setGuidelinesColor(R.color.white)
                setBackgroundColor(Color.argb(119, 0, 0, 0))
                setMinCropWindowSize(24, 24)
                setMinCropResultSize(20, 20)
                setMaxCropResultSize(99999, 99999)
                setActivityTitle("")
                setActivityMenuIconColor(0)
                setOutputUri(null)
                setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
                setOutputCompressQuality(90)
                setRequestedSize(0, 0)
                setRequestedSize(0, 0, CropImageView.RequestSizeOptions.RESIZE_INSIDE)
                setInitialCropWindowRectangle(null)
                setInitialRotation(0)
                setAllowCounterRotation(false)
                setFlipHorizontally(false)
                setFlipVertically(false)
                setCropMenuCropButtonTitle(getString(R.string.crop_image_title_text))

                setCropMenuCropButtonIcon(0)
                setAllowRotation(true)
                setNoOutputImage(false)
                setFixAspectRatio(false)
            })
    }

    fun showTag(selectedTagList: List<AgriTag>?) {
        val finalAgriTagAdapter = FinalAgriTagAdapter()
        finalAgriTagAdapter.setCallback(object : FinalAgriTagAdapter.Callback {
            override fun onRemoveTagFromList(v: View, position: Int) {
                launchTagList()
            }
        })
        binding!!.finalTagRecyclerView.adapter = finalAgriTagAdapter
        binding!!.finalTagRecyclerView.layoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        finalAgriTagAdapter.setList(selectedTagList)
        //    getSelectedTagList(selectedTagList, createOnSelectedTagChangedListner());
    }

    private fun launchTagList() {
//        val intent = Intent(this@EditPostActivity, TagListActivity::class.java)
//        startActivityForResult(intent, Constants.GET_SELECTED_TAGS)
    }


    fun saveFilePath(bitmap: Bitmap) {
        val filename = "image"
        val f = File(this.externalCacheDir!!.absolutePath, filename)
        try {
            f.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }

//Convert bitmap to byte array
        val bitmap1 = bitmap
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        //bitmap.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        val bitmapData = bos.toByteArray()

//write the bytes in file
        try {
            val fos = FileOutputStream(f)
            fos.write(bitmapData)
            fos.flush()
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun initiateTextWatcher() {


        //  binding.descriptionEditText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        if (binding!!.descriptionNoTokenEditText.visibility == View.VISIBLE) {
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
                            //  Log.i("Create Post","handle input");
                            var text = s.toString()
                            text = Html.fromHtml(text).toString()
                            handleEditTextInput(text)
                            timer = null
                        }
                    }, DELAY)
                }
            }
            binding!!.descriptionNoTokenEditText.addTextChangedListener(textWatcher)
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
                    if (!startSetText) {
                        var text = s.toString()
                        if (text != null) {
                            text = Html.fromHtml(text).toString()
                        }
                        if (!startSuggestion) {
                            if (text != null && text.length > 0 && (text[text.length - 1] == '#' || text[text.length - 1] == '@')) {
                                startSuggestion = true
                                startPosition = text.length - 1
                            } else if (text != null && text.length > 0 && text[text.length - 1] == ' ') {
                                startSuggestion = false
                                searchText = null
                                startPosition = null
                            }
                        } else {
                            if (text != null && text.length > 0 && text[text.length - 1] == ' ') {
                                startSuggestion = false
                                searchText = null
                                startPosition = null
                            } else if (startPosition != null && text.length > 0 && text.length - 1 < startPosition!!) {
                                startSuggestion = false
                                searchText = null
                                startPosition = null
                            }
                        }
                        if (timer != null) timer!!.cancel()
                    }
                }

                override fun afterTextChanged(s: Editable) {
                    //avoid triggering event when text is too short wait if he has paused typing
                    if (!startSetText) {
                        var text = s.toString()
                        text = Html.fromHtml(text).toString()
                        if (startSuggestion) {
                            if (startPosition != null && text.length > startPosition!!) searchText =
                                text.substring(startPosition!!)
                            if (searchText != null && searchText!![0] == '@' && searchText!!.length > 1) {
                                presenter?.getMentionSuggestion(searchText!!.substring(1))
                            } else if (searchText != null && searchText!![0] == '#' && searchText!!.length > 1) {
                                presenter?.getSearchSuggestion(searchText!!.substring(1))
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
                                    //   Log.i("Create Post", "handle input");
                                    handleEditTextInput(finalText)
                                    timer = null
                                }
                            }, DELAY)
                        }
                    }
                }
            }
            binding!!.descriptionEditText.addTextChangedListener(textWatcher)
        }
    }

    fun handleEditTextInput(text: String) {
        afterTextChangedCount++
        // String text = binding.descriptionEditText.getText().toString();
        var descriptionUrl: String? = null
        if (text.length > 10) {
            val urlList: List<String> = pullLinks(text)
            if (urlList.size > 0) {
                descriptionUrl = urlList[0]
                if (descriptionUrl != null) {
                    if (intent!!.hasExtra(POST_EXTRA_KEY)) {
                        if (afterTextChangedCount > 1) {
                            urlFromIntent = descriptionUrl
                            initUrlPreview(descriptionUrl)
                        }
                    } else {
                        urlFromIntent = descriptionUrl
                        initUrlPreview(descriptionUrl)
                    }
                }
            }
        }
    }

    private fun initUrlPreview(text: String) {
        try {
            textCrawler?.makePreview(callback, text)
            binding!!.urlPreviewImageContainer.visibility = View.VISIBLE
            binding!!.urlPreviewProgressBar.visibility = View.VISIBLE
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
            if (b || sourceContent.getFinalUrl() == "") {
                /*
                  Inflating the content layout into Main View LinearLayout
                 */
                binding!!.urlPreviewImageContainer.visibility = View.GONE
                binding!!.urlPreviewProgressBar.visibility = View.GONE
                val failed = layoutInflater.inflate(
                    R.layout.failed,
                    linearLayout
                )
                val titleTextView = failed
                    .findViewById<TextView>(R.id.text)
                titleTextView.text = """
                    ${getString(R.string.failed_preview)}
                    ${sourceContent.getFinalUrl()}
                    """.trimIndent()
                failed.setOnClickListener {
                    //                            releasePreviewArea();
                }
                previewDataFromIntentExtraValues
                // hideProgress();
            } else {
                if (sourceContent.getCannonicalUrl() != null && sourceContent.getCannonicalUrl() == Constants.IsUrlForPlayStore) {
                    previewDataFromIntentExtraValues
                } else {
                    isPreviewFromIntent = false
                    binding!!.urlPreviewImageContainer.visibility = View.VISIBLE
                    binding!!.urlPreviewProgressBar.visibility = View.GONE
                    if (sourceContent.getTitle() != null && sourceContent.getDescription() != null && sourceContent.getFinalUrl() != null) {
                        txtUrlTitleDescriptionPreview?.setText(sourceContent.getTitle() + "\n" + sourceContent.getDescription())
                        if (txtUrlTitleDescriptionPreview != null) {
                            txtUrlTitleDescriptionPreview?.setVisibility(View.VISIBLE)
                            // finalDescription=String.valueOf(sourceContent.getTitle ()+"\n"+sourceContent.getDescription()+"\n"+"For mor detail Click on link");
                            // finalDescription = txtUrlTitleDescriptionPreview.getText().toString();
                            urlLink = sourceContent.getFinalUrl().toString()
                            addImageButton!!.isEnabled = false
                            if (urlFromIntent != null) {
                                urlLink = urlFromIntent
                            }
                        }
                    } else if (sourceContent.getFinalUrl() != null) {
                        // descriptionEditText.setEnabled(false);
                        addImageButton!!.isEnabled = false
                        urlLink = sourceContent.getFinalUrl().toString()
                        if (urlFromIntent != null) {
                            urlLink = urlFromIntent
                        }
                    } else {
                        txtUrlTitleDescriptionPreview?.setVisibility(View.GONE)
                    }
                    currentImageSet = arrayOfNulls(sourceContent.getImages().size)
                    if (sourceContent.getImages().size > 0) {
                        val imageUrl1: String = sourceContent.getImages().get(0)
                        try {
                            val url = URL(imageUrl1)
                            try {
                                val imageUri5 = url.toURI()
                                val imageUri8 = Uri.parse(imageUri5.toString())
                                //   loadImage(imageUri8);
                                loadUrlPreviewImage(imageUri8.toString())
                                binding!!.urlPreviewRemoveImageButton.visibility = View.GONE
                                // ifImagePathFalse(imageUri8);
                                // binding.imageView.setVisibility(View.VISIBLE);
                                if (imageUri8 == null) {
                                    showSnackBar("Image preview not find .Try again")
                                    //  hideProgress();
                                } else {
                                    isImageFileFromHttpUrl = true
                                    // isFileFromDevice = true;
                                    val sharedImagePath = imageUri8.toString()
                                    binding!!.imageView.isEnabled = false
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

    //  isImageFromDevice(item);
    //     finalDescription = txtUrlTitleDescriptionPreview.getText().toString();
    private val previewDataFromIntentExtraValues: Unit
        private get() {
            if (getIntent() != null && getIntent().action != null && getIntent().clipData != null) {
                if (getIntent().action == Intent.ACTION_SEND) {
                    val item = getIntent().clipData!!.getItemAt(0)
                    if (item != null) {
                        try {
                            binding!!.urlPreviewImageContainer.visibility = View.VISIBLE
                            binding!!.urlPreviewProgressBar.visibility = View.GONE
                            val sharedImageUri = item.uri
                            txtUrlTitleDescriptionPreview?.setText(item.text.toString())
                            if (sharedImageUri != null) {
                                loadUrlPreviewImage(sharedImageUri.toString())
                                isImageFileFromHttpUrl = true
                                //  isImageFromDevice(item);
                            }
                            if (sharedImageUri != null) {
                                urlPreviewImage = sharedImageUri.toString()
                            }
                            urlPreviewDescription = item.text.toString()
                            isPreviewFromIntent = true
                            //     finalDescription = txtUrlTitleDescriptionPreview.getText().toString();
                            if (urlFromIntent != null) {
                                urlLink = urlFromIntent
                            }
                        } catch (e: Exception) {
                            urlLink = null
                            showSnackBar("No preview available .Please try again")
                        }
                    } else {
                        urlLink = null
                        showSnackBar("No preview available .Please try again")
                    }
                } else {
                    urlLink = null
                    showSnackBar("No preview available .Please try again")
                }
            } else {
                urlLink = null
                showSnackBar("No preview available .Please try again")
            }
        }

    //// link preview extracting mechanism ends
    fun showSnackBar(message: String?) {
        if (message != null) {
            val snackbar: Snackbar = Snackbar.make(
                findViewById<View>(android.R.id.content),
                message,
                Snackbar.LENGTH_LONG
            )
            snackbar.show()
        }
    }

    private fun loadImage(imageUri: Uri?) {
        if (imageUri == null) {
            return
        }
        try {
            binding!!.removeImageButton.visibility = View.VISIBLE
            binding!!.imageContainer.visibility = View.VISIBLE
            binding!!.imageView.visibility = View.VISIBLE
            this.imageUri = imageUri
            Glide.with(this)
                .load(imageUri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .fitCenter()
                .into(binding!!.imageView)

            //  imageUri = ImagePicker.getUriAfterResized(this, imageUri, filename);
            // Bitmap bitmap = ImagePicker.getImageResized(this, imageUri);
            //saveFilePath(bitmap);
        } catch (outOfMemoryError: OutOfMemoryError) {
            showToast(getString(R.string.error_out_of_memory))
        } catch (e: Exception) {
            e.printStackTrace()
            showToast(getString(R.string.some_thing_went_wrong))
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
        hashMap.clear()
//        hashMap[getString(R.string.analytic_post_id)] = postId
//    }
    }


    override fun onTokenAdded(token: Tag?) {
        startSuggestion = false
        searchText = null
        startPosition = null
    }

    override fun onTokenRemoved(token: Tag?) {}
    override fun onTokenIgnored(token: Tag?) {}
    override fun openCameraToCapture() {
        TODO("Not yet implemented")
    }

    override fun updateAddDeleteBtn(imageNo: Int) {
    }

    override fun populateTagSuggestionList(tags: Array<Tag>) {
        binding!!.descriptionEditText.setAdapter(TagAdapter(this, R.layout.tag_layout, tags))
    }

    override fun populateHasTagList(tags: Array<Tag>) {
    }


    companion object {
        const val POST_EXTRA_KEY = "EditPostActivity.POST_EXTRA_KEY"
        const val UPDATE_POST_REQUEST = 9
        private val TAG = EditPostActivity::class.java.simpleName
        private const val CHOOSER_PERMISSIONS_REQUEST_CODE = 7459
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_edit_post
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): CreatePostViewModel {
        return presenter
    }
}