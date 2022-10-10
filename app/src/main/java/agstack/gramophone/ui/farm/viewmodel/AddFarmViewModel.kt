package agstack.gramophone.ui.farm.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.farm.model.AddFarmRequest
import agstack.gramophone.ui.farm.navigator.AddFarmNavigator
import agstack.gramophone.ui.home.view.fragments.market.model.CropData
import agstack.gramophone.utils.Constants
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AddFarmViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : BaseViewModel<AddFarmNavigator>() {

    var progress = MutableLiveData<Boolean>()
    var addFarmRequest = AddFarmRequest()
    var selectedCrop : CropData? = null

    init {
        progress.value = false
    }

    fun onDateViewClick(){
        getNavigator()?.showDatePicker()
    }

    fun onClickSaveAndContinue() {
        updateFarm()
        validateFarmValues()
    }

    private fun validateFarmValues(){
        if (addFarmRequest.field_name.isNullOrEmpty() ||
            addFarmRequest.crop_id.isNullOrEmpty()
        ) {
            getNavigator()?.showToast("Please select a crop")
        } else if (addFarmRequest.area!! <= 0.0) {
            getNavigator()?.showToast("Please enter the area")
        } else if (addFarmRequest.crop_sowing_date.isNullOrEmpty()) {
            getNavigator()?.showToast("Please select sowing date")
        } else if (addFarmRequest.unit.isNullOrEmpty()) {
            getNavigator()?.showToast("Please select area unit")
        } else {
            addFarm(addFarmRequest)
        }
    }


   private fun updateFarm(
    ) {
        selectedCrop?.cropName.let { addFarmRequest.field_name = it }
        selectedCrop?.cropId.let { addFarmRequest.crop_id = it.toString() }
        getNavigator()?.getArea().let { addFarmRequest.area = it }
        getNavigator()?.getDate().let { addFarmRequest.crop_sowing_date = it }
        getNavigator()?.getAreaUnit().let { addFarmRequest.unit = it }
    }

    private fun addFarm(addFarmRequest: AddFarmRequest) {
        progress.value = true
        viewModelScope.launch {
            try {
                val response = productRepository.addFarm(addFarmRequest)
                if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS
                    && response.body()?.gp_api_response_data != null
                ) {
                    val addFarmResponse = response.body()
                    progress.value = false
                    getNavigator()?.onFarmAdded()
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

    fun getFarmUnits() {
        progress.value = true
        viewModelScope.launch {
            try {
                val response = productRepository.getFarmUnits()
                if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS
                    && response.body()?.gp_api_response_data != null
                ) {
                    val farmUnitsResponse = response.body()
                    progress.value = false
                    getNavigator()?.setFarmUnitsAdapter(farmUnitsResponse?.gp_api_response_data!!)
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