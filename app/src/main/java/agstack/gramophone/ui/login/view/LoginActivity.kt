package agstack.gramophone.ui.login.view

import agstack.gramophone.Status
import agstack.gramophone.base.BaseActivity
import agstack.gramophone.databinding.ActivityLoginBinding
import agstack.gramophone.retrofit.ApiHelper
import agstack.gramophone.retrofit.RetrofitBuilder
import agstack.gramophone.splash.viewmodel.LoginViewModel
import agstack.gramophone.ui.apptour.view.AppTourActivity
import agstack.gramophone.ui.login.viewmodel.ViewModelfactory
import agstack.gramophone.ui.profile.view.ProfileSelectionActivity
import agstack.gramophone.ui.verifyotp.view.VerifyOtpActivity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    companion object {
        fun start(activity: ProfileSelectionActivity) {
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
        setupViewModel()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.loginUser().observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        //TODO Handle the response here
                    }
                    Status.ERROR -> {
                        //TODO Handle the error here
                    }

                    Status.LOADING -> {
                        // TODO manage progress
                    }
                }
            }
        })
    }


    private fun setupUi() {
        binding.submitBtn.setOnClickListener {view ->
            VerifyOtpActivity.start(this@LoginActivity)
        }
    }

    private fun setupViewModel() {
        // With ViewModelFactory
        viewModel = ViewModelProvider(
            this, ViewModelfactory(ApiHelper(RetrofitBuilder.apiService))
        )
            .get(LoginViewModel::class.java)
    }
}