package agstack.gramophone.ui.unitconverter

import agstack.gramophone.base.BaseNavigator

interface UnitConvertorNavigator :BaseNavigator {
    fun setOutputValue(finalValue: String)
    fun onSpinner1ValueChanged_OutputChange()
     fun convertValue_SetOutput()
     fun switchValues(itemValPosSpinner1: Int, itemValPosSpinner2: Int)
}