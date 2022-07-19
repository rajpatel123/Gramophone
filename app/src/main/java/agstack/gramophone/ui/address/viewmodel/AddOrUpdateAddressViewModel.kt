package agstack.gramophone.ui.address.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.address.AddressNavigator
import agstack.gramophone.ui.address.model.AddressRequestModel
import agstack.gramophone.ui.address.model.AddressResponseModel
import agstack.gramophone.ui.address.model.State
import agstack.gramophone.ui.address.model.UpdateAddressRequestModel
import agstack.gramophone.ui.login.model.SendOtpResponseModel
import agstack.gramophone.utils.ApiResponse
import agstack.gramophone.utils.Constants
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class AddOrUpdateAddressViewModel @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository
) : BaseViewModel<AddressNavigator>() {

    var state: State? = null
    var stateNameStr = ObservableField<String>()
    var districtName: String? = ""
    var tehsilName: String? = ""
    var villageName: String? = ""
    var pinCode: String? = ""
    var address: String? = ""
    var isStateSelected: Boolean? = false

    fun onStateUpdate(v: View) {
        if (state != null) {
            val bundle = Bundle()
            bundle.putString(Constants.STATE, state?.name)
            //getNavigator()?.moveToNext(bundle)
        } else {
            getNavigator()?.onError(getNavigator()?.getMessage(R.string.select_state_error))
        }


    }

    fun submitAddress() {
        if (TextUtils.isEmpty(stateNameStr.get())) {
            getNavigator()?.onError(getNavigator()?.getMessage(R.string.state_required))
            return
        }
        if (TextUtils.isEmpty(districtName)) {
            getNavigator()?.onError(getNavigator()?.getMessage(R.string.district_required))
            return
        }

        if (TextUtils.isEmpty(tehsilName)) {
            getNavigator()?.onError(getNavigator()?.getMessage(R.string.tehsil_required))
            return
        }

        if (TextUtils.isEmpty(villageName)) {
            getNavigator()?.onError(getNavigator()?.getMessage(R.string.village_required))
            return
        }

        if (TextUtils.isEmpty(address)) {
            getNavigator()?.onError(getNavigator()?.getMessage(R.string.address_required))
            return
        }

        val updateAddressRequestModel= UpdateAddressRequestModel(address,districtName,pinCode,stateNameStr.get(),tehsilName,villageName)
         viewModelScope.launch {
           updateCompleteAddress(updateAddressRequestModel)
        }

    }

       suspend  fun updateCompleteAddress(updateAddressRequestModel: UpdateAddressRequestModel) {
           getNavigator()?.onLoading()
           try {
               if (getNavigator()?.isNetworkAvailable() == true) {
                   val response = onBoardingRepository.updateAddress(updateAddressRequestModel)

                   val updateAddress = handleUpdateAddressResponse(response).data

                if (Constants.GP_API_STATUS.equals(updateAddress?.gp_api_status)) {
                    getNavigator()?.onSuccess(updateAddress?.gp_api_message!!)
                    getNavigator()?.goToApp()
                }

               } else
                   getNavigator()?.onError(getNavigator()?.getMessage(R.string.no_internet)!!)
           } catch (ex: Exception) {
               when (ex) {
                   is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.network_failure)!!)
                   else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
               }
           }
      }

    private suspend fun getDistrictList() {
        getNavigator()?.onLoading()
        try {
            if (getNavigator()?.isNetworkAvailable() == true) {
                val response = onBoardingRepository.getDistrict(
                    "state",
                    AddressRequestModel("", "", "", "", "", "")
                )

                val stateListData = handleGetStateListResponse(response).data

//                if (Constants.GP_API_STATUS.equals(stateListData?.gp_api_status)) {
//                    getNavigator()?.updateDistrict(stateListData?.gp_api_response_data?.state_list!!) {
//
//                    }
//                }

            } else
                getNavigator()?.onError(getNavigator()?.getMessage(R.string.no_internet)!!)
        } catch (ex: Exception) {
            when (ex) {
                is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.network_failure)!!)
                else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
            }
        }
    }

    private fun handleGetStateListResponse(response: Response<AddressResponseModel>): ApiResponse<AddressResponseModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return ApiResponse.Success(resultResponse)
            }
        }
        return ApiResponse.Error(response.message())
    }


    private fun handleUpdateAddressResponse(response: Response<SendOtpResponseModel>): ApiResponse<SendOtpResponseModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return ApiResponse.Success(resultResponse)
            }
        }
        return ApiResponse.Error(response.message())
    }

    fun setStatesName(stateName: String) {

    }

    fun getAddressViaLocation() {

    }

    fun updateAddress(resultData: Bundle) {
        getNavigator()?.updateUI(resultData)

    }


}