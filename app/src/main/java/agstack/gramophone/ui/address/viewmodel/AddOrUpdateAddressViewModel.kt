package agstack.gramophone.ui.address.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.address.AddressNavigator
import agstack.gramophone.ui.address.model.*
import agstack.gramophone.ui.address.model.addressdetails.AddressDataByLatLongResponseModel
import agstack.gramophone.ui.address.model.addressdetails.AddressRequestWithLatLongModel
import agstack.gramophone.ui.address.model.addressdetails.GpApiResponseData
import agstack.gramophone.ui.address.view.StateListActivity
import agstack.gramophone.ui.login.model.SendOtpResponseModel
import agstack.gramophone.utils.ApiResponse
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.Constants.DISTRICT
import agstack.gramophone.utils.Constants.PINCODE
import agstack.gramophone.utils.Constants.TEHSIL
import agstack.gramophone.utils.Constants.VILLAGE
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.location.Address
import android.os.Bundle
import android.text.TextUtils
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
    var stateNameInitial = ObservableField<String>()
    var stateImageUrl = ObservableField<String>()
    var isImageAvailable = ObservableField<Boolean>()
    var districtName = ObservableField<String>()
    var tehsilName = ObservableField<String>()
    var villageName = ObservableField<String>()
    var pinCode = ObservableField<String>()
    var address = ObservableField<String>()
    var loading = ObservableField<Boolean>()


    fun changeState() {
        getNavigator()?.openAndFinishActivity(StateListActivity::class.java, Bundle().apply {
            putString(Constants.CHANGE_STATE,Constants.CHANGE_STATE)
        })
    }

    fun submitAddress() {

        if (TextUtils.isEmpty(districtName.get())) {
            getNavigator()?.onError(getNavigator()?.getMessage(R.string.district_required))
            return
        }

        if (TextUtils.isEmpty(tehsilName.get())) {
            getNavigator()?.onError(getNavigator()?.getMessage(R.string.tehsil_required))
            return
        }

        val updateAddressRequestModel = UpdateAddressRequestModel(
            address.get(),
            districtName.get(),
            pinCode.get(),
            getNavigator()?.getState(),
            tehsilName.get(),
            villageName.get()
        )
        viewModelScope.launch {
            updateCompleteAddress(updateAddressRequestModel)
        }

    }

    suspend fun updateCompleteAddress(updateAddressRequestModel: UpdateAddressRequestModel) {
        loading.set(true)
        try {
            if (getNavigator()?.isNetworkAvailable() == true) {
                val response = onBoardingRepository.updateAddress(updateAddressRequestModel)

                val updateAddress = handleUpdateAddressResponse(response).data
                loading.set(false)

                if (Constants.GP_API_STATUS.equals(updateAddress?.gp_api_status)) {
                    getNavigator()?.onSuccess(updateAddress?.gp_api_message!!)
                    SharedPreferencesHelper.instance?.putBoolean(
                        SharedPreferencesKeys.logged_in,
                        true
                    )
                    getNavigator()?.goToApp()
                } else {
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

    fun getVillage(type: String, state: String, district: String, tehsil: String, village: String) {
        viewModelScope.launch {
            getAddressDataList(type, state, district, tehsil, village)
        }
    }

    fun getPinCode(type: String, state: String, district: String, tehsil: String, village: String) {
        viewModelScope.launch {
            getAddressDataList(type, state, district, tehsil, village)
        }
    }

    private suspend fun getAddressDataList(
        type: String,
        state: String,
        district: String,
        tehsil: String,
        village: String
    ) {
        loading.set(true)
        try {
            if (getNavigator()?.isNetworkAvailable() == true) {
                val response = onBoardingRepository.getAddressDataByType(
                    type,
                    AddressRequestModel("", district, "", state, tehsil, village)
                )

                val stateListData = handleGetStateListResponse(response).data

                if (Constants.GP_API_STATUS.equals(stateListData?.gp_api_status)) {
                    val dataList = ArrayList<AddressDataModel>()

                    when (type) {
                        DISTRICT -> {
                            stateListData?.gp_api_response_data?.district_list?.forEach {
                                val addressDataModel = AddressDataModel(it.name.toString())
                                dataList.add(addressDataModel)
                            }
                            val adapter = getNavigator()?.getAdapter(dataList)!!
                            getNavigator()?.updateDistrict(adapter) {
                                districtName.set(it.name)
                                getNavigator()?.closeDistrictDropDown()
                                getTehsil(
                                    TEHSIL,
                                    stateNameStr.get().toString(),
                                    districtName.get().toString(),
                                    ""
                                )
                            }
                        }

                        TEHSIL -> {
                            stateListData?.gp_api_response_data?.tehsil_list?.forEach {
                                val addressDataModel = AddressDataModel(it.name.toString())
                                dataList.add(addressDataModel)
                            }
                            val adapter = getNavigator()?.getAdapter(dataList)!!
                            getNavigator()?.updateTehsil(adapter) {
                                tehsilName.set(it.name)
                                getNavigator()?.closeTehsilDropDown()
                                getVillage(
                                    VILLAGE,
                                    stateNameStr.get().toString(),
                                    districtName.get().toString(),
                                    tehsilName.get().toString(),
                                    ""
                                )
                            }
                        }
                        VILLAGE -> {
                            stateListData?.gp_api_response_data?.village_list?.forEach {
                                val addressDataModel = AddressDataModel(it.name.toString())
                                dataList.add(addressDataModel)
                            }
                            val adapter = getNavigator()?.getAdapter(dataList)!!
                            getNavigator()?.updateVillage(adapter) {
                                villageName.set(it.name)
                                getNavigator()?.closeVillageDropDown()
                                getPinCode(
                                    PINCODE,
                                    stateNameStr.get().toString(),
                                    districtName.get().toString(),
                                    tehsilName.get().toString(),
                                    villageName.get().toString()
                                )
                            }
                        }

                        PINCODE -> {
                            stateListData?.gp_api_response_data?.pincode_list?.forEach {
                                val addressDataModel = AddressDataModel(it.pincode.toString())
                                dataList.add(addressDataModel)
                            }
                            val adapter = getNavigator()?.getAdapter(dataList)!!
                            getNavigator()?.updatePinCode(adapter) {
                                pinCode.set(it.name)
                                getNavigator()?.closePincodeDropDown()
                            }
                        }
                    }

                    loading.set(false)

                } else {
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

    fun setStatesName(stateName: String, image: String) {
        stateNameStr.set(stateName)
        stateImageUrl.set(image)
        if (image.isNullOrEmpty()){
            isImageAvailable.set(false)
            stateNameInitial.set(stateName.subSequence(0,1).toString())
        }else{
            isImageAvailable.set(true)

        }
    }


    fun getAddressByLocation() {

        val gps = getNavigator()?.getGPSTracker()
        if (gps?.canGetLocation() == true) {
            val latitude: Double = gps.getLatitude()
            val longitude: Double = gps.getLongitude()
            val addressRequestModel =
                AddressRequestWithLatLongModel(latitude.toString(), longitude.toString())
            getAddressByLatLong(addressRequestModel = addressRequestModel)
        } else {
            // Can't get location.
            // GPS or network is not enabled.
            // Ask user to enable GPS/network in settings.
            gps?.showSettingsAlert()
        }
    }


    private fun getAddressByLatLong(addressRequestModel: AddressRequestWithLatLongModel) {

        loading.set(true)
        try {
            if (getNavigator()?.isNetworkAvailable() == true) {
                viewModelScope.launch {
                    val addressResponse =
                        onBoardingRepository.updateAddressByLatLong(addressRequestModel)

                    val updateAddress = handleResponse(addressResponse).data
                    loading.set(false)

                    if (Constants.GP_API_STATUS.equals(updateAddress?.gp_api_status)) {
                        updateAddressByLatLong(updateAddress?.gp_api_response_data)
                    } else {
                        getNavigator()?.onError(updateAddress?.gp_api_message)
                    }

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

    private fun updateAddressByLatLong(address: GpApiResponseData?) {
        if (address?.state != null) stateNameStr.set(address.state)
        if (address?.district != null) districtName.set(address.district)
        if (address?.tehsil != null) tehsilName.set(address.tehsil)
        if (address?.village != null) villageName.set(address.village)

        isImageAvailable.set(false)
        stateNameInitial.set(stateNameStr.get()?.subSequence(0,1).toString())

        if (address?.pincode_list != null && address.pincode_list.size > 0)
            pinCode.set(address.pincode_list.get(0).pincode)
    }


    private fun handleResponse(response: Response<AddressDataByLatLongResponseModel>): ApiResponse<AddressDataByLatLongResponseModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return ApiResponse.Success(resultResponse)
            }
        }
        return ApiResponse.Error(response.message())
    }

}