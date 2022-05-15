package agstack.gramophone.ui.apptour.view

import agstack.gramophone.R
import agstack.gramophone.Status
import agstack.gramophone.base.BaseActivity
import agstack.gramophone.retrofit.ApiHelper
import agstack.gramophone.retrofit.RetrofitBuilder
import agstack.gramophone.splash.viewmodel.AppTourViewModel
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class AppTourActivity : BaseActivity() {

    private lateinit var viewModel: AppTourViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apptour)
        setupUi()
    }



    private fun setupUi() {
        //TODO intialise ui components
    }


}