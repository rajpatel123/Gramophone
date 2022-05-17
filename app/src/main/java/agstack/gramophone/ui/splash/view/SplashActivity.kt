package agstack.gramophone.ui.splash.view

import agstack.gramophone.R
import agstack.gramophone.Status
import agstack.gramophone.base.BaseActivity
import agstack.gramophone.ui.language.view.LanguageActivity
import agstack.gramophone.retrofit.ApiHelper
import agstack.gramophone.retrofit.RetrofitBuilder
import agstack.gramophone.ui.home.view.HomeActivity
import agstack.gramophone.ui.splash.model.SplashModel
import agstack.gramophone.ui.splash.viewmodel.SplashViewModel
import agstack.gramophone.ui.splash.viewmodel.ViewModelFactory
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class SplashActivity : BaseActivity() {

    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setupViewModel()
        setupUi()
        setupObservers()
    }

    private fun setupViewModel() {
        // With ViewModelFactory
        viewModel = ViewModelProvider(
            this, ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        )
            .get(SplashViewModel::class.java)
    }

    private fun setupUi() {
        viewModel.initSplashScreen()
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

        val observer = Observer<SplashModel> {
            val intent = Intent(this, LanguageActivity::class.java)
            startActivity(intent)
            finish()
        }
        viewModel.liveData.observe(this, observer)
    }
}