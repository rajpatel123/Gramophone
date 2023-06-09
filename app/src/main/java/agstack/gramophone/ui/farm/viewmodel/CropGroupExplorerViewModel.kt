package agstack.gramophone.ui.farm.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.farm.model.AddHarvestRequest
import agstack.gramophone.ui.farm.model.Data
import agstack.gramophone.ui.farm.model.DeletefarmReqquestModel
import agstack.gramophone.ui.farm.navigator.CropGroupExplorerNavigator
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.Utility
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class CropGroupExplorerViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val onBoardingRepository: OnBoardingRepository
    ) :
    BaseViewModel<CropGroupExplorerNavigator>() {

    var progress = MutableLiveData<Boolean>()

    init {
        progress.value = false
    }

    fun addHarvestQues(body: AddHarvestRequest) {
        progress.value = true

        if (getNavigator()?.isNetworkAvailable()==true){
            viewModelScope.launch {
                try {
                    val response = productRepository.addHarvestQues(body)
                    if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS
                        && response.body()?.gp_api_response_data != null
                    ) {
                        val res = response.body()
                        getNavigator()?.onAddHarvestQues()
                    }else{
//                    getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                    }
                    progress.value = false
                } catch (ex: Exception) {
                    progress.value = false
                    when (ex) {
                        is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
//                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                    }
                }
            }
        }
    }

    fun getFarmUnits(type : String) {
        progress.value = true
        if (getNavigator()?.isNetworkAvailable()==true) {
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
//                    getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                    }
                } catch (ex: Exception) {
                    progress.value = false
                    when (ex) {
                        is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
//                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                    }
                }
            }

        }
    }

    fun deleteFarm(it: Data) {
        if (getNavigator()?.isNetworkAvailable()==true) {
            viewModelScope.launch {
                try {
                    progress.value = true

                    val response = onBoardingRepository.deleteFarm(DeletefarmReqquestModel(it.crop_id.toString(),it.farm_id.toString()))
                    if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS
                        && response.body()?.gp_api_response_data != null
                    ) {
                        progress.value = false
                        getNavigator()?.refreshFarm(it)
                    }else{
                        progress.value = false
                        getNavigator()?.showToast(Utility.getErrorMessage(response.errorBody()))
                    }
                } catch (ex: Exception) {
                    progress.value = false
                    when (ex) {
                        is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
//                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                    }
                }
            }
        }

    }
}