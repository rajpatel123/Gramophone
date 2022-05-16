package agstack.gramophone.ui.language.view

import agstack.gramophone.R
import agstack.gramophone.Status
import agstack.gramophone.base.BaseActivity
import agstack.gramophone.databinding.ActivityLanguageBinding
import agstack.gramophone.retrofit.ApiHelper
import agstack.gramophone.retrofit.RetrofitBuilder
import agstack.gramophone.ui.language.adapter.LanguageAdapter
import agstack.gramophone.ui.language.viewmodel.LanguageViewModel
import agstack.gramophone.ui.language.viewmodel.ViewModelFactory
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager

class LanguageActivity : BaseActivity() {

    private lateinit var viewModel: LanguageViewModel
    internal var binding: ActivityLanguageBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageBinding.inflate(layoutInflater);

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
        binding?.recyclerLanguage?.layoutManager = GridLayoutManager(this, 2)
        binding?.recyclerLanguage?.setHasFixedSize(true)
        binding?.recyclerLanguage?.adapter = LanguageAdapter()
    }

    private fun setupViewModel() {
        // With ViewModelFactory
        viewModel = ViewModelProvider(
            this, ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        )
            .get(LanguageViewModel::class.java)
    }
}