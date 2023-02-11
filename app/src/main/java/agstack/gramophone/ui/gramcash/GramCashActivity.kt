package agstack.gramophone.ui.gramcash

import agstack.gramophone.BR
import agstack.gramophone.BuildConfig
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.GramCashActivityBinding
import agstack.gramophone.ui.faq.FAQAdapter
import agstack.gramophone.ui.home.view.HomeActivity
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class GramCashActivity :
    BaseActivityWrapper<GramCashActivityBinding, GramCashNavigator, GramCashViewModel>(),
    GramCashNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolBar(true, "", R.drawable.ic_arrow_left_white)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.green_medium)))
        mViewModel?.getGramCash()
    }

    private val gramCashViewModel: GramCashViewModel by viewModels()
    override fun getLayoutID(): Int {
        return R.layout.gram_cash_activity
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): GramCashViewModel {
        return gramCashViewModel
    }

    override fun setFAQAdapter(faqAdapter: FAQAdapter) {
        viewDataBinding.rvFaq.adapter = faqAdapter
    }

    override fun setGramCashRulesAdapter(faqAdapter: FAQAdapter) {
        viewDataBinding.rvGcRules.adapter = faqAdapter
    }


    override fun manageaboutBottomPopup() {
        val mBottomSheetDialog = this?.let { Dialog(it, R.style.MaterialDialogSheet) }
        val layoutInflater =
            this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogLayout = layoutInflater.inflate(R.layout.about_gram_cash_popup, null)

        mBottomSheetDialog.setContentView(dialogLayout)
        mBottomSheetDialog.setCancelable(true)
        mBottomSheetDialog.getWindow()?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        mBottomSheetDialog.getWindow()?.setGravity(Gravity.BOTTOM)
        var button: ImageView = dialogLayout.findViewById(R.id.closeImageView) as ImageView

        button.setOnClickListener {
            mBottomSheetDialog.dismiss()
        }

        mBottomSheetDialog?.show()


    }

    override fun sendMoEngageEvent(eventName: String) {
        val properties = Properties()
            .addAttribute("Customer_Id",
                SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.CUSTOMER_ID)!!)
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        if (eventName == "KA_View_GramCash") {
            properties.addAttribute("Redirection_Source", Constants.HAMBURGER_HOME)
                .addAttribute("GramCash_Balance",
                    gramCashViewModel.gramCashResponseData.get()?.gramcashTotal)
        } else if(eventName == "KA_Click_GramCashExpiringSoon") {
            properties.addAttribute("GramCash_ExpiringSoon_Balance", gramCashViewModel.gramCashResponseData.get()?.gramcashExpiringSoon.toString())
        } else if (eventName == "KA_View_AboutGramCash") {
            properties.addAttribute("Redirection_Source", "GramCashLanding")
        } else if (eventName == "KA_View_GramCashExpiringSoon") {
            properties.addAttribute("Redirection_Source", "GramCashLanding")
        } else if (eventName == "KA_View_GramCashTransaction") {
            properties.addAttribute("Redirection_Source", "GramCashLanding")
        }
        MoEAnalyticsHelper.trackEvent(this, eventName, properties)
    }

    override fun loadBannerImage(referralAndShareImage: String?) {
        Glide.with(this)
            .load(referralAndShareImage)
            .into(viewDataBinding.bannerImg)
    }


}
