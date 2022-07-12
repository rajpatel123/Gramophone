package agstack.gramophone.ui.login.view


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityLoginBinding
import agstack.gramophone.ui.login.LoginNavigator
import agstack.gramophone.ui.login.viewmodel.LoginViewModel
import agstack.gramophone.ui.verifyotp.view.VerifyOtpActivity
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.Resource
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*

@AndroidEntryPoint
class LoginActivity : BaseActivityWrapper<ActivityLoginBinding, LoginNavigator, LoginViewModel>(), LoginNavigator {

    //initialise ViewModel
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
        loginViewModel.generateOtpResponseModel.observe(this, Observer{
            when (it) {
                is Resource.Success -> {
                    progress.visibility = View.GONE
                    Toast.makeText(this@LoginActivity,it.data?.gp_api_message,Toast.LENGTH_LONG).show()
                    val bundle = Bundle()
                    //Add your data from getFactualResults method to bundle
                    bundle.putString(Constants.MOBILE_NO, loginViewModel.mobileNo)
                    openAndFinishActivity(VerifyOtpActivity::class.java,bundle)
                }
                is Resource.Error -> {
                    progress.visibility = View.GONE
                    Toast.makeText(this@LoginActivity,it.message,Toast.LENGTH_LONG).show()
                }
                is Resource.Loading -> {
                    progress.visibility = View.VISIBLE
                }
            }
        })

    }

    override fun getLayoutID(): Int {
      return R.layout.activity_login
    }

    override fun getBindingVariable(): Int {
        return  BR.viewModel
    }

    override fun getViewModel(): LoginViewModel {
        return loginViewModel
    }

   }