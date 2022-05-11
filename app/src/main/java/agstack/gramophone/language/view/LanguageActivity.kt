package agstack.gramophone.language.view

import agstack.gramophone.R
import agstack.gramophone.Status
import agstack.gramophone.base.BaseActivity
import agstack.gramophone.retrofit.ApiHelper
import agstack.gramophone.retrofit.RetrofitBuilder
import agstack.gramophone.language.viewmodel.LanguageViewModel
import agstack.gramophone.language.viewmodel.ViewModelFactory
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class LanguageActivity : BaseActivity() {

    private lateinit var viewModel: LanguageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setupViewModel()
        setupUi()
        setupObservers()
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


    private fun setupUi() {
        //TODO intialise ui components
    }

    private fun setupViewModel() {
        // With ViewModelFactory
        viewModel = ViewModelProvider(
            this, ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        )
            .get(LanguageViewModel::class.java)
    }
}