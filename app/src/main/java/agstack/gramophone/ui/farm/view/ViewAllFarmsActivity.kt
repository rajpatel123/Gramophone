package agstack.gramophone.ui.farm.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityViewAllFarmsBinding
import agstack.gramophone.ui.farm.adapter.ViewAllFarmsAdapter
import agstack.gramophone.ui.farm.model.AddHarvestRequest
import agstack.gramophone.ui.farm.model.Data
import agstack.gramophone.ui.farm.model.FarmEvent
import agstack.gramophone.ui.farm.model.unit.GpApiResponseData
import agstack.gramophone.ui.farm.navigator.ViewAllFarmsNavigator
import agstack.gramophone.ui.farm.viewmodel.ViewAllFarmsViewModel
import agstack.gramophone.ui.home.view.fragments.market.model.CropData
import agstack.gramophone.utils.EventBus
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable

@AndroidEntryPoint
class ViewAllFarmsActivity :
    BaseActivityWrapper<ActivityViewAllFarmsBinding, ViewAllFarmsNavigator, ViewAllFarmsViewModel>(),
    ViewAllFarmsNavigator {

    var bottomSheet: BottomSheetFarmInformation? = null
    var units: List<GpApiResponseData>? = null
    var selectedCrop: CropData? = null
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbarTitle(getMessage(R.string.message_buy_your_crop))
        getViewModel().getFarms()
        getViewModel().getFarmUnits("harvest")

        viewDataBinding.swipeRefresh.setColorSchemeResources(R.color.blue)
        viewDataBinding.swipeRefresh.setOnRefreshListener {
            refresh()
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
            viewDataBinding.addFarmWrapper.buttonWrapperLayout.visibility = View.GONE
            setToolbarTitle(getMessage(R.string.old_farms_title))
            getViewModel().getOldFarms()
        }

        disposable =  EventBus.subscribe<FarmEvent>()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if(it.text ==  "harvest_info_added")
                    refresh()
            }
    }

    override fun onBackPressed() {
        if (viewDataBinding.addFarmWrapper.viewOldFarmsLayout.visibility == View.GONE) {
            viewDataBinding.addFarmWrapper.viewOldFarmsLayout.visibility = View.VISIBLE
            viewDataBinding.addFarmWrapper.buttonWrapperLayout.visibility = View.VISIBLE
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

    override fun setFarmUnits(units: List<GpApiResponseData>) {
        this.units = units
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.let { if(!it.isDisposed) it.dispose() }
    }

    override fun setViewAllFarmsAdapter(
        farmsList: List<List<Data>>,
        isCustomerFarm: Boolean,
        isOldFarms: Boolean
    ) {
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
                        putBoolean("isOldFarms", isOldFarms)
                        putParcelableArrayList("unitsList", units as ArrayList)
                        putString("farm_ref_id", it[0].farm_ref_id)
                    })
                }
            },
            footerListener = {
                selectedCrop = CropData(
                    cropId = it[0].crop_id,
                    cropName = it[0].crop_name,
                    cropImage = it[0].crop_image,
                )

                if (it.size == 1 && isOldFarms && it[0].is_crop_cycle_completed!!) {
                    if (it[0].harvested_quantity.isNullOrEmpty()) {
                        bottomSheet =
                            BottomSheetFarmInformation { farm_reference_id, output_qty, output_unit_id ->
                                getViewModel().addHarvestQues(
                                    AddHarvestRequest(
                                        farm_reference_id,
                                        output_qty,
                                        output_unit_id
                                    )
                                )
                            }
                        val bundle = Bundle()
                        bundle.putParcelableArrayList("unitsList", units as ArrayList)
                        bundle.putString("farm_ref_id", it[0].farm_ref_id)
                        bottomSheet?.arguments = bundle
                        bottomSheet?.show(
                            this@ViewAllFarmsActivity.supportFragmentManager,
                            "bottom_sheet"
                        )
                    } else {
                        // do nothing
                    }
                } else {
                    if (isOldFarms) {
                        // do nothing
                    } else {
                        openActivity(
                            AddFarmActivity::class.java,
                            Bundle().apply {
                                putParcelable("selectedCrop", selectedCrop)
                            })
                    }
                }
            },
            isOldFarms = isOldFarms
        )
    }

    private fun refresh() {
        if (viewDataBinding.addFarmWrapper.viewOldFarmsLayout.visibility == View.GONE) {
            getViewModel().getOldFarms()
        } else {
            getViewModel().getFarms()
        }
        viewDataBinding.swipeRefresh.isRefreshing = false
    }

    override fun onAddHarvestQues() {
        bottomSheet?.dismiss()
        refresh()

        val dialog = InfoSubmittedDialog({}, {
            openActivity(
                AddFarmActivity::class.java,
                Bundle().apply {
                    putParcelable("selectedCrop", selectedCrop)
                })
        })

        dialog.show(this@ViewAllFarmsActivity.supportFragmentManager, "custom_sheet")
    }
}