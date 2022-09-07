package agstack.gramophone.ui.address.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.address.AddressNavigator
import agstack.gramophone.ui.address.model.*
import agstack.gramophone.ui.address.model.addressdetails.AddressRequestWithLatLongModel
import agstack.gramophone.ui.address.view.StateListActivity
import agstack.gramophone.ui.login.model.SendOtpResponseModel
import agstack.gramophone.utils.ApiResponse
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.Constants.ALL_STRING
import agstack.gramophone.utils.Constants.DISTRICT
import agstack.gramophone.utils.Constants.PINCODE
import agstack.gramophone.utils.Constants.TEHSIL
import agstack.gramophone.utils.Constants.VILLAGE
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
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
            putString(Constants.CHANGE_STATE, Constants.CHANGE_STATE)
        })
    }

    fun onBackPressedClick() {
        getNavigator()?.onBackPressClick()
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
        if (image.isNullOrEmpty()) {
            isImageAvailable.set(false)
            stateNameInitial.set(stateName.subSequence(0, 1).toString())
        } else {
            isImageAvailable.set(true)
            stateImageUrl.set(image)

        }
    }


    fun getAddressByLocation() {

        val gps = getNavigator()?.getGPSTracker()
        if (gps?.canGetLocation() == true) {
            val latitude: Double = gps.getLatitude()
            val longitude: Double = gps.getLongitude()

            val geocoder: Geocoder
            val addresses: List<Address>


            addresses = getNavigator()?.getGeoCoder()?.getFromLocation(
                latitude,
                longitude,
                1
            )!! // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            var addressRequestModel:AddressRequestWithLatLongModel
            if (addresses != null && addresses.size > 0) {
                val city = addresses[0].locality
                val state = addresses[0].adminArea
                val postalCode = addresses[0].postalCode

                 addressRequestModel =
                    AddressRequestWithLatLongModel(
                        latitude.toString(),
                        longitude.toString(),
                        state,
                        city,
                        "",
                        "",
                        pincode = postalCode
                    )
                Log.d("AddressPayload", Gson().toJson(addressRequestModel))
                getAddressByLocationFromApi(latitude,longitude)
              //  getAddressByLatLng(addressRequestModel = addressRequestModel)
            }else{
                getAddressByLocationFromApi(latitude,longitude)
            }

        } else {
            // Can't get location.
            // GPS or network is not enabled.
            // Ask user to enable GPS/network in settings.
            gps?.showSettingsAlert()
        }
    }



    private fun getAddressByLocationFromApi(latitude: Double, longitude: Double) {
        try {
            if (getNavigator()?.isNetworkAvailable() == true) {
                viewModelScope.launch {

                    val addressResponse =
                        onBoardingRepository.getLocationAddress(
                            "" + latitude + "," + longitude,
                            "AIzaSyAgy7OYQrHaPSXndFDMjXU2pMcpk48uyt0"
                        )
                    if (addressResponse.isSuccessful) {
                        parseAddressDetail(latitude, longitude, ))
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

    private fun parseAddressDetail(latitude: Double, longitude: Double, locationObj: JSONObject?) {
        val addressData = HashMap<String, String>()
        val locationArray = locationObj?.getJSONArray("results")
        val item = locationArray?.getJSONObject(0)
        val address_components = item?.getJSONArray("address_components")

        for (j in 0 until address_components?.length()!!) {
            val addressObj = address_components.getJSONObject(j)
            val itemTypeArray = addressObj.getJSONArray("types")
            for (k in 0 until itemTypeArray.length()) {
                val itemType = itemTypeArray.optString(k)
                when (itemType) {
                    "sublocality_level_1" -> {
                        addressData.put("village", addressObj.optString("long_name"))
                    }
                    "postal_code" -> {
                        addressData.put("pincode", addressObj.optString("long_name"))
                    }

                    "locality" -> {
                        addressData.put("tehsil", addressObj.optString("long_name"))
                    }

                    "administrative_area_level_2" -> {
                        addressData.put("dist", addressObj.optString("long_name"))
                    }

                    "administrative_area_level_1" -> {
                        addressData.put("state", addressObj.optString("long_name"))
                    }
                }

            }
        }

        if (addressData.keys.size > 0) {
            var addressRequest = AddressRequestWithLatLongModel(
                latitude.toString(),
                longitude.toString(),
                addressData.get("state"),
                addressData.get("dist"),
                addressData.get("tehsil"),
                addressData.get("village"),
                addressData.get("pincode"),
            )

            getAddressByLatLng(addressRequest)
        }
    }


    private fun getAddressByLatLng(addressRequestModel: AddressRequestWithLatLongModel) {

        loading.set(true)
        try {
            if (getNavigator()?.isNetworkAvailable() == true) {
                viewModelScope.launch {
                    val addressResponse =
                        onBoardingRepository.getAddressByLatLong(addressRequestModel)

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

    private fun updateAddressByLatLong(address: agstack.gramophone.ui.address.model.GpApiResponseData?) {
        val dataList = ArrayList<AddressDataModel>()

        when (address?.type) {
            ALL_STRING -> {
                if (address?.state != null) stateNameStr.set(address.state)
                if (address?.district != null) districtName.set(address.district)
                if (address?.tehsil != null) tehsilName.set(address.tehsil)
                if (address?.village != null) villageName.set(address.village)
                if (address?.pincode != null) pinCode.set(address.pincode)

            }
            DISTRICT -> {
                if (address?.state != null) stateNameStr.set(address.state)

                address?.district_list?.forEach {
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
                if (address?.state != null) stateNameStr.set(address.state)
                if (address?.district != null) districtName.set(address.district)
                address?.tehsil_list?.forEach {
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
                if (address?.state != null) stateNameStr.set(address.state)
                if (address?.district != null) districtName.set(address.district)
                if (address?.tehsil != null) tehsilName.set(address.tehsil)
                address?.village_list?.forEach {
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
                if (address?.state != null) stateNameStr.set(address.state)
                if (address?.district != null) districtName.set(address.district)
                if (address?.tehsil != null) tehsilName.set(address.tehsil)
                if (address?.village != null) villageName.set(address.village)
                address?.pincode_list?.forEach {
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

        if (address?.state_top_list?.size!! >0){
            isImageAvailable.set(true)
            stateImageUrl.set(address?.state_top_list?.get(0).image)

        }else{
            isImageAvailable.set(false)
            stateNameInitial.set(stateNameStr.get()?.subSequence(0, 1).toString())
        }

    }


    private fun handleResponse(response: Response<StateResponseModel>): ApiResponse<StateResponseModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return ApiResponse.Success(resultResponse)
            }
        }
        return ApiResponse.Error(response.message())
    }





}