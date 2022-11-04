package agstack.gramophone.ui.farm.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.farm.model.AddHarvestRequest
import agstack.gramophone.ui.farm.model.Data
import agstack.gramophone.ui.farm.model.FarmRequest
import agstack.gramophone.ui.farm.navigator.ViewAllFarmsNavigator
import agstack.gramophone.utils.Constants
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amnix.xtension.extensions.isNotNullOrEmpty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ViewAllFarmsViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : BaseViewModel<ViewAllFarmsNavigator>() {

    private val oldFarm = "historical"
    private val activeFarm = "active"
    private var farmType = activeFarm
    var progress = MutableLiveData<Boolean>()

    init {
        progress.value = false
    }

    private fun isOldFarms(): Boolean {
        return farmType == oldFarm
    }

    fun getOldFarms() {
        getFarms(farmType = oldFarm)
    }

    fun getFarms(farmType : String = activeFarm) {
        this.farmType = farmType
        progress.value = true

        viewModelScope.launch {
            try {
                val response = productRepository.getFarmsData(farmType, FarmRequest("99", "0"))
                if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS
                    && response.body()?.gp_api_response_data != null
                ) {
                    val farmResponse = response.body()
                    val farmsList = ArrayList<List<Data>>()
                    var isCustomerFarms = false
                    if (farmResponse?.gp_api_response_data?.customer_farm != null
                        && farmResponse.gp_api_response_data.customer_farm.data.isNotNullOrEmpty()
                    ) {
                        isCustomerFarms = true
                        farmsList.addAll(farmResponse.gp_api_response_data.customer_farm.data)
                    } else if (farmResponse?.gp_api_response_data?.model_farm != null &&
                        farmResponse.gp_api_response_data.model_farm.data.isNotNullOrEmpty()
                    ) {
                        isCustomerFarms = false
                        farmsList.addAll(farmResponse.gp_api_response_data.model_farm.data)
                    }

                    getNavigator()?.setViewAllFarmsAdapter(
                       farmsList, isCustomerFarms, isOldFarms()
                    )
                }
                progress.value = false
            } catch (ex: Exception) {
                progress.value = false
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
                    getNavigator()?.setFarmUnits(farmUnitsResponse?.gp_api_response_data!!)
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


    fun addHarvestQues(body: AddHarvestRequest) {
        progress.value = true

        viewModelScope.launch {
            try {
                val response = productRepository.addHarvestQues(body)
                if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS
                    && response.body()?.gp_api_response_data != null
                ) {
                    val res = response.body()
                    getNavigator()?.onAddHarvestQues()
                }else{
                    getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
                progress.value = false
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