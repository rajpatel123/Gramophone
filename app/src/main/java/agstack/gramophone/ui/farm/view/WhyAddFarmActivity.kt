package agstack.gramophone.ui.farm.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityWhyAddFarmBinding
import agstack.gramophone.ui.farm.navigator.WhyAddFarmNavigator
import agstack.gramophone.ui.farm.viewmodel.WhyAddFarmViewModel
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WhyAddFarmActivity  : BaseActivityWrapper<ActivityWhyAddFarmBinding, WhyAddFarmNavigator, WhyAddFarmViewModel>(),
    WhyAddFarmNavigator {

    companion object {
        fun start(activity : AppCompatActivity){
            activity.startActivity(Intent(activity, WhyAddFarmActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbarTitle("Why Add Farm")
    }

    override fun setToolbarTitle(title: String) {
        setUpToolBar(true, title, R.drawable.ic_cross)
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_why_add_farm
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): WhyAddFarmViewModel {
        val viewModel: WhyAddFarmViewModel by viewModels()
        return viewModel
    }

}