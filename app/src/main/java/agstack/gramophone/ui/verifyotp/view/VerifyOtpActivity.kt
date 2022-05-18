package agstack.gramophone.ui.verifyotp.view

import agstack.gramophone.Status
import agstack.gramophone.base.BaseActivity
import agstack.gramophone.databinding.ActivityVerifyOtpBinding
import agstack.gramophone.retrofit.ApiHelper
import agstack.gramophone.retrofit.RetrofitBuilder
import agstack.gramophone.ui.home.view.HomeActivity
import agstack.gramophone.ui.login.view.LoginActivity
import agstack.gramophone.ui.verifyotp.viewmodel.ViewModelFactory
import agstack.gramophone.ui.verifyotp.viewmodel.VerifyOtpViewModel
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class VerifyOtpActivity : BaseActivity() {

    private lateinit var binding: ActivityVerifyOtpBinding
    private lateinit var viewModel: VerifyOtpViewModel

    companion object {
        fun start(activity: LoginActivity) {
            val intent = Intent(activity, VerifyOtpActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupUi()
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
        binding.changeNumberTxt.setOnClickListener { onBackPressed() }
        binding.verifyOtpBtn.setOnClickListener {
            HomeActivity.start(this@VerifyOtpActivity)
        }
    }

    private fun setupViewModel() {
        // With ViewModelFactory
        viewModel = ViewModelProvider(
            this, ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        )
            .get(VerifyOtpViewModel::class.java)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        LoginActivity.start(this@VerifyOtpActivity)
    }
}