package agstack.gramophone.ui.farm.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityCropGroupExplorerBinding
import agstack.gramophone.ui.farm.adapter.CropGroupExplorerAdapter
import agstack.gramophone.ui.farm.model.Data
import agstack.gramophone.ui.farm.navigator.CropGroupExplorerNavigator
import agstack.gramophone.ui.farm.viewmodel.CropGroupExplorerViewModel
import agstack.gramophone.ui.home.view.fragments.market.model.CropData
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CropGroupExplorerActivity :
    BaseActivityWrapper<ActivityCropGroupExplorerBinding, CropGroupExplorerNavigator, CropGroupExplorerViewModel>(),
    CropGroupExplorerNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUi()
    }

    private fun setUi(){
        intent.extras?.let {
            if (it.containsKey("cropList")) {
                it.getParcelableArrayList<Data>("cropList")?.let { list ->
                    setToolbarTitle(list[0]?.crop_name ?: "")
                    setAdapter(list)
                }
            }
        }
    }

    override fun setToolbarTitle(title: String) {
        setUpToolBar(true, title, R.drawable.ic_action_navigation_arrow_back, true)
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_crop_group_explorer
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): CropGroupExplorerViewModel {
        val viewModel: CropGroupExplorerViewModel by viewModels()
        return viewModel
    }

    override fun setAdapter(cropList: List<Data>) {
        viewDataBinding.rvCrops.adapter = CropGroupExplorerAdapter(cropList, {}, {

            val selectedCrop = CropData(
                cropId = it.crop_id,
                cropName = it.crop_name,
                cropImage = it.crop_image,
            )
            openActivity(
                AddFarmActivity::class.java,
                Bundle().apply {
                    putParcelable("selectedCrop", selectedCrop)
                })
        })
    }

}