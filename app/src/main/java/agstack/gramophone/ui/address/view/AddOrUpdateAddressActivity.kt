package agstack.gramophone.ui.address.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityAddOrUpdateAddressBinding
import agstack.gramophone.di.GPSTracker
import agstack.gramophone.ui.address.AddressNavigator
import agstack.gramophone.ui.address.adapter.AddressDataListAdapter
import agstack.gramophone.ui.address.model.AddressDataModel
import agstack.gramophone.ui.address.viewmodel.AddOrUpdateAddressViewModel
import agstack.gramophone.ui.home.view.HomeActivity
import agstack.gramophone.utils.Constants
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_add_or_update_address.*
import java.util.*


@AndroidEntryPoint
class AddOrUpdateAddressActivity :
    BaseActivityWrapper<ActivityAddOrUpdateAddressBinding, AddressNavigator, AddOrUpdateAddressViewModel>(),
    AddressNavigator {

    private val addOrUpdateAddressViewModel: AddOrUpdateAddressViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent?.extras?.containsKey(Constants.STATE) == true) {
            addOrUpdateAddressViewModel.setStatesName(
                intent?.extras?.get(Constants.STATE) as String,
                intent?.extras?.get(Constants.STATE_IMAGE_URL) as String
            )
        }
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
    override fun updateDistrict(
        adapter: AddressDataListAdapter,
        onSelect: (AddressDataModel) -> Unit
    ) {
        adapter.selectedItem = onSelect
        districtSpinner.setAdapter(adapter)
        districtSpinner.setOnClickListener {
            districtSpinner.threshold = 1
        }


            districtSpinner.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                }
                override fun afterTextChanged(s: Editable) {
                    districtSpinner.threshold = 1
                    addOrUpdateAddressViewModel.tehsilName.set("")
                    addOrUpdateAddressViewModel.villageName.set("")
                    addOrUpdateAddressViewModel.pinCode.set("")
                    tehsilSpinner.setAdapter(null)
                    villageNameSpinner.setAdapter(null)
                    pincodeSpinner.setAdapter(null)

                    if (TextUtils.isEmpty(s)){
                        addOrUpdateAddressViewModel.getDistrict(
                            "district",
                            addOrUpdateAddressViewModel.stateNameStr.get()!!,
                            "",
                            ""
                        )
                    }
                }
            })
    }

    override fun updateTehsil(
        adapter: AddressDataListAdapter,
        onSelect: (AddressDataModel) -> Unit
    ) {
        adapter.selectedItem = onSelect
        tehsilSpinner.setAdapter(adapter)
        tehsilSpinner.setOnClickListener {
            tehsilSpinner.threshold = 1
        }

        tehsilSpinner.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable) {
                tehsilSpinner.threshold = 1
                addOrUpdateAddressViewModel.villageName.set("")
                addOrUpdateAddressViewModel.pinCode.set("")
                villageNameSpinner.setAdapter(null)
                pincodeSpinner.setAdapter(null)
            }
        })
    }

    override fun updateVillage(
        adapter: AddressDataListAdapter,
        onSelect: (AddressDataModel) -> Unit
    ) {
        adapter.selectedItem = onSelect
        villageNameSpinner.setAdapter(adapter)
        villageNameSpinner.setOnClickListener {
            villageNameSpinner.threshold = 1
        }

        villageNameSpinner.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable) {
                villageNameSpinner.threshold = 1
                addOrUpdateAddressViewModel.pinCode.set("")
                pincodeSpinner.setAdapter(null)
            }
        })
    }

    override fun updatePinCode(
        adapter: AddressDataListAdapter,
        onSelect: (AddressDataModel) -> Unit
    ) {
        adapter.selectedItem = onSelect
        pincodeSpinner.setAdapter(adapter)
        pincodeSpinner.setOnClickListener {
            pincodeSpinner.threshold = 1
        }
        pincodeSpinner.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable) {
                pincodeSpinner.threshold = 1
            }
        })
    }


    override fun goToApp() {
        //add check here , if intent from Edit Profile then just finish this activity else

        if (intent?.extras?.containsKey(Constants.FROM_EDIT_PROFILE) == true) {
            finish()

        } else {
            openAndFinishActivity(HomeActivity::class.java, null)
        }
    }

    override fun getState(): String? = etStateName.text.toString()


    override fun getAdapter(dataList: ArrayList<AddressDataModel>): AddressDataListAdapter {
        return AddressDataListAdapter(this, R.layout.item_address_data_name, dataList)
    }

    override fun closeDistrictDropDown() {
        districtSpinner.threshold = 1000
        districtSpinner.dismissDropDown()
        districtSpinner.nextFocusDownId = R.id.tehsilSpinner
    }

    override fun closeTehsilDropDown() {
        tehsilSpinner.threshold = 1000
        tehsilSpinner.dismissDropDown()
        tehsilSpinner.nextFocusDownId = R.id.villageNameSpinner
    }

    override fun closeVillageDropDown() {
        villageNameSpinner.threshold = 1000
        villageNameSpinner.dismissDropDown()
        villageNameSpinner.nextFocusDownId = R.id.pincodeSpinner
    }

    override fun closePincodeDropDown() {
        pincodeSpinner.threshold = 1000
        pincodeSpinner.dismissDropDown()
        pincodeSpinner.nextFocusDownId = R.id.submitBtn
    }

    override fun getGPSTracker(): GPSTracker = GPSTracker(this@AddOrUpdateAddressActivity)

    override fun onError(message: String?) {
        Toast.makeText(this@AddOrUpdateAddressActivity, ""+message, Toast.LENGTH_SHORT).show()

    }

    private fun showResults(currentAdd: String) {
        Toast.makeText(this@AddOrUpdateAddressActivity, currentAdd, Toast.LENGTH_SHORT).show()

    }

    override fun onResume() {
        super.onResume()
        if (intent?.extras?.containsKey(Constants.STATE) == true) {
            addOrUpdateAddressViewModel.setStatesName(
                intent?.extras?.get(Constants.STATE) as String,
                intent?.extras?.get(Constants.STATE_IMAGE_URL) as String
            )
            addOrUpdateAddressViewModel.getDistrict(
                "district",
                intent?.extras?.get(Constants.STATE) as String,
                "",
                ""
            )
        } else {
            addOrUpdateAddressViewModel.getAddressByLocation()
        }

        if (intent?.extras?.containsKey(Constants.FROM_EDIT_PROFILE) == true) {
            viewDataBinding.ivBack.visibility = View.VISIBLE
            viewDataBinding.saveBtn.visibility=View.VISIBLE

        } else {
            viewDataBinding.ivBack.visibility = View.GONE
            viewDataBinding.saveBtn.visibility=View.GONE
        }
    }

    override fun onBackPressClick() {
        super.onBackPressed()
    }

}
