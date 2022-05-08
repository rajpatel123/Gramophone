package agstack.gramophone.splash.view

import agstack.gramophone.R
import agstack.gramophone.Status
import agstack.gramophone.retrofit.ApiHelper
import agstack.gramophone.retrofit.RetrofitBuilder
import agstack.gramophone.splash.adapter.MainAdapter
import agstack.gramophone.splash.viewmodel.SplashViewModel
import agstack.gramophone.splash.viewmodel.ViewModelFactory
import android.os.Bundle
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

class SplashActivity : AppCompatActivity() {

    private lateinit var viewModel: SplashViewModel
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setupViewModel()
        setupUi()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.getUsers().observe(this, Observer { it?.let {
            resource ->
            when(resource.status){
                Status.SUCCESS->{

                }
                Status.ERROR->{

                }

                Status.LOADING->{

                }
            }
        } })
    }


    private fun setupUi() {
        //TODO intialise ui components
    }

    private fun setupViewModel() {
        // With ViewModelFactory
         viewModel = ViewModelProvider(
             this, ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
         )
                   .get(SplashViewModel::class.java)
    }
}