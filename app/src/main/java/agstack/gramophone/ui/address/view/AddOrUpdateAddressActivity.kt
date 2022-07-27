package agstack.gramophone.ui.address.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityAddOrUpdateAddressBinding
import agstack.gramophone.di.GetAddressIntentService
import agstack.gramophone.ui.address.AddressNavigator
import agstack.gramophone.ui.address.adapter.AddressDataAdapter
import agstack.gramophone.ui.address.adapter.CustomListAdapter
import agstack.gramophone.ui.address.model.AddressDataModel
import agstack.gramophone.ui.address.viewmodel.AddOrUpdateAddressViewModel
import agstack.gramophone.ui.home.view.HomeActivity
import agstack.gramophone.ui.verifyotp.view.VerifyOtpActivity
import agstack.gramophone.utils.ApiResponse
import agstack.gramophone.utils.Constants
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.ObservableField
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_add_or_update_address.*
import kotlinx.android.synthetic.main.activity_login.*

@AndroidEntryPoint
class AddOrUpdateAddressActivity :
    BaseActivityWrapper<ActivityAddOrUpdateAddressBinding, AddressNavigator, AddOrUpdateAddressViewModel>(),
    AddressNavigator {

    var stateNameStr = ObservableField<String>()
    var districtName: String? = ""
    var tehsilName: String? = ""
    var villageName: String? = ""
    var pinCode: String? = ""
    var address: String? = ""
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationPermissionCode = 2
    private lateinit var addressResultReceiver: LocationAddressResultReceiver
    private lateinit var currentLocation: Location
    private lateinit var locationCallback: LocationCallback
    private val addOrUpdateAddressViewModel: AddOrUpdateAddressViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent?.extras?.containsKey(Constants.STATE) == true) {
            addOrUpdateAddressViewModel.setStatesName(intent?.extras?.get(Constants.STATE) as String)
            etStateName.setText(intent?.extras?.get(Constants.STATE) as String)
            addOrUpdateAddressViewModel.stateNameStr.set(etStateName.text.toString())

            addOrUpdateAddressViewModel.getDistrict(
                "district",
                intent?.extras?.get(Constants.STATE) as String,
                "",
                ""
            )
        } else {
            addOrUpdateAddressViewModel.getAddressViaLocation()
            addressResultReceiver = LocationAddressResultReceiver(Handler())

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    currentLocation = locationResult.locations[0]
                    getAddress()
                }
            }
            startLocationUpdates()

        }

         setupObserver()
    }

    private fun setupObserver() {

        addOrUpdateAddressViewModel.addressDataModel.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is ApiResponse.Success -> {
                    districtSpinner.setAdapter(AddressDataAdapter(this, it.data!!))
                }
                is ApiResponse.Error -> {
                    progress.visibility = View.GONE
                    it.message?.let { message ->
                        // Toast.makeText(this@LoginActivity, message, Toast.LENGTH_LONG).show()
                        openActivity(VerifyOtpActivity::class.java,null)

                    }
                }
                is ApiResponse.Loading -> {
                    progress.visibility = View.VISIBLE
                }
            }
        })
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val locationRequest = LocationRequest()
//            locationRequest.interval = 2000
//            locationRequest.fastestInterval = 1000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)

    }

    override fun getLayoutID(): Int {
        return R.layout.activity_add_or_update_address
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): AddOrUpdateAddressViewModel {
        return addOrUpdateAddressViewModel

    }


    override fun updateDistrict(adapter: CustomListAdapter, onSelect: (AddressDataModel) -> Unit) {
        adapter.selectedItem = onSelect
        districtSpinner.setAdapter(adapter)
        districtSpinner.setOnClickListener {
            districtSpinner.threshold = 1
        }
    }

    override fun updateTehsil(adapter: CustomListAdapter, onSelect: (AddressDataModel) -> Unit) {
        adapter.selectedItem = onSelect
        tehsilSpinner.setAdapter(adapter)
        tehsilSpinner.setOnClickListener {
            tehsilSpinner.threshold = 1
        }
    }

    override fun updateVillage(adapter: CustomListAdapter, onSelect: (AddressDataModel) -> Unit) {
        adapter.selectedItem = onSelect
        villageNameSpinner.setAdapter(adapter)
        villageNameSpinner.setOnClickListener {
            villageNameSpinner.threshold = 1
        }
    }

    override fun updatePinCode(adapter: CustomListAdapter, onSelect: (AddressDataModel) -> Unit) {
        adapter.selectedItem = onSelect
        pincodeSpinner.setAdapter(adapter)
        pincodeSpinner.setOnClickListener {
            pincodeSpinner.threshold = 1
        }
    }

    override fun updateUI(resultData: Bundle) {
        etStateName.setText(resultData.getString("State"))
        districtSpinner.setText(resultData.getString("District"))
        tehsilSpinner.setText(resultData.getString("Tehsil"))
        villageNameSpinner.setText(resultData.getString("Tehsil"))
        etAddress.setText(resultData.getString("Address"))
        pincodeSpinner.setText(resultData.getString("Postal"))
    }

    override fun goToApp() {
        openAndFinishActivity(HomeActivity::class.java, null)
    }

    override fun getState(): String? {
        return etStateName.text.toString()
    }

    override fun changeState() {
        openAndFinishActivity(StateListActivity::class.java, null)
    }

    override fun getAdapter(dataList: ArrayList<AddressDataModel>): CustomListAdapter {
//        return AddressDataAdapter(this,dataList)
        return CustomListAdapter(this, R.layout.item_address_data_name, dataList)
    }

    override fun closeDistrictDropDown() {
        districtSpinner.threshold = 1000
        districtSpinner.dismissDropDown()
        districtSpinner.nextFocusDownId=R.id.tehsilSpinner
    }

    override fun closeTehsilDropDown() {
        tehsilSpinner.threshold = 1000
        tehsilSpinner.dismissDropDown()
        tehsilSpinner.nextFocusDownId=R.id.villageNameSpinner
    }

    override fun closeVillageDropDown() {
        villageNameSpinner.threshold = 1000
        villageNameSpinner.dismissDropDown()
        villageNameSpinner.nextFocusDownId=R.id.pincodeSpinner
    }

    override fun closePincodeDropDown() {
        pincodeSpinner.threshold = 1000
        pincodeSpinner.dismissDropDown()
        pincodeSpinner.nextFocusDownId=R.id.submitBtn
    }

    private fun getAddress() {
        if (!Geocoder.isPresent()) {
            Toast.makeText(
                this@AddOrUpdateAddressActivity,
                "Can't find current address, ",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val intent = Intent(this, GetAddressIntentService::class.java)
        intent.putExtra("add_receiver", addressResultReceiver)
        intent.putExtra("add_location", currentLocation)
        startService(intent)
    }

    private inner class LocationAddressResultReceiver(handler: Handler) : ResultReceiver(handler) {
        override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
            if (resultCode == 0) {
                Log.d("Address", "Location null retrying")
                getAddress()
            }
            if (resultCode == 1) {
                Toast.makeText(
                    this@AddOrUpdateAddressActivity,
                    "Address not found, ",
                    Toast.LENGTH_SHORT
                ).show()
            }
            addOrUpdateAddressViewModel.updateAddress(resultData)
        }
    }

    override fun onError(message: String?) {
        Toast.makeText(this@AddOrUpdateAddressActivity, message, Toast.LENGTH_SHORT).show()

    }
    private fun showResults(currentAdd: String) {
        Toast.makeText(this@AddOrUpdateAddressActivity, currentAdd, Toast.LENGTH_SHORT).show()

    }

    override fun onResume() {
        super.onResume()
        if (this::fusedLocationClient.isInitialized)
            startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        if (this::fusedLocationClient.isInitialized)
            fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    fun test(){
        val str = "AMAR"
        val arr = str.split("").toTypedArray()
        val reverse: ArrayList<String> = ArrayList()
        for (i in arr.size downTo 0) {
            reverse.add(arr[i])
        }

        System.out.println("Reverse String is " + reverse)
    }
}
