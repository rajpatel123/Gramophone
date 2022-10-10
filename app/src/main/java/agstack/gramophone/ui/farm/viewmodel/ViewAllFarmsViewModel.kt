package agstack.gramophone.ui.farm.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.farm.adapter.FarmAdapter
import agstack.gramophone.ui.farm.adapter.ViewAllFarmsAdapter
import agstack.gramophone.ui.farm.model.Data
import agstack.gramophone.ui.farm.model.FarmRequest
import agstack.gramophone.ui.farm.navigator.ViewAllFarmsNavigator
import agstack.gramophone.ui.farm.view.SelectCropActivity
import agstack.gramophone.utils.Constants
import android.content.Context
import android.content.Intent
import android.os.Bundle
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
    var progress = MutableLiveData<Boolean>()

    init {
        progress.value = false
    }

    fun getOldFarms() {
        getFarms(farmType = oldFarm)
    }

    fun getFarms(farmType : String = activeFarm) {
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
                    farmsList.addAll(farmsList)
                    farmsList.addAll(farmsList)

                    getNavigator()?.setViewAllFarmsAdapter(
                       farmsList, isCustomerFarms
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
}