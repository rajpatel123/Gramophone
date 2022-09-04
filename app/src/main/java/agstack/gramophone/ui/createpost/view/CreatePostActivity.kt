package agstack.gramophone.ui.createpost.view

import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityCreatePostBinding
import agstack.gramophone.ui.createpost.CreatePostNavigator
import agstack.gramophone.ui.createpost.viewmodel.CreatePostViewModel
import agstack.gramophone.ui.dialog.LanguageBottomSheetFragment
import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.google.zxing.integration.android.IntentIntegrator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*

@AndroidEntryPoint
class CreatePostActivity: BaseActivityWrapper<ActivityCreatePostBinding, CreatePostNavigator, CreatePostViewModel>(),
CreatePostNavigator,
LanguageBottomSheetFragment.LanguageUpdateListener {
    val REQUEST_CODE = 0x0000c0de
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

    override fun getLayoutID(): Int {
        TODO("Not yet implemented")
    }

    override fun getViewModel(): CreatePostViewModel {
        TODO("Not yet implemented")
    }

    override fun onLanguageUpdate() {
        TODO("Not yet implemented")
    }

    override fun getBindingVariable(): Int {
        TODO("Not yet implemented")
    }
}