package agstack.gramophone.ui.verifyotp.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityVerifyOtpBinding
import agstack.gramophone.ui.home.view.HomeActivity
import agstack.gramophone.ui.login.view.LoginActivity
import agstack.gramophone.ui.verifyotp.VerifyOTPNavigator
import agstack.gramophone.ui.verifyotp.viewmodel.VerifyOtpViewModel
import agstack.gramophone.utils.Constants.MOBILE_NO
import agstack.gramophone.utils.Resource
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_verify_otp.*

@AndroidEntryPoint
class VerifyOtpActivity : BaseActivityWrapper<ActivityVerifyOtpBinding, VerifyOTPNavigator, VerifyOtpViewModel>(),
    VerifyOTPNavigator {
    private  val verifyOtpViewModel: VerifyOtpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObservers()

        val extras = intent.extras
        verifyOtpViewModel.mobileNo= extras?.getString(MOBILE_NO).toString()
    }

    private fun setupObservers() {
        verifyOtpViewModel.validateOtpResponseModel.observe(this, Observer{
            when (it) {
                is Resource.Success -> {
                    progress.visibility = View.GONE
                    Toast.makeText(this@VerifyOtpActivity,it.data?.gp_api_message, Toast.LENGTH_LONG).show()
                    val bundle = Bundle()
                    //Add your data from getFactualResults method to bundle
                    bundle.putString(MOBILE_NO, verifyOtpViewModel.mobileNo)
                    openAndFinishActivity(HomeActivity::class.java,bundle)
                }
                is Resource.Error -> {
                    progress.visibility = View.GONE
                    Toast.makeText(this@VerifyOtpActivity,it.message, Toast.LENGTH_LONG).show()

                }
                is Resource.Loading -> {
                    progress.visibility = View.VISIBLE
                }
            }
        })
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