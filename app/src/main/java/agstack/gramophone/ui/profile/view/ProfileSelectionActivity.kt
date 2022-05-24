package agstack.gramophone.ui.profile.view

import agstack.gramophone.Status
import agstack.gramophone.base.BaseActivity
import agstack.gramophone.databinding.ActivityProfileSelectionBinding
import agstack.gramophone.retrofit.ApiHelper
import agstack.gramophone.retrofit.RetrofitBuilder
import agstack.gramophone.ui.home.view.HomeActivity
import agstack.gramophone.ui.profile.viewmodel.ProfileSelectionViewModel
import agstack.gramophone.ui.profile.viewmodel.ViewModelFactory
import agstack.gramophone.ui.verifyotp.view.VerifyOtpActivity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class ProfileSelectionActivity : BaseActivity() {
    private lateinit var binding: ActivityProfileSelectionBinding
    private lateinit var viewModel: ProfileSelectionViewModel

    companion object {
        fun start(activity: VerifyOtpActivity) {
            val intent = Intent(activity, ProfileSelectionActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupUi()
        setupObservers()
    }

    private fun setupViewModel() {
        // With ViewModelFactory
        viewModel = ViewModelProvider(
            this, ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        )
            .get(ProfileSelectionViewModel::class.java)
    }

    private fun setupUi() {
        binding.continueBtn.setOnClickListener {
            HomeActivity.start(this@ProfileSelectionActivity)
        }
    }

    private fun setupObservers() {
        viewModel.getUsers().observe(this, Observer {
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
}