package agstack.gramophone.ui.login.view

import agstack.gramophone.base.BaseActivity
import agstack.gramophone.databinding.ActivityLoginBinding
import agstack.gramophone.ui.apptour.view.AppTourActivity
import agstack.gramophone.ui.login.viewmodel.LoginViewModel
import agstack.gramophone.ui.splash.view.SplashActivity
import agstack.gramophone.ui.verifyotp.view.VerifyOtpActivity
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.LocaleManagerClass
import agstack.gramophone.utils.Resource
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.log

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding
    //initialise ViewModel
    private val loginViewModel: LoginViewModel by viewModels()


    companion object {
        fun start(activity: SplashActivity) {
            val intent = Intent(activity, LoginActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }

        fun start(activity: AppTourActivity) {
            val intent = Intent(activity, LoginActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }

        fun start(activity: VerifyOtpActivity) {
            val intent = Intent(activity, LoginActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
    }

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
                    val value = it.data!!

                    val data = value

                }
                is Resource.Error -> {
                    binding.progress.visibility = View.GONE
                    it.message?.let { message ->
                        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    binding.progress.visibility = View.VISIBLE
                }
            }
        })

    }


    private fun setupUi() {
        binding.submitBtn.setOnClickListener { view ->
            //Perform validations then call api
            val hashMap = HashMap<Any, Any>()
            hashMap[Constants.PHONE] = "8285886155"
            val language = LocaleManagerClass.getLangCodeFromPreferences(this)
            hashMap[Constants.LANGUAGE] = language

            loginViewModel.sendOTP(hashMap)
        }
    }

}