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
import agstack.gramophone.utils.ApiResponse
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

    }

    private fun setupObservers() {
        verifyOtpViewModel.validateOtpResponseModel.observe(this, Observer{
            when (it) {
                is ApiResponse.Success -> {
                    progress.visibility = View.GONE
                    Toast.makeText(this@VerifyOtpActivity,it.data?.gp_api_message, Toast.LENGTH_LONG).show()
                    val bundle = Bundle()
                    //Add your data from getFactualResults method to bundle
                    bundle.putString(MOBILE_NO, verifyOtpViewModel.mobileNo)
                    openAndFinishActivity(HomeActivity::class.java,bundle)
                }
                is ApiResponse.Error -> {
                    progress.visibility = View.GONE
                    Toast.makeText(this@VerifyOtpActivity,it.message, Toast.LENGTH_LONG).show()

                }
                is ApiResponse.Loading -> {
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

    override fun getBundle(): Bundle? {
        return intent.extras
    }

    override fun onError(message: String?) {
        TODO("Not yet implemented")
    }

    override fun <T> onSuccess(cls: Class<T>) {
        TODO("Not yet implemented")
    }

    override fun onLoading() {
        TODO("Not yet implemented")
    }
}