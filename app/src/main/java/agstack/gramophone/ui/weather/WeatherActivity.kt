package agstack.gramophone.ui.weather


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityWeatherBinding
import agstack.gramophone.di.GPSTracker
import agstack.gramophone.ui.dialog.LocationAccessDialog
import agstack.gramophone.utils.Constants
import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherActivity :
    BaseActivityWrapper<ActivityWeatherBinding, WeatherNavigator, WeatherViewModel>(),
    WeatherNavigator, View.OnClickListener {

    //initialise ViewModel
    private val weatherViewModel: WeatherViewModel by viewModels()
    private var gpsTracker: GPSTracker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
    }

    private fun setupUi() {
        viewDataBinding.tvChangeLoc.setOnClickListener(this)
        weatherViewModel.getWeatherData()
        weatherViewModel.getWeatherDetail()
        weatherViewModel.getWeatherDetailHourly()
        weatherViewModel.getWeatherDetailDayWise()
        gpsTracker = GPSTracker(this@WeatherActivity)
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
                val locationAccessDialog = LocationAccessDialog() {
                    if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        weatherViewModel.getLatitudeLongitude(gpsTracker)
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

    override fun getLayoutID(): Int {
        return R.layout.activity_weather
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): WeatherViewModel {
        return weatherViewModel
    }

}