package agstack.gramophone.ui.address.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityAddOrUpdateAddressBinding
import agstack.gramophone.di.GetAddressIntentService
import agstack.gramophone.ui.address.AddressNavigator
import agstack.gramophone.ui.address.viewmodel.AddOrUpdateAddressViewModel
import agstack.gramophone.ui.home.view.HomeActivity
import agstack.gramophone.utils.Constants
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_add_or_update_address.*

@AndroidEntryPoint
class AddOrUpdateAddressActivity :
    BaseActivityWrapper<ActivityAddOrUpdateAddressBinding, AddressNavigator, AddOrUpdateAddressViewModel>(),
    AddressNavigator {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationPermissionCode = 2
    private lateinit var addressResultReceiver: LocationAddressResultReceiver
    private lateinit var currentLocation: Location
    private lateinit var locationCallback: LocationCallback
    private val addOrUpdateAddressViewModel: AddOrUpdateAddressViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent?.extras?.containsKey(Constants.STATE_NAME) == true) {
            addOrUpdateAddressViewModel.setStatesName(intent?.extras?.get(Constants.STATE_NAME) as String)
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

    override fun updateDistrict(district: ArrayList<String>, function: () -> Unit) {
        //val niceSpinner = findViewById<View>(R.id.spDistrict) as NiceSpinner
        //val dataset: List<String> = LinkedList(Arrays.asList(district))
        //niceSpinner.attachDataSource(dataset)
    }

    override fun updateUI(resultData: Bundle) {
        etStateName.setText(resultData.getString("State"))
        etDistrictname.setText(resultData?.getString("District"))
        etTahseelName.setText(resultData?.getString("Tehsil"))
        etAddress.setText(resultData?.getString("Address"))
        etPinCode.setText(resultData?.getString("Postal"))
    }

    override fun goToApp() {
        openAndFinishActivity(HomeActivity::class.java,null)
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
        startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}
