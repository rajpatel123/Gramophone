package agstack.gramophone.di

import android.app.IntentService
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.ResultReceiver
import android.util.Log
import java.util.*

class GetAddressIntentService : IntentService(IDENTIFIER) {
    private var addressResultReceiver: ResultReceiver? = null
    override fun onHandleIntent(intent: Intent?) {
        val msg: String
        addressResultReceiver = Objects.requireNonNull(intent)!!.getParcelableExtra("add_receiver")
        if (addressResultReceiver == null) {
            Log.e("GetAddressIntentService", "No receiver, not processing the request further")
            return
        }
        val location = intent!!.getParcelableExtra<Location>("add_location")
        if (location == null) {

           val bundle = Bundle()
           bundle.putString("msg", "No location, can't go further without location")

           sendResultsToReceiver(0, bundle)
            return
        }
        val geoCoder = Geocoder(this, Locale.getDefault())
        var addresses: List<Address>? = null
        try {
            addresses = geoCoder.getFromLocation(location.latitude, location.longitude, 1)
        } catch (ioException: Exception) {
            Log.e("", "Error in getting address for the location")
        }
        if (addresses == null || addresses.isEmpty()) {
            val bundle = Bundle()
            bundle.putString("msg", "No address found for the location")

            sendResultsToReceiver(1, bundle)
        } else {
            val address = addresses[0]
            val bundle = Bundle()
            bundle.putString("State", address.adminArea)
            bundle.putString("Tehsil", address.locality)
            bundle.putString("Postal", address.postalCode)
            bundle.putString("District", address.subAdminArea)
            val addressDetails = """
         ${address.getAddressLine(0)}
         """.trimIndent()
            bundle.putString("Address", addressDetails)


            sendResultsToReceiver(2, bundle)
        }
    }

    private fun sendResultsToReceiver(resultCode: Int, bundle: Bundle) {
        addressResultReceiver!!.send(resultCode, bundle)
    }

    companion object {
        private const val IDENTIFIER = "GetAddressIntentService"
    }
}