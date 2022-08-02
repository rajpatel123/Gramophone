package agstack.gramophone.ui.address.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityAddOrUpdateAddressBinding
import agstack.gramophone.di.GetAddressIntentService
import agstack.gramophone.ui.address.AddressNavigator
import agstack.gramophone.ui.address.adapter.AddressDataListAdapter
import agstack.gramophone.ui.address.model.AddressDataModel
import agstack.gramophone.ui.address.viewmodel.AddOrUpdateAddressViewModel
import agstack.gramophone.ui.home.view.HomeActivity
import agstack.gramophone.utils.Constants
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_add_or_update_address.*
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class AddOrUpdateAddressActivity :
    BaseActivityWrapper<ActivityAddOrUpdateAddressBinding, AddressNavigator, AddOrUpdateAddressViewModel>(),
    AddressNavigator {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private val addOrUpdateAddressViewModel: AddOrUpdateAddressViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        if (isLocationEnabled()) {
            fusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                val location: Location? = task.result
                if (location != null) {
                    val geocoder = Geocoder(this, Locale.getDefault())
                    val list: List<Address> =
                        geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    addOrUpdateAddressViewModel.updateAddress(list[0])
                }
            }
        } else {
            showToast(getMessage(R.string.turn_on_location))
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }

    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
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


    //Can be improved
    override fun updateDistrict(adapter: AddressDataListAdapter, onSelect: (AddressDataModel) -> Unit) {
        adapter.selectedItem = onSelect
        districtSpinner.setAdapter(adapter)
        districtSpinner.setOnClickListener {
            districtSpinner.threshold = 1
        }
    }

    override fun updateTehsil(adapter: AddressDataListAdapter, onSelect: (AddressDataModel) -> Unit) {
        adapter.selectedItem = onSelect
        tehsilSpinner.setAdapter(adapter)
        tehsilSpinner.setOnClickListener {
            tehsilSpinner.threshold = 1
        }
    }

    override fun updateVillage(adapter: AddressDataListAdapter, onSelect: (AddressDataModel) -> Unit) {
        adapter.selectedItem = onSelect
        villageNameSpinner.setAdapter(adapter)
        villageNameSpinner.setOnClickListener {
            villageNameSpinner.threshold = 1
        }
    }

    override fun updatePinCode(adapter: AddressDataListAdapter, onSelect: (AddressDataModel) -> Unit) {
        adapter.selectedItem = onSelect
        pincodeSpinner.setAdapter(adapter)
        pincodeSpinner.setOnClickListener {
            pincodeSpinner.threshold = 1
        }
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

    override fun getAdapter(dataList: ArrayList<AddressDataModel>): AddressDataListAdapter {
        return AddressDataListAdapter(this, R.layout.item_address_data_name, dataList)
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

    override fun onError(message: String?) {
        Toast.makeText(this@AddOrUpdateAddressActivity, message, Toast.LENGTH_SHORT).show()

    }
    private fun showResults(currentAdd: String) {
        Toast.makeText(this@AddOrUpdateAddressActivity, currentAdd, Toast.LENGTH_SHORT).show()

    }

    override fun onResume() {
        super.onResume()
        if (intent?.extras?.containsKey(Constants.STATE) == true) {
            addOrUpdateAddressViewModel.setStatesName(intent?.extras?.get(Constants.STATE) as String, intent?.extras?.get(Constants.STATE_IMAGE_URL) as String)
            addOrUpdateAddressViewModel.getDistrict(
                "district",
                intent?.extras?.get(Constants.STATE) as String,
                "",
                ""
            )
        } else {
            addOrUpdateAddressViewModel.getAddressViaLocation()
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            startLocationUpdates()

        }
    }

}
