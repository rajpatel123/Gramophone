package agstack.gramophone.ui.farm.view

import agstack.gramophone.BR
import agstack.gramophone.BuildConfig
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityViewAllFarmsBinding
import agstack.gramophone.ui.advisory.AdvisoryActivity
import agstack.gramophone.ui.farm.adapter.ViewAllFarmsAdapter
import agstack.gramophone.ui.farm.model.AddHarvestRequest
import agstack.gramophone.ui.farm.model.Data
import agstack.gramophone.ui.farm.model.FarmEvent
import agstack.gramophone.ui.farm.model.unit.GpApiResponseData
import agstack.gramophone.ui.farm.navigator.ViewAllFarmsNavigator
import agstack.gramophone.ui.farm.viewmodel.ViewAllFarmsViewModel
import agstack.gramophone.ui.home.view.fragments.market.model.CropData
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.EventBus
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import androidx.activity.viewModels
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
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
        getViewModel().redirectionScreen = intent?.extras?.getString(Constants.REDIRECTION_SOURCE)!!
        sendViewFormMoEngageEvents()
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

        disposable = EventBus.subscribe<FarmEvent>()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.text == "harvest_info_added")
                    refresh()
            }
    }

    override fun onBackPressed() {
        if (viewDataBinding.addFarmWrapper.viewOldFarmsLayout.visibility == View.GONE) {
            viewDataBinding.addFarmWrapper.viewOldFarmsLayout.visibility = View.VISIBLE
            viewDataBinding.addFarmWrapper.buttonWrapperLayout.visibility = View.VISIBLE
            viewDataBinding.rvFarmsOld.visibility = GONE

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
        disposable?.let { if (!it.isDisposed) it.dispose() }
    }

    override fun setViewAllFarmsAdapter(
        farmsList: List<List<Data>>,
        isCustomerFarms: Boolean,
        isOldFarms: Boolean,
    ) {
        if (farmsList.isEmpty()) {
            viewDataBinding.emptyView.visibility = View.VISIBLE
            viewDataBinding.rvFarms.visibility = View.GONE
        } else {
            viewDataBinding.emptyView.visibility = View.GONE
            viewDataBinding.rvFarms.visibility = View.VISIBLE
        }

        viewDataBinding.rvFarms.adapter = ViewAllFarmsAdapter(
            farmsList,
            headerListener = {
                if (it.size < 2) {
                    openActivity(AdvisoryActivity::class.java, Bundle().apply {
                        putInt(
                            Constants.FARM_ID,
                            it[0].farm_id?.toInt()!!
                        )
                        if (isCustomerFarms) {
                            putString(Constants.FARM_TYPE, "customer_farm")
                        } else {
                            putString(Constants.FARM_TYPE, "model_farm")
                        }
                        putString(Constants.CROP_NAME, it[0].crop_name)
                        putString(Constants.CROP_IMAGE, it[0].crop_image)
                        putString(Constants.CROP_REF_ID, it[0].farm_ref_id)
                        putInt(Constants.CROP_ID, it[0].crop_id)
                        putString(Constants.CROP_DURATION, it[0].crop_sowing_date)
                        putString(Constants.CROP_END_DATE, it[0].crop_anticipated_completed_date)
                        putString(Constants.CROP_STAGE, it[0].stage_name)
                        putString(Constants.CROP_DAYS, it[0].days)
                    })

                } else {
                    if (isCustomerFarms) {
                        openActivity(CropGroupExplorerActivity::class.java, Bundle().apply {
                            putParcelableArrayList(
                                "cropList", it as ArrayList<Data>
                            )
                            putBoolean("isOldFarms", isOldFarms)
                            putBoolean("isCustomerFarms", isCustomerFarms)
                            putParcelableArrayList("unitsList", units as ArrayList)
                            putString("farm_ref_id", it[0].farm_ref_id)
                            putString(Constants.REDIRECTION_SOURCE, "My Farms")
                        })
                    }
                }
            },
            contentListener = {
                if (isCustomerFarms) {
                    openActivity(CropGroupExplorerActivity::class.java, Bundle().apply {
                        putParcelableArrayList(
                            "cropList", it as ArrayList<Data>
                        )
                        putBoolean("isOldFarms", isOldFarms)
                        putBoolean("isCustomerFarms", isCustomerFarms)
                        putParcelableArrayList("unitsList", units as ArrayList)
                        putString("farm_ref_id", it[0].farm_ref_id)
                        putString(Constants.REDIRECTION_SOURCE, "My Farms")
                    })
                }
            },
            footerListener = {
                selectedCrop = CropData(
                    cropId = it[0].crop_id,
                    cropName = it[0].crop_name,
                    cropImage = it[0].crop_image,
                )

                if (it.size == 1 /*&& isOldFarms*/ && isCustomerFarms && it[0].is_crop_cycle_completed!!) {
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
            isOldFarms = isOldFarms,
            isCustomerFarms = isCustomerFarms,
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

    override fun setViewAllOldFarmsAdapter(
        farmsList: ArrayList<List<Data>>,
        isCustomerFarms: Boolean,
        isOldFarms: Boolean
    ) {

        if (farmsList.isEmpty()) {
            viewDataBinding.emptyView.visibility = View.VISIBLE
            viewDataBinding.rvFarmsOld.visibility = View.GONE
        } else {
            viewDataBinding.emptyView.visibility = View.GONE
            viewDataBinding.rvFarmsOld.visibility = View.VISIBLE
            viewDataBinding.rvFarms.visibility = GONE

        }

        viewDataBinding.rvFarmsOld.adapter = ViewAllFarmsAdapter(
            farmsList,
            headerListener = {
                if (it.size < 2) {
                    openActivity(AdvisoryActivity::class.java, Bundle().apply {
                        putInt(
                            Constants.FARM_ID,
                            it[0].farm_id?.toInt()!!
                        )
                        if (isCustomerFarms) {
                            putString(Constants.FARM_TYPE, "customer_farm")
                        } else {
                            putString(Constants.FARM_TYPE, "model_farm")
                        }
                        putString(Constants.CROP_NAME, it[0].crop_name)
                        putString(Constants.CROP_IMAGE, it[0].crop_image)
                        putString(Constants.CROP_REF_ID, it[0].farm_ref_id)
                        putInt(Constants.CROP_ID, it[0].crop_id)
                        putString(Constants.CROP_DURATION, it[0].crop_sowing_date)
                        putString(Constants.CROP_END_DATE, it[0].crop_anticipated_completed_date)
                        putString(Constants.CROP_STAGE, it[0].stage_name)
                        putString(Constants.CROP_DAYS, it[0].days)
                    })

                } else {
                    if (isCustomerFarms) {
                        openActivity(CropGroupExplorerActivity::class.java, Bundle().apply {
                            putParcelableArrayList(
                                "cropList", it as ArrayList<Data>
                            )
                            putBoolean("isOldFarms", isOldFarms)
                            putBoolean("isCustomerFarms", isCustomerFarms)
                            putParcelableArrayList("unitsList", units as ArrayList)
                            putString("farm_ref_id", it[0].farm_ref_id)
                            putString(Constants.REDIRECTION_SOURCE, "My Farms")
                        })
                    }
                }
            },
            contentListener = {
                if (isCustomerFarms) {
                    openActivity(CropGroupExplorerActivity::class.java, Bundle().apply {
                        putParcelableArrayList(
                            "cropList", it as ArrayList<Data>
                        )
                        putBoolean("isOldFarms", isOldFarms)
                        putBoolean("isCustomerFarms", isCustomerFarms)
                        putParcelableArrayList("unitsList", units as ArrayList)
                        putString("farm_ref_id", it[0].farm_ref_id)
                        putString(Constants.REDIRECTION_SOURCE, "My Farms")
                    })
                }
            },
            footerListener = {
                selectedCrop = CropData(
                    cropId = it[0].crop_id,
                    cropName = it[0].crop_name,
                    cropImage = it[0].crop_image,
                )

                if (it.size == 1 /*&& isOldFarms*/ && isCustomerFarms && it[0].is_crop_cycle_completed!!) {
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
            isOldFarms = isOldFarms,
            isCustomerFarms = isCustomerFarms,
        )
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    private fun sendViewFormMoEngageEvents() {
        val properties = Properties()
            .addAttribute(
                "Customer_Id",
                SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.CUSTOMER_ID)!!
            )
            .addAttribute("Redirection_Source", getViewModel().redirectionScreen)
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(this, "KA_View_AddFarm", properties)
    }
}