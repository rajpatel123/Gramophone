package agstack.gramophone.ui.farm.view

import agstack.gramophone.BR
import agstack.gramophone.BuildConfig
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.AddFarmActivityBinding
import agstack.gramophone.ui.farm.model.FarmEvent
import agstack.gramophone.ui.farm.model.unit.GpApiResponseData
import agstack.gramophone.ui.farm.navigator.AddFarmNavigator
import agstack.gramophone.ui.farm.viewmodel.AddFarmViewModel
import agstack.gramophone.utils.EventBus
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class AddFarmActivity :
    BaseActivityWrapper<AddFarmActivityBinding, AddFarmNavigator, AddFarmViewModel>(),
    AddFarmNavigator {

    var myCalendar: Calendar = Calendar.getInstance()
    var units: List<GpApiResponseData>? = null
    var selectedUnit: GpApiResponseData? = null

    companion object {
        fun start(activity: AppCompatActivity) {
            activity.startActivity(Intent(activity, AddFarmActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolBar()
        if (getBundle() != null && getBundle()?.containsKey("selectedCrop")!!) {
            getViewModel().selectedCrop = getBundle()?.getParcelable("selectedCrop")
        }

        viewDataBinding.edtAreaUnit.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    selectedUnit = units?.get(position)
                }
            }
        getViewModel().getFarmUnits("farm")
    }

    fun getBundle(): Bundle? {
        return intent.extras
    }

    private fun setToolBar() {
        viewDataBinding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun getLayoutID(): Int {
        return R.layout.add_farm_activity
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): AddFarmViewModel {
        val viewModel: AddFarmViewModel by viewModels()
        return viewModel
    }

    override fun onFarmAdded() {
        EventBus.post(FarmEvent("farm_added"))
        showToast(getMessage(R.string.message_farm_added))
        finish()
    }

    override fun setFarmUnitsAdapter(units: List<GpApiResponseData>) {
        val adapter = ArrayAdapter(this, R.layout.simple_list_item, units)
        viewDataBinding.edtAreaUnit.adapter = adapter
        this.units = units
    }

    override fun showDatePicker() {
        val onDateSetListener = OnDateSetListener { view, year, month, day ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, day)
            updateLabel()
        }

        DatePickerDialog(this@AddFarmActivity,
            onDateSetListener,
            myCalendar[Calendar.YEAR],
            myCalendar[Calendar.MONTH],
            myCalendar[Calendar.DAY_OF_MONTH]
        ).show()
    }

    private fun updateLabel() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        viewDataBinding.txtDate.text = dateFormat.format(myCalendar.time)
    }

    override fun getDate(): String {
        return viewDataBinding.txtDate.text.toString()
    }

    override fun getArea(): Double {
        try {
            return viewDataBinding.edtArea.text.toString().toDouble()
        } catch (ex: NumberFormatException) {

        }
        return 0.0
    }

    override fun getAreaUnit(): GpApiResponseData? {
        return selectedUnit
    }

    override fun sendSaveFarmMoEngageEvents(
        farmId: String,
        crop: String,
        sowingDate: String,
        area: String,
    ) {
        val properties = Properties()
            .addAttribute("Customer_Id",
                SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.CUSTOMER_ID)!!)
            .addAttribute("Farm_ID", farmId)
            .addAttribute("Crop", crop)
            .addAttribute("Sowing_Date", sowingDate)
            .addAttribute("Area", area)
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(this, "KA_Click_SaveFarm", properties)
    }
}


