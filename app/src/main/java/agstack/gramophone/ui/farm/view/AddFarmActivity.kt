package agstack.gramophone.ui.farm.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.AddFarmActivityBinding
import agstack.gramophone.ui.farm.navigator.AddFarmNavigator
import agstack.gramophone.ui.farm.viewmodel.AddFarmViewModel
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class AddFarmActivity :
    BaseActivityWrapper<AddFarmActivityBinding, AddFarmNavigator, AddFarmViewModel>(),
    AddFarmNavigator {

    var myCalendar: Calendar = Calendar.getInstance()

    companion object {
        fun start(activity: AppCompatActivity) {
            activity.startActivity(Intent(activity, AddFarmActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolBar()
        if (getBundle() != null && getBundle()?.containsKey("selectedCrop")!!) {
            getViewModel().selectedCrop = getBundle()?.getParcelable("selectedCrop")
        }
        getViewModel().getFarmUnits()
    }

    fun getBundle(): Bundle? {
        return intent.extras
    }

    private fun setToolBar() {
        viewDataBinding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun getLayoutID(): Int {
        return R.layout.add_farm_activity
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): AddFarmViewModel {
        val viewModel: AddFarmViewModel by viewModels()
        return viewModel
    }

    override fun onFarmAdded() {
        showToast("you successfully added the farm")
        finish()
    }

    override fun setFarmUnitsAdapter(units: List<String>) {
        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, units)
        viewDataBinding.edtAreaUnit.adapter = adapter
    }

    override fun showDatePicker() {
        val onDateSetListener = OnDateSetListener { view, year, month, day ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, month)
                myCalendar.set(Calendar.DAY_OF_MONTH, day)
                updateLabel()
            }

        DatePickerDialog(this@AddFarmActivity,
            onDateSetListener,
            myCalendar[Calendar.YEAR],
            myCalendar[Calendar.MONTH],
            myCalendar[Calendar.DAY_OF_MONTH]
        ).show()
    }

    private fun updateLabel() {
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        viewDataBinding.txtDate.text = dateFormat.format(myCalendar.time)
    }

    override fun getDate() : String {
        return viewDataBinding.txtDate.text.toString()
    }

    override fun getArea() : Double{
        try {
            return viewDataBinding.edtArea.text.toString().toDouble()
        } catch (ex : NumberFormatException){

        }
        return 0.0
    }

    override fun getAreaUnit() : String {
        return  viewDataBinding.edtAreaUnit.selectedItem.toString()
    }
}


