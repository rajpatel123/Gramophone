package agstack.gramophone.ui.farm.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityViewAllFarmsBinding
import agstack.gramophone.ui.farm.adapter.ViewAllFarmsAdapter
import agstack.gramophone.ui.farm.model.Data
import agstack.gramophone.ui.farm.navigator.ViewAllFarmsNavigator
import agstack.gramophone.ui.farm.viewmodel.ViewAllFarmsViewModel
import agstack.gramophone.ui.home.view.fragments.market.model.CropData
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewAllFarmsActivity :
    BaseActivityWrapper<ActivityViewAllFarmsBinding, ViewAllFarmsNavigator, ViewAllFarmsViewModel>(),
    ViewAllFarmsNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbarTitle(getMessage(R.string.message_buy_your_crop))
        getViewModel().getFarms()

        viewDataBinding.swipeRefresh.setColorSchemeResources(R.color.blue)
        viewDataBinding.swipeRefresh.setOnRefreshListener {
            if (viewDataBinding.addFarmWrapper.viewOldFarmsLayout.visibility == View.GONE) {
                getViewModel().getOldFarms()
            } else {
                getViewModel().getFarms()
            }
            viewDataBinding.swipeRefresh.isRefreshing = false
        }

        viewDataBinding.addFarmWrapper.addFarmTitleLayout.setOnClickListener {
            openActivity(
                SelectCropActivity::class.java,
                null
            )
        }
        viewDataBinding.addFarmWrapper.txtWhyAddFarm.setOnClickListener {
            openActivity(
                WhyAddFarmActivity::class.java,
                null
            )
        }

        viewDataBinding.addFarmWrapper.viewOldFarmsLayout.setOnClickListener {
            viewDataBinding.addFarmWrapper.viewOldFarmsLayout.visibility = View.GONE
            setToolbarTitle(getMessage(R.string.old_farms_title))
            getViewModel().getOldFarms()
        }

    }

    override fun onBackPressed() {
        if (viewDataBinding.addFarmWrapper.viewOldFarmsLayout.visibility == View.GONE) {
            viewDataBinding.addFarmWrapper.viewOldFarmsLayout.visibility = View.VISIBLE
            setToolbarTitle(getMessage(R.string.message_buy_your_crop))
            getViewModel().getFarms()
        } else {
            super.onBackPressed()
        }
    }

    override fun setToolbarTitle(title: String) {
        setUpToolBar(true, title, R.drawable.ic_arrow_left, true)
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_view_all_farms
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): ViewAllFarmsViewModel {
        val viewModel: ViewAllFarmsViewModel by viewModels()
        return viewModel
    }

    override fun setViewAllFarmsAdapter(farmsList: List<List<Data>>, isCustomerFarm: Boolean) {
        viewDataBinding.rvFarms.adapter = ViewAllFarmsAdapter(
            farmsList,
            headerListener = {

            },
            contentListener = {
                if (isCustomerFarm) {
                    openActivity(CropGroupExplorerActivity::class.java, Bundle().apply {
                        putParcelableArrayList(
                            "cropList", it as ArrayList<Data>
                        )
                    })
                }
            },
            footerListener = {
                val selectedCrop = CropData(
                    cropId = it[0].crop_id,
                    cropName = it[0].crop_name,
                    cropImage = it[0].crop_image,
                )
                openActivity(
                    AddFarmActivity::class.java,
                    Bundle().apply {
                        putParcelable("selectedCrop", selectedCrop)
                    })
            },
        )
    }
}