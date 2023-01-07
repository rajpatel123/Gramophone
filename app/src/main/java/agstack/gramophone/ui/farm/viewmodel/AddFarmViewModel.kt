package agstack.gramophone.ui.farm.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.farm.model.AddFarmRequest
import agstack.gramophone.ui.farm.navigator.AddFarmNavigator
import agstack.gramophone.ui.home.view.fragments.market.model.CropData
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.moengage.core.Properties
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AddFarmViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : BaseViewModel<AddFarmNavigator>() {

    var progress = MutableLiveData<Boolean>()
    var enableSaveButton = MutableLiveData<Boolean>()
    var addFarmRequest = AddFarmRequest()
    var selectedCrop : CropData? = null

    init {
        progress.value = false
        enableSaveButton.value = true
    }

    fun onDateViewClick(){
        getNavigator()?.showDatePicker()
    }

    fun onClickSaveAndContinue() {
        updateFarmValues()
        validateFarmValues()
    }

    private fun validateFarmValues(){
        if (addFarmRequest.field_name.isNullOrEmpty() ||
            addFarmRequest.crop_id.isNullOrEmpty()
        ) {
            getNavigator()?.showToast(getNavigator()?.getMessage(R.string.message_select_crop))
        } else if (addFarmRequest.area!! <= 0.0) {
            getNavigator()?.showToast(getNavigator()?.getMessage(R.string.message_enter_area))
        } else if (addFarmRequest.area!! > 500.0) {
            getNavigator()?.showToast(getNavigator()?.getMessage(R.string.message_valid_area_upper_bound))
        }else if (addFarmRequest.crop_sowing_date.isNullOrEmpty()) {
            getNavigator()?.showToast(getNavigator()?.getMessage(R.string.message_sowing_date))
        } else if (addFarmRequest.unit.isNullOrEmpty()) {
            getNavigator()?.showToast(getNavigator()?.getMessage(R.string.message_area_unit))
        } else {
            addFarm(addFarmRequest)
        }
    }


   private fun updateFarmValues(
    ) {
        selectedCrop?.cropName.let { addFarmRequest.field_name = it }
        selectedCrop?.cropId.let { addFarmRequest.crop_id = it.toString() }
        getNavigator()?.getArea().let { addFarmRequest.area = it }
        getNavigator()?.getDate().let { addFarmRequest.crop_sowing_date = it }
        getNavigator()?.getAreaUnit().let { addFarmRequest.unit = it?.unit }
        getNavigator()?.getAreaUnit().let { addFarmRequest.unit_id = it?.unit_id }
    }

    private fun addFarm(addFarmRequest: AddFarmRequest) {
        progress.value = true
        enableSaveButton.value = false
        val properties = Properties()
        properties.addAttribute(
            "Customer_Id",
            SharedPreferencesHelper.instance?.getString(
                SharedPreferencesKeys.CUSTOMER_ID
            )!!)
            .addAttribute("Crop",addFarmRequest.field_name)
            .addAttribute(" Sowing Date",addFarmRequest.crop_sowing_date)
            .addAttribute("Area",addFarmRequest.area)
            .setNonInteractive()
        viewModelScope.launch {
            try {
                val response = productRepository.addFarm(addFarmRequest)
                if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS
                    && response.body()?.gp_api_response_data != null
                ) {
                    val addFarmResponse = response.body()
                    getNavigator()?.onFarmAdded()

                    getNavigator()?.sendMoEngageEvent("KA_Add_Farm_Info", properties)


                }else{
                    getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
                progress.value = false
                enableSaveButton.value = true
            } catch (ex: Exception) {
                progress.value = false
                enableSaveButton.value = true
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
        }
    }

    fun getFarmUnits(type : String) {
        progress.value = true
        viewModelScope.launch {
            try {
                val response = productRepository.getFarmUnits(type = type)
                if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS
                    && response.body()?.gp_api_response_data != null
                ) {
                    val farmUnitsResponse = response.body()
                    progress.value = false
                    getNavigator()?.setFarmUnitsAdapter(farmUnitsResponse?.gp_api_response_data!!)
                }else{
                    progress.value = false
                    getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            } catch (ex: Exception) {
                progress.value = false
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
        }
    }
}