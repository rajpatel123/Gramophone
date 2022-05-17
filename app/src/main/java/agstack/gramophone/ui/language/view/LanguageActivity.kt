package agstack.gramophone.ui.language.view

import agstack.gramophone.R
import agstack.gramophone.Status
import agstack.gramophone.base.BaseActivity
import agstack.gramophone.databinding.ActivityLanguageBinding
import agstack.gramophone.retrofit.ApiHelper
import agstack.gramophone.retrofit.RetrofitBuilder
import agstack.gramophone.ui.language.adapter.LanguageAdapter
import agstack.gramophone.ui.language.model.LanguageData
import agstack.gramophone.ui.language.viewmodel.LanguageViewModel
import agstack.gramophone.ui.language.viewmodel.ViewModelFactory
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView

class LanguageActivity : BaseActivity() {

    private lateinit var viewModel: LanguageViewModel
    internal var binding: ActivityLanguageBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)

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
        /* binding?.recyclerLanguage?.layoutManager = GridLayoutManager(this, 2)
         binding?.recyclerLanguage?.setHasFixedSize(true)
         binding?.recyclerLanguage?.adapter = LanguageAdapter()*/
        val languageList = ArrayList<LanguageData>()
        languageList.add(LanguageData("English", "English"))
        languageList.add(LanguageData("हिंदी", "Hindi"))
        languageList.add(LanguageData("मराठी", "Marathi"))
        languageList.add(LanguageData("ગુજરાતી", "Gujrati"))

        val recyclerLanguage = findViewById<RecyclerView>(R.id.recycler_language)
        recyclerLanguage?.setHasFixedSize(true)
        recyclerLanguage?.adapter = LanguageAdapter(languageList)
    }

    private fun setupViewModel() {
        // With ViewModelFactory
        viewModel = ViewModelProvider(
            this, ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        )
            .get(LanguageViewModel::class.java)
    }
}