package agstack.gramophone.ui.address.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.address.AddressNavigator
import agstack.gramophone.ui.address.model.*
import agstack.gramophone.ui.login.model.SendOtpResponseModel
import agstack.gramophone.utils.ApiResponse
import agstack.gramophone.utils.Constants
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
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
    val addressDataModel: MutableLiveData<ApiResponse<List<AddressDataModel>>> =
        MutableLiveData()
    var state: State? = null
    var stateNameStr = ObservableField<String>()
    var districtName = ObservableField<String>()

    //var districtName: String? = ""
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

    fun changeState(){
        getNavigator()?.changeState()
    }
    fun submitAddress() {

        if (TextUtils.isEmpty(districtName.get())) {
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

        val updateAddressRequestModel = UpdateAddressRequestModel(
            address,
            districtName.get(),
            pinCode,
            getNavigator()?.getState(),
            tehsilName,
            villageName
        )
        viewModelScope.launch {
            updateCompleteAddress(updateAddressRequestModel)
        }

    }

    suspend fun updateCompleteAddress(updateAddressRequestModel: UpdateAddressRequestModel) {
        getNavigator()?.onLoading()
        try {
            if (getNavigator()?.isNetworkAvailable() == true) {
                val response = onBoardingRepository.updateAddress(updateAddressRequestModel)

                val updateAddress = handleUpdateAddressResponse(response).data

                if (Constants.GP_API_STATUS.equals(updateAddress?.gp_api_status)) {
                    getNavigator()?.onSuccess(updateAddress?.gp_api_message!!)
                    getNavigator()?.goToApp()
                }else{
                    getNavigator()?.onError(updateAddress?.gp_api_message)

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

    fun getDistrict(type: String, state: String, district: String, tehsil: String) {
        viewModelScope.launch {
            getAddressDataList(type, state, district, tehsil, "")
        }
    }

    fun getTehsil(type: String, state: String, district: String, tehsil: String) {
        viewModelScope.launch {
            getAddressDataList(type, state, district, tehsil, "")
        }
    }

    fun getVillage(type: String, state: String, district: String, tehsil: String,village: String) {
        viewModelScope.launch {
            getAddressDataList(type, state, district, tehsil,village)
        }
    }

    fun getPinCode(type: String, state: String, district: String, tehsil: String,village: String) {
        viewModelScope.launch {
            getAddressDataList(type, state, district, tehsil,village)
        }
    }

    private suspend fun getAddressDataList(
        type: String,
        state: String,
        district: String,
        tehsil: String,
        village: String
    ) {
        getNavigator()?.onLoading()
        try {
            if (getNavigator()?.isNetworkAvailable() == true) {
                val response = onBoardingRepository.getAddressDataByType(
                    type,
                    AddressRequestModel("", district, "", state, tehsil, village)
                )

                val stateListData = handleGetStateListResponse(response).data

                if (Constants.GP_API_STATUS.equals(stateListData?.gp_api_status)) {
                    val dataList1 = ArrayList<AddressDataModel>()
                    val dataList = ArrayList<String>()

                    when (type) {
                        "district" -> {

                            stateListData?.gp_api_response_data?.district_list?.forEach {
                                val addressDataModel= AddressDataModel(it.name.toString())
                                dataList1.add(addressDataModel)
                            }
                            val adapter = getNavigator()?.getAdapter(dataList1)!!
                            getNavigator()?.updateDistrict(adapter){
                                districtName.set(it.name)
                                getNavigator()?.closeDropDown()
                            }
                        }

                        "tehsil" -> {
                            dataList.add("Select")
                            stateListData?.gp_api_response_data?.tehsil_list?.forEach {
                                dataList.add(it.name.toString())
                            }
                            getNavigator()?.updateTehsil(dataList) {
                            }
                        }
                        "village" -> {
                            dataList.add("Select")
                            stateListData?.gp_api_response_data?.village_list?.forEach {
                                dataList.add(it.name.toString())
                            }
                            getNavigator()?.updateVillage(dataList) {
                            }
                        }

                        "pincode" -> {
                            dataList.add("Select")
                            stateListData?.gp_api_response_data?.pincode_list?.forEach {
                                dataList.add(it.pincode.toString())
                            }
                            getNavigator()?.updatePinCode(dataList) {
                            }
                        }
                    }

                }else{
                    getNavigator()?.onError(stateListData?.gp_api_message)

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

    private fun handleGetStateListResponse(response: Response<StateResponseModel>): ApiResponse<StateResponseModel> {
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