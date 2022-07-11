package agstack.gramophone.ui.verifyotp.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityVerifyOtpBinding
import agstack.gramophone.ui.home.view.HomeActivity
import agstack.gramophone.ui.login.view.LoginActivity
import agstack.gramophone.ui.verifyotp.VerifyOTPNavigator
import agstack.gramophone.ui.verifyotp.viewmodel.VerifyOtpViewModel
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_verify_otp.*

@AndroidEntryPoint
class VerifyOtpActivity : BaseActivityWrapper<ActivityVerifyOtpBinding, VerifyOTPNavigator, VerifyOtpViewModel>(),
    VerifyOTPNavigator {
    private  val verifyOtpViewModel: VerifyOtpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {

    }


    override fun onBackPressed() {
        super.onBackPressed()
        openActivity(LoginActivity::class.java,null)
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_verify_otp
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): VerifyOtpViewModel {
      return verifyOtpViewModel
    }
}