package agstack.gramophone.ui.login.view

import agstack.gramophone.base.BaseActivity
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityLoginBinding
import agstack.gramophone.ui.apptour.view.AppTourActivity
import agstack.gramophone.ui.login.LoginNavigator
import agstack.gramophone.ui.login.viewmodel.LoginViewModel
import agstack.gramophone.ui.splash.view.SplashActivity
import agstack.gramophone.ui.splash.viewmodel.SplashViewModel
import agstack.gramophone.ui.verifyotp.view.VerifyOtpActivity
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.LocaleManagerClass
import agstack.gramophone.utils.Resource
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginNavigator, LoginViewModel>(), LoginNavigator {

    private lateinit var binding: ActivityLoginBinding
    //initialise ViewModel
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUi()
        setupObservers()
    }

    private fun setupObservers() {
        loginViewModel.generateOtpResponseModel.observe(this, Observer{
            when (it) {
                is Resource.Success -> {
                    binding.progress.visibility = View.GONE
                    VerifyOtpActivity.start(this@LoginActivity)
                }
                is Resource.Error -> {
                    binding.progress.visibility = View.GONE
                    it.message?.let { message ->
                        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    progress.visibility = View.VISIBLE
                }
            }
        })

    }


    private fun setupUi() {
        submitBtn.setOnClickListener { view ->
            val hashMap = HashMap<Any, Any>()
            hashMap[Constants.PHONE] = mobileEdt.text.toString()
            val language = LocaleManagerClass.getLangCodeFromPreferences(this)
            hashMap[Constants.LANGUAGE] = language
            loginViewModel.sendOTP(hashMap)
        }
    }


    override fun getLayoutID(): Int {
        TODO("Not yet implemented")
    }

    override fun getBindingVariable(): Int {
        TODO("Not yet implemented")
    }

    override fun getViewModel(): LoginViewModel {
        TODO("Not yet implemented")
    }

}