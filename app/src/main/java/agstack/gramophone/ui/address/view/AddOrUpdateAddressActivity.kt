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
import agstack.gramophone.ui.profile.model.UserAddress
import agstack.gramophone.utils.Constants
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_add_or_update_address.*


@AndroidEntryPoint
class AddOrUpdateAddressActivity :
    BaseActivityWrapper<ActivityAddOrUpdateAddressBinding, AddressNavigator, AddOrUpdateAddressViewModel>(),
    AddressNavigator {

    private var addressReceivedfromBundle: UserAddress? = null
    private val addOrUpdateAddressViewModel: AddOrUpdateAddressViewModel by viewModels()
    var stateListIntentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data?.hasExtra(Constants.STATE_NAME) == true) {
                    if (!(addressReceivedfromBundle!=null && addressReceivedfromBundle!!.state.equals(data?.getStringExtra(Constants.STATE_NAME) as String))){
                        addOrUpdateAddressViewModel.districtName.set("")
//                        addOrUpdateAddressViewModel.tehsilName.set("")
//                        addOrUpdateAddressViewModel.villageName.set("")
//                        addOrUpdateAddressViewModel.pinCode.set("")
                        addOrUpdateAddressViewModel.address.set("")

                        addOrUpdateAddressViewModel.setStatesName(
                            data?.getStringExtra(Constants.STATE_NAME) as String,
                            null
                        )
                        addOrUpdateAddressViewModel.getDistrict(
                            "district",
                            data?.getStringExtra(Constants.STATE_NAME) as String,
                            "",
                            ""
                        )
                    }
                }

            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent?.extras?.containsKey(Constants.STATE) == true) {
            addOrUpdateAddressViewModel.setStatesName(
                intent?.extras?.get(Constants.STATE) as String,
                intent?.extras?.get(Constants.STATE_IMAGE_URL) as String
            )
        }
        watchSpinners()

        addOrUpdateAddressViewModel.checkPermissionAndUpdateUi()
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


    fun watchSpinners(){
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

                if (TextUtils.isEmpty(s)){
                    addOrUpdateAddressViewModel.getTehsil(
                        "tehsil",
                        addOrUpdateAddressViewModel.stateNameStr.get()!!,
                        addOrUpdateAddressViewModel.districtName.get()!!,
                        ""
                    )
                }
            }
        })
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

                if (TextUtils.isEmpty(s)){
                    addOrUpdateAddressViewModel.getVillage(
                        "village",
                        addOrUpdateAddressViewModel.stateNameStr.get()!!,
                        addOrUpdateAddressViewModel.districtName.get()!!,
                        addOrUpdateAddressViewModel.tehsilName.get()!!,
                        ""
                        )
                }
            }
        })
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

                if (TextUtils.isEmpty(s)){
                    addOrUpdateAddressViewModel.getPinCode(
                        "pincode",
                        addOrUpdateAddressViewModel.stateNameStr.get()!!,
                        addOrUpdateAddressViewModel.districtName.get()!!,
                        addOrUpdateAddressViewModel.tehsilName.get()!!,
                        addOrUpdateAddressViewModel.villageName.get()!!,

                        )
                }
            }
        })

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
    }

    override fun setStateImage(imageUrl: String) {
        Glide.with(this).load(imageUrl).into(ivStateImage)
    }

    override fun requestForLocation() {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)
    }

    override fun updateUi() {

        if (intent?.extras?.containsKey(Constants.FROM_EDIT_PROFILE) == true) {

            viewDataBinding.ivBack.visibility = View.VISIBLE
            viewDataBinding.saveBtn.visibility=View.VISIBLE
            if(intent?.extras?.containsKey(Constants.ADDRESSOBJECT)==true){
                 addressReceivedfromBundle =
                    intent?.getParcelableExtra<agstack.gramophone.ui.profile.model.UserAddress>(
                        Constants.ADDRESSOBJECT
                    )
                mViewModel?.setAddressdata(addressReceivedfromBundle!!)
            }


        } else {

            viewDataBinding.ivBack.visibility = View.GONE
            viewDataBinding.saveBtn.visibility = View.GONE
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

//            if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) == true) {
//
//            } else {
//                requestForLocation()
//            }


        }
    }

    override fun openStateList() {
        if (intent?.extras?.containsKey(Constants.FROM_EDIT_PROFILE) != true) {
            openAndFinishActivity(StateListActivity::class.java, Bundle().apply {
                putString(Constants.CHANGE_STATE, Constants.CHANGE_STATE)
            })
        } else {
            val stateIntent = Intent(this, SelectOtherStateActivity::class.java)
            stateListIntentLauncher.launch(stateIntent)
        }


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

    }

    override fun onBackPressClick() {
        super.onBackPressed()
    }


}
