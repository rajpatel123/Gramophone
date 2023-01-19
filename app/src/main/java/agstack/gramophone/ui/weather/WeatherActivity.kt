package agstack.gramophone.ui.weather


import agstack.gramophone.BR
import agstack.gramophone.BuildConfig
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityWeatherBinding
import agstack.gramophone.di.GPSTracker
import agstack.gramophone.ui.dialog.LocationAccessDialog
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherActivity :
    BaseActivityWrapper<ActivityWeatherBinding, WeatherNavigator, WeatherViewModel>(),
    WeatherNavigator, View.OnClickListener {

    //initialise ViewModel
    private val weatherViewModel: WeatherViewModel by viewModels()
    private val VIEW_WEATHER = 0
    private val SHARE_ON_WHATSAPP = 1
    private val CHANGE_LOCATION = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
        if (TextUtils.isEmpty(SharedPreferencesHelper.instance?.getString(Constants.LATITUDE)
            )){
            if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                weatherViewModel.getLatitudeLongitude()
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    1)
            }
        }else{
          weatherViewModel.getWeatherData()
        }


    }

    private fun setupUi() {
        setToolbarTitle(getString(R.string.weather))
        viewDataBinding.tvChangeLoc.setOnClickListener(this)
        viewDataBinding.swipeRefresh.setColorSchemeResources(R.color.blue)
        viewDataBinding.swipeRefresh.setOnRefreshListener {
            weatherViewModel.getWeatherData()
            viewDataBinding.swipeRefresh.isRefreshing = false
        }
        sendWeatherMoEngageEvent(VIEW_WEATHER)
    }

    override fun setToolbarTitle(title: String) {
        setUpToolBar(true, title, R.drawable.ic_arrow_left)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_share, menu);
        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            if (item?.itemId == R.id.itemShare) {
                item.actionView?.setOnClickListener(this)
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.itemShare -> {
                sendWeatherMoEngageEvent(SHARE_ON_WHATSAPP)
                val whatsappIntent = Intent(Intent.ACTION_SEND)
                whatsappIntent.type = "text/plain"
                whatsappIntent.setPackage("com.whatsapp")
                whatsappIntent.putExtra(Intent.EXTRA_TEXT,
                    "Current temperature in your area " + weatherViewModel.currentTemp.value)
                try {
                    startActivity(whatsappIntent)
                } catch (ex: ActivityNotFoundException) {
                    showToast("Whatsapp have not been installed.")
                }
            }
            R.id.tvChangeLoc -> {
                sendWeatherMoEngageEvent(CHANGE_LOCATION)
                val locationAccessDialog = LocationAccessDialog() {
                    if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        weatherViewModel.getLatitudeLongitude()
                    } else {
                        ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            1)
                    }
                }
                locationAccessDialog.show(
                    supportFragmentManager,
                    Constants.LOCATION_ACCESS_DIALOG
                )
            }
        }
    }

    override fun setHourWiseForecastAdapter(hourWiseForecastAdapter: HourWiseForecastAdapter) {
        viewDataBinding.rvHoursForecast.adapter = hourWiseForecastAdapter
    }

    override fun setDayWiseForecastAdapter(dayWiseForecastAdapter: DayWiseForecastAdapter) {
        viewDataBinding.rvDayForecast.adapter = dayWiseForecastAdapter
    }

    override fun getGPSTracker(): GPSTracker = GPSTracker(this@WeatherActivity)

    override fun getLayoutID(): Int {
        return R.layout.activity_weather
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): WeatherViewModel {
        return weatherViewModel
    }

    private fun sendWeatherMoEngageEvent(eventType: Int) {
        val properties = Properties()
            .addAttribute("Profile ID",
                SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.CUSTOMER_ID)!!)
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()

        when (eventType) {
            0 -> {
                properties.addAttribute("Redirection_Source ID", "Home Screen")
                MoEAnalyticsHelper.trackEvent(this, "KA_View weather", properties)
            }
            1 -> {
                MoEAnalyticsHelper.trackEvent(this, "KA_share_on_whatsapp", properties)
            }
            else -> {
                MoEAnalyticsHelper.trackEvent(this, "KA_Change_Location", properties)
            }
        }
    }

    private fun sendWeatherMoEngageEvent() {
        val properties = Properties()
        properties.addAttribute("Redirection_Source ID", "Home Screen")
            .addAttribute("Profile ID",
                SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.CUSTOMER_ID)!!)
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(this, "KA_View weather", properties)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                weatherViewModel.getLatitudeLongitude()
            }
        }

    }

}