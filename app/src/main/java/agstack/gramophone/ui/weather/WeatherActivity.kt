package agstack.gramophone.ui.weather


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityWeatherBinding
import agstack.gramophone.ui.dialog.LocationAccessDialog
import agstack.gramophone.utils.Constants
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherActivity :
    BaseActivityWrapper<ActivityWeatherBinding, WeatherNavigator, WeatherViewModel>(),
    WeatherNavigator, View.OnClickListener, LocationAccessDialog.OkCancelListener {

    //initialise ViewModel
    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
    }

    private fun setupUi() {
        setUpToolBar(true, getString(R.string.weather), R.drawable.ic_arrow_left)
        viewDataBinding.tvChangeLoc.setOnClickListener(this)
        weatherViewModel.getWeatherData()
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

            }
            R.id.tvChangeLoc -> {
                val locationAccessDialog = LocationAccessDialog()
                locationAccessDialog.listener = this
                locationAccessDialog.show(
                    supportFragmentManager,
                    Constants.LOCATION_ACCESS_DIALOG
                )
            }
        }
    }

    override fun onGoToSettingClick() {
        weatherViewModel.onLocationClick()
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