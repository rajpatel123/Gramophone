package agstack.gramophone.ui.unitconverter

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.settings.SettingsRepository
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class UnitConverterViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : BaseViewModel<UnitConvertorNavigator>() {
    // factors order - Bigha, Acre, Hectare, Square Feet
    var sqFeetBaseArray = doubleArrayOf(27221.90, 43560.00, 107639.00, 1.00)
    var itemValPosSpinner1:Int =0
    var itemValPosSpinner2:Int =0

    fun onSelectSpinner1Item(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
         itemValPosSpinner1 = pos
        getNavigator()?.onSpinner1ValueChanged_OutputChange()

    }


    fun onSelectSpinner2Item(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        itemValPosSpinner2=pos
        getNavigator()?.convertValue_SetOutput()
    }

    fun convertVal(
        inputEditText: EditText,
        inputSpinner: Spinner,
        outputEditText: EditText,
        outputSpinner: Spinner
    ) {
        try {
            if (inputEditText.getText() != null) {

                var value1 = inputEditText.getText().toString().toDouble()

                var spinner1SelectedIndex = inputSpinner?.getSelectedItemPosition();

                // Convert value to base unit value
                var baseValue1 = value1 * sqFeetBaseArray[spinner1SelectedIndex];

                var spinner2SelectedIndex = outputSpinner?.getSelectedItemPosition();

                // Obtain value2 after dividing by respective base unit
                var value2 = baseValue1 / sqFeetBaseArray[spinner2SelectedIndex];

                var finalValue = String.format("%.2f", value2)
                getNavigator()?.setOutputValue(finalValue)



            }
        } catch (error: NumberFormatException) {
            outputEditText.setText("");
        }

    }



    fun onSwitchClick(){
        getNavigator()?.switchValues(itemValPosSpinner1,itemValPosSpinner2)

    }

}