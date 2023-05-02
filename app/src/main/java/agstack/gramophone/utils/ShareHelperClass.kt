package agstack.gramophone.utils


import agstack.gramophone.BuildConfig
import agstack.gramophone.R
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks


class ShareHelperClass(

// Private variables
    private val presentingActivity: androidx.fragment.app.FragmentActivity,
    appendedUri: Uri,
    source: ShareAnalyticsSource,
    medium: ShareAnalyticsMedium,
    campaign: ShareAnalyticsCampaign,
    socialMetaTitle: String?,
    socialMetaDescription: String?,
    socialMetaImageUri: Uri?,
    private val shortUriHandler: ShortUriHandler,
    private val genericUriHandler: GenericUriHandler
) {
    private var shortLinkUri: Uri? = null
    private var completeDynamicLinkUri: Uri? = null // to be consumed to generate short link
    private var imageUri: Uri? = null
    public var extraSubject: String? = null
    public var extraText: String? = null
    private var contentTitle: String? = null

    init {
        buildDeepLink(
            appendedUri,
            source,
            medium,
            campaign,
            socialMetaTitle,
            socialMetaDescription,
            socialMetaImageUri
        )
    }

    // Reference code: https://stackoverflow.com/a/42125340/217586
    // Idea is we will get Uri with appended query parameters passed from activity, which further needs to be encoded
    private fun buildDeepLink(
        appendedUri: Uri,
        source: ShareAnalyticsSource,
        medium: ShareAnalyticsMedium,
        campaign: ShareAnalyticsCampaign,
        socialMetaTitle: String?,
        socialMetaDescription: String?,
        socialMetaImageUrl: Uri?
    ) {
        val socialMetaTagParametersBuilder =
            socialMetaTitle?.let { DynamicLink.SocialMetaTagParameters.Builder().setTitle(it) }
        // Check for social meta description
        contentTitle = socialMetaTitle
        if (socialMetaDescription != null && !TextUtils.isEmpty(socialMetaDescription)) {
            socialMetaTagParametersBuilder?.setDescription(socialMetaDescription)
        }
        // Check for socialMetaImageUrl
        if (socialMetaImageUrl != null && Uri.EMPTY != socialMetaImageUrl) {
            socialMetaTagParametersBuilder?.setImageUrl(socialMetaImageUrl)
            imageUri = socialMetaImageUrl
        }

        completeDynamicLinkUri =appendedUri
        // Prepare the completeDynamicLinkUri
        val completeDynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(appendedUri) //https://app.gramophone.in/gM7N
            .setDomainUriPrefix(DYNAMIC_LINK_DOMAIN) //app.gramophone.in
            // .setDynamicLinkDomain(DYNAMIC_LINK_DOMAIN)

            .setAndroidParameters(
                DynamicLink.AndroidParameters.Builder(BuildConfig.APPLICATION_ID)
                    .setMinimumVersion(MIN_VERSION).build()
            )
            .setGoogleAnalyticsParameters(
                DynamicLink.GoogleAnalyticsParameters.Builder().setSource(source.name)
                    .setMedium(medium.name).setCampaign(campaign.name).build()
            )
            .setSocialMetaTagParameters(socialMetaTagParametersBuilder?.build()!!)
            .buildDynamicLink()
          completeDynamicLinkUri = completeDynamicLink.uri
        //completeDynamicLinkUri = Uri.parse("https://app.gramophone.in?utm_campaign=userInitiated&sd=Obtaining%20agricultural%20products%20and%20advisory%20from%20your%20home%20became%20easier!&si=https%3A%2F%2Fgramophone-images.s3.ap-south-1.amazonaws.com%2Frefer-n-earn-en.jpg&st=Gramophone&amv=89&apn=agstack.gramophone.debug&link=http%3A%2F%2Fwww.gramophone.in%2F%3Fcategory%3DfirstAppOpen%26referralCode%3D208db7f&utm_medium=social&utm_source=androidApp")
    }

    // Reference: https://www.youtube.com/watch?v=pplnFBm5ROk
    fun shareDynamicLink() {
        if (shortLinkUri != null && Uri.EMPTY != shortLinkUri) {
            shortUriHandler.processShortUri(shortLinkUri!!)
        } else {

            FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(completeDynamicLinkUri!!)
                .buildShortDynamicLink()
                .addOnCompleteListener(presentingActivity) { task ->
                    if (task.isSuccessful) {
                        // Share short link :)
                        shortLinkUri = task.result?.shortLink
                        shortUriHandler.processShortUri(shortLinkUri!!)
                        Log.i("deep_link_promo4", "task success")
                    }
//                        else {
//                            // Share base uri :-) Load just Home screen
//                            genericUriHandler.processGenericUri(GENERIC_URI)
//                        }
                }
                .addOnFailureListener(presentingActivity) {
                    // Share base uri :-) Load just Home screen
                    Log.i("deep_link_promo4", "task fail" + it.toString())
                    genericUriHandler.processGenericUri(GENERIC_URI)
                }
        }
    }


    fun shareDeepLinkWithExtraText(extraText: String, extraSubject: String) {
        try {
            if (imageUri != null) {
                shareDeepLinkWithExtraImageAndText(
                    extraText,
                    imageUri!!,
                    extraSubject,
                    IntentKeys.OtherShareKey
                )
            } else {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT, extraSubject)
                intent.putExtra(Intent.EXTRA_TEXT, extraText)
                presentingActivity.startActivityForResult(intent, RESULT_CODE)
            }
        } catch (e: Exception) {
        }

    }

    fun shareDeepLinkWithExtraYouTubeUrl(
        extraText: String,
        extraSubject: String,
        youtubeUrl: String
    ) {

        if (imageUri != null) {

            shareDeepLinkWithExtraImageAndYoutube(
                extraText,
                imageUri!!,
                extraSubject,
                IntentKeys.OtherShareKey,
                youtubeUrl
            );
        } else {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, extraSubject)
            intent.putExtra(Intent.EXTRA_TEXT, extraText)
            intent.putExtra(IntentKeys.GramophonetvKey, youtubeUrl)
            presentingActivity.startActivityForResult(intent, RESULT_CODE)
        }
    }

/*
    fun shareLinkContentToFb(
        sharedUrlLink: String,
        title: String,
        description: String,
        shareOnVariable: String?,
        fbShareDialog: ShareDialog,
        callbackManager: CallbackManager
    ) {
        if (shareOnVariable != null && shareOnVariable == IntentKeys.FacebookShareKey) {

            shareOnFacebook(callbackManager, fbShareDialog)
            val content = ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(sharedUrlLink))
                .setContentTitle(title)
                .setContentDescription(description)
                .build()
            fbShareDialog.show(content, ShareDialog.Mode.AUTOMATIC)


        }
    }*/


    fun shareDeepLinkWithExtraTextWithOption(
        extraText: String?,
        extraSubject: String?,
        shareOn: String?
    ) {
        //   shareTextTest(extraText+extraSubject);
        if (shareOn != null) {
            //  if(!shareOn.equals(IntentKeys.FacebookShareKey)) {
            if (imageUri != null) {

                shareDeepLinkWithExtraImage(extraText, imageUri!!, extraSubject, shareOn)

            } else {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"


                when (shareOn) {
                    IntentKeys.WhatsAppShareKey -> {
                        intent.setPackage("com.whatsapp")
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    IntentKeys.FacebookShareKey -> {
                        intent.setPackage("com.facebook.katana")

                    }
                    //intent.putExtra("link",)

                }
                if (shortLinkUri != null) {
                    intent.setData(shortLinkUri)
                } else {
                    intent.setData(GENERIC_URI)
                }

                intent.putExtra(Intent.EXTRA_SUBJECT, extraSubject)
                intent.putExtra(Intent.EXTRA_TEXT, extraText)
                presentingActivity.startActivityForResult(intent, Constants.postShareRequestKey)
            }
        }
        //    }
    }

    fun shareDeepLinkWithExtraImage(
        extraText: String?,
        postImageUri: Uri,
        extraSubject: String?,
        shareOn: String
    ) {
        val intent = Intent(Intent.ACTION_SEND)
        if (contentTitle != null) {
            intent.putExtra(Intent.EXTRA_TEXT, contentTitle + "\n" + extraText);
        } else {
            intent.putExtra(Intent.EXTRA_TEXT, extraText);
        }
        intent.type = "text/plain"
        if (shortLinkUri != null) {
            intent.putExtra("link", shortLinkUri.toString())
        } else {
            intent.putExtra("link", GENERIC_URI.toString())
        }
        intent.putExtra(IntentKeys.ShareImageKey, postImageUri.toString());
        intent.putExtra(Intent.EXTRA_SUBJECT, extraSubject);

        when (shareOn) {
            IntentKeys.WhatsAppShareKey -> {
                intent.setPackage("com.whatsapp")
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            IntentKeys.FacebookShareKey -> {
                intent.setPackage("com.facebook.katana")
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }


        }
        presentingActivity.startActivity(Intent.createChooser(intent, "Image"));


    }


    fun shareDeepLinkWithExtraImageAndText(
        extraText: String,
        postImageUri: Uri,
        extraSubject: String,
        shareOn: String
    ) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(IntentKeys.ShareImageKey, postImageUri.toString());
        intent.putExtra(Intent.EXTRA_SUBJECT, extraSubject);
        //intent.putExtra(Intent.EXTRA_TEXT,extraText);
        if (contentTitle != null) {
            intent.putExtra(Intent.EXTRA_TEXT, contentTitle + "\n" + extraText);
        } else {
            intent.putExtra(Intent.EXTRA_TEXT, extraText);
        }
        when (shareOn) {
            IntentKeys.WhatsAppShareKey -> {
                intent.setPackage("com.whatsapp")
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            IntentKeys.FacebookShareKey -> intent.setPackage("com.facebook.katana")

        }
        presentingActivity.startActivity(
            Intent.createChooser(
                intent,
                presentingActivity.getString(R.string.share_button_title)
            )
        );

    }

    fun shareDeepLinkWithExtraImageAndYoutube(
        extraText: String,
        postImageUri: Uri,
        extraSubject: String,
        shareOn: String,
        youtubeUrl: String
    ) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(IntentKeys.ShareImageKey, postImageUri.toString());
        intent.putExtra(IntentKeys.GramophonetvKey, youtubeUrl);
        intent.putExtra(Intent.EXTRA_SUBJECT, extraSubject);
        //intent.putExtra(Intent.EXTRA_TEXT,extraText);
        if (contentTitle != null) {
            intent.putExtra(Intent.EXTRA_TEXT, contentTitle + "\n" + extraText);
        } else {
            intent.putExtra(Intent.EXTRA_TEXT, extraText);
        }
        when (shareOn) {
            IntentKeys.WhatsAppShareKey -> {
                intent.setPackage("com.whatsapp")
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            IntentKeys.FacebookShareKey -> intent.setPackage("com.facebook.katana")

        }
        presentingActivity.startActivity(
            Intent.createChooser(
                intent,
                presentingActivity.getString(R.string.share_button_title)
            )
        );

    }

    companion object {
        // Constants
        //    private val DYNAMIC_LINK_DOMAIN = "hft3v.app.goo.gl"
        private val DYNAMIC_LINK_DOMAIN = "https://app.gramophone.in"

        // To be used to generate appended Uri
        @JvmField
        val BASE_URI = Uri.parse("https://www.gramophone.in/")

        // To be used for generic message and/ or when actual link could not be generated
        @JvmField
        val GENERIC_URI = Uri.parse("https://app.gramophone.in/gM7N")

        // Generic Image URI
        @JvmField
        val GENERIC_IMAGE_URI =
            // Uri.parse("http://res.cloudinary.com/agstack/image/upload/v1/web/logo.png")
            Uri.parse("https://gramophone-images.s3.ap-south-1.amazonaws.com/gramophone_logo.png")
        @JvmField
        val GENERIC_HINDI_IMAGE_URI =
            Uri.parse("https://gramophone-images.s3.ap-south-1.amazonaws.com/refer-n-earn-hindi.jpg")

        @JvmField
        val GENERIC_MARATHI_IMAGE_URI =
            Uri.parse("https://gramophone-images.s3.ap-south-1.amazonaws.com/refer-n-earn-mr.jpg")

        @JvmField
        val GENERIC_ENG_IMAGE_URI =
            Uri.parse("https://gramophone-images.s3.ap-south-1.amazonaws.com/refer-n-earn-en.jpg")






        // Version number from which dynamic links are supported
        private val MIN_VERSION = 89

        // Result code expected after share sheet dismiss
        @JvmField
        val RESULT_CODE = 901
    }

}

interface ShortUriHandler {
    fun processShortUri(shortUri: Uri)
}

interface GenericUriHandler {
    fun processGenericUri(genericUri: Uri)
}
