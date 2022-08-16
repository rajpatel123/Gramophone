package agstack.gramophone.di

import agstack.gramophone.utils.Constants.MIN_DISTANCE_CHANGE_FOR_UPDATES
import agstack.gramophone.utils.Constants.MIN_TIME_BW_UPDATES
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Service
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.util.Log


class GPSTracker(context: Context) : Service(), LocationListener {
    private val mContext: Context

    // Flag for GPS status
    var isGPSEnabled = false

    // Flag for network status
    var isNetworkEnabled = false

    // Flag for GPS status
    var canGetLocation = false
    private var location : Location? = null
    var latitude  = 0.0
    var longitude =0.0


    // Declaring a Location Manager
    protected var locationManager: LocationManager? = null
    @SuppressLint("MissingPermission")
    fun getLocation(): Location? {
        try {
            locationManager = mContext.getSystemService(LOCATION_SERVICE) as LocationManager?

            // Getting GPS status
            isGPSEnabled = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)!!

            // Getting network status
            isNetworkEnabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (!isGPSEnabled && !isNetworkEnabled) {
                // No network provider is enabled
            } else {
                canGetLocation = true
                if (isNetworkEnabled) {
                    locationManager!!.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this
                    )
                    Log.d("Network", "Network")
                    if (locationManager != null) {
                        location = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        if (location != null) {
                            latitude = location!!.getLatitude()
                            longitude = location!!.getLongitude()
                        }
                    }
                }
                // If GPS enabled, get latitude/longitude using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager!!.requestLocationUpdates( LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this
                        )
                        Log.d("GPS Enabled", "GPS Enabled")
                        if (locationManager != null) {
                            location = locationManager!!
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER)
                            if (location != null) {
                                latitude = location!!.getLatitude()
                                longitude = location!!.getLongitude()
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return location
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app.
     */
    fun stopUsingGPS() {
        locationManager?.removeUpdates(this@GPSTracker)
    }

    /**
     * Function to get latitude
     */
    @JvmName("getLatitude1")
    fun getLatitude(): Double {
        if (location != null) {
            latitude = location!!.getLatitude()
        }

        // return latitude
        return latitude
    }

    /**
     * Function to get longitude
     */
    @JvmName("getLongitude1")
    fun getLongitude(): Double {
        if (location != null) {
            longitude = location!!.getLongitude()
        }

        // return longitude
        return longitude
    }

    /**
     * Function to check GPS/Wi-Fi enabled
     * @return boolean
     */
    fun canGetLocation(): Boolean {
        return canGetLocation
    }

    /**
     * Function to show settings alert dialog.
     * On pressing the Settings button it will launch Settings Options.
     */
    fun showSettingsAlert() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(mContext)

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings")

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?")

        // On pressing the Settings button.
        alertDialog.setPositiveButton("Settings",
            DialogInterface.OnClickListener { dialog, which ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                mContext.startActivity(intent)
            })

        // On pressing the cancel button
        alertDialog.setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        // Showing Alert Message
        alertDialog.show()
    }

    init {
        mContext = context
        getLocation()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }
    override fun onLocationChanged(location: Location) {
        Log.d("Location",""+location.latitude)
    }
}