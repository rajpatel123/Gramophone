package agstack.gramophone.ui.unitconverter

import agstack.gramophone.BR
import agstack.gramophone.BuildConfig
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.UnitConverterActivityBinding
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.amnix.xtension.extensions.enableIf
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UnitConverterActivity :
    BaseActivityWrapper<UnitConverterActivityBinding, UnitConvertorNavigator, UnitConverterViewModel>(),
    UnitConvertorNavigator {

    private val unitViewModel: UnitConverterViewModel by viewModels()

    override fun getLayoutID(): Int {
        return R.layout.unit_converter_activity
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): UnitConverterViewModel {
        return unitViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolBar(true, resources.getString(R.string.unit_convertor), R.drawable.ic_arrow_left)
        initViews()
        sendViewConverterMoEngageEvent()
    }

    private fun initViews() {
        viewDataBinding.editTextUnit1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                try {
                    convertValue_SetOutput()
                } catch (error: NumberFormatException) {
                    viewDataBinding.editTextUnit2.setText("")
                }
            }

        })
        val mUnitsList = resources.getStringArray(R.array.units_kind_array)
        val unitKindArrayAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.spinner_item, mUnitsList)
        unitKindArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        viewDataBinding.spinnerArea.enableIf(false)
        viewDataBinding.spinnerArea.isClickable = false
        viewDataBinding.spinnerArea.setAdapter(unitKindArrayAdapter)

    }


    override fun setOutputValue(finalValue: String) {
        viewDataBinding.editTextUnit2.setText(finalValue)
    }

    override fun onSpinner1ValueChanged_OutputChange() {

        if (viewDataBinding.editTextUnit1.getText() != null && viewDataBinding.editTextUnit1.getText().length > 0) {
            convertValue_SetOutput()
        }

    }


    override fun convertValue_SetOutput() {
        mViewModel?.convertVal(
            viewDataBinding.editTextUnit1,
            viewDataBinding.spinnerUnit1,
            viewDataBinding.editTextUnit2,
            viewDataBinding.spinnerUnit2
        )
    }

    override fun switchValues(itemValPosSpinner1: Int, itemValPosSpinner2: Int) {
        viewDataBinding.spinnerUnit1.setSelection(itemValPosSpinner2)
        viewDataBinding.spinnerUnit2.setSelection(itemValPosSpinner1)
    }

    private fun sendViewConverterMoEngageEvent() {
        val properties = Properties()
            .addAttribute("Profile ID",
                SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.CUSTOMER_ID)!!)
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(this, "KA_View converter", properties)
    }
}