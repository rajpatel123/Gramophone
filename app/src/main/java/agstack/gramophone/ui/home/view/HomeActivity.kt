package agstack.gramophone.ui.home.view

import agstack.gramophone.R
import agstack.gramophone.BR
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityHomeBinding
import agstack.gramophone.ui.home.navigator.HomeActivityNavigator
import agstack.gramophone.ui.home.viewmodel.HomeViewModel
import android.os.Bundle
import androidx.activity.viewModels

class HomeActivity : BaseActivityWrapper<ActivityHomeBinding,HomeActivityNavigator,HomeViewModel>() ,HomeActivityNavigator{
    private lateinit var binding: ActivityHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun getLayoutID(): Int {
        return R.layout.activity_home
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): HomeViewModel {
        return homeViewModel
    }
}