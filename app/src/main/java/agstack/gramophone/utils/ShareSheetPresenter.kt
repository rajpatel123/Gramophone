package agstack.gramophone.utils

import agstack.gramophone.R
import android.content.Intent
import android.net.Uri

class ShareSheetPresenter(
    private val presentingActivity: androidx.fragment.app.FragmentActivity,
        private val genericUriHandler: GenericUriHandler
) {


    fun shareDynamicLink() {
        genericUriHandler.processGenericUri(GENERIC_URI)
    }

    fun shareDeepLinkWithExtraTextWithOption(
        extraText: String,
        extraSubject: String,
        extraImage:Uri?=null,
        shareOn: String
    ) {

        if (shareOn != null) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, extraText)
            intent.putExtra(Intent.EXTRA_SUBJECT, extraSubject)
           // intent.putExtra(IntentKeys.ShareImageKey, extraImage.toString());



            when (shareOn) {
                IntentKeys.WhatsAppShareKey -> {
                    intent.setPackage("com.whatsapp")
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    intent.setType("*/*")
                    intent.putExtra(Intent.EXTRA_STREAM,extraImage)
                }
                IntentKeys.OtherShareKey -> {
                    intent.setType("*/*")
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    intent.putExtra(Intent.EXTRA_STREAM,extraImage)



                }
            }


            presentingActivity.startActivity(Intent.createChooser(intent,
               "Share App link"));
        }
    }


    companion object {
        // To be used for generic message and/ or when actual link could not be generated
        @JvmField
        val GENERIC_URI = Uri.parse("https://app.gramophone.in/gM7N")
    }


    interface GenericUriHandler {
        fun processGenericUri(genericUri: Uri)
    }

}