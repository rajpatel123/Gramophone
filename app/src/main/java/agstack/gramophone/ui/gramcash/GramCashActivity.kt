package agstack.gramophone.ui.gramcash

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.GramCashActivityBinding
import agstack.gramophone.ui.faq.FAQAdapter
import agstack.gramophone.ui.home.view.HomeActivity
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.viewModels
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


}
