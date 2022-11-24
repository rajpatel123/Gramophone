package agstack.gramophone.ui.farm.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityCropGroupExplorerBinding
import agstack.gramophone.ui.advisory.AdvisoryActivity
import agstack.gramophone.ui.farm.adapter.CropGroupExplorerAdapter
import agstack.gramophone.ui.farm.model.AddHarvestRequest
import agstack.gramophone.ui.farm.model.Data
import agstack.gramophone.ui.farm.model.FarmEvent
import agstack.gramophone.ui.farm.model.unit.GpApiResponseData
import agstack.gramophone.ui.farm.navigator.CropGroupExplorerNavigator
import agstack.gramophone.ui.farm.viewmodel.CropGroupExplorerViewModel
import agstack.gramophone.ui.home.view.fragments.market.model.CropData
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.EventBus
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CropGroupExplorerActivity :
    BaseActivityWrapper<ActivityCropGroupExplorerBinding, CropGroupExplorerNavigator, CropGroupExplorerViewModel>(),
    CropGroupExplorerNavigator {
    var isOldFarms = false
    var isCustomerFarms = false
    var units: List<GpApiResponseData>? = null
    var farmRefId: String? = null
    var bottomSheet: BottomSheetFarmInformation? = null
    var selectedCrop: CropData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUi()
    }

    private fun setUi() {
        intent.extras?.let {
            if (it.containsKey("isOldFarms")) {
                isOldFarms = it.getBoolean("isOldFarms")
            }

            if (it.containsKey("isCustomerFarms")) {
                isCustomerFarms = it.getBoolean("isCustomerFarms")
            }

            if (it.containsKey("cropList")) {
                it.getParcelableArrayList<Data>("cropList")?.let { list ->
                    setToolbarTitle(list[0]?.crop_name ?: "")
                    setAdapter(list)
                }
            }

            if (it.containsKey("unitsList")) {
                units = it.getParcelableArrayList("unitsList")
            }else{
                getViewModel().getFarmUnits("harvest")
            }

            if (it.containsKey("farm_ref_id")) {
                farmRefId = it.getString("farm_ref_id")
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
        viewDataBinding.rvCrops.adapter = CropGroupExplorerAdapter(
            cropList,
            headerListener = {
                openActivity(AdvisoryActivity::class.java,Bundle().apply {
                    putInt(
                        Constants.FARM_ID,
                        it.farm_id?.toInt()!!
                    )
                    putString(Constants.FARM_TYPE,"customer_farm")
                    putString(Constants.CROP_NAME,it.crop_name)
                    putString(Constants.CROP_IMAGE,it.crop_image)
                    putString(Constants.CROP_REF_ID,it.farm_ref_id)
                    putInt(Constants.CROP_ID,it.crop_id)
                    putString(Constants.CROP_DURATION,it.crop_sowing_date)
                    putString(Constants.CROP_END_DATE,it.crop_anticipated_completed_date)
                    putString(Constants.CROP_STAGE,it.stage_name)
                    putString(Constants.CROP_DAYS,it.days)
                })
            },
            contentListener = {

            },
            footerListener = {
                selectedCrop = CropData(
                    cropId = it.crop_id,
                    cropName = it.crop_name,
                    cropImage = it.crop_image,
                )

                if (isCustomerFarms && it.is_crop_cycle_completed!!) {
                    if (it.harvested_quantity.isNullOrEmpty()) {
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
                        bundle.putString("farm_ref_id", it.farm_ref_id)
                        bottomSheet?.arguments = bundle
                        bottomSheet?.show(
                            this@CropGroupExplorerActivity.supportFragmentManager,
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
            isOldFarms = isOldFarms,
            isCustomerFarms = isCustomerFarms
        )
    }

    override fun onAddHarvestQues() {
        bottomSheet?.dismiss()

        val dialog = InfoSubmittedDialog(
            listener1 = {
                EventBus.post(FarmEvent("harvest_info_added"))
                finish()
            },

            listener2 = {
                openActivity(
                    AddFarmActivity::class.java,
                    Bundle().apply {
                        putParcelable("selectedCrop", selectedCrop)
                    })

                EventBus.post(FarmEvent("harvest_info_added"))
                finish()
            },
        )

        dialog.show(this@CropGroupExplorerActivity.supportFragmentManager, "custom_sheet")
    }

    override fun setFarmUnits(units: List<GpApiResponseData>) {
        this.units = units
    }
}