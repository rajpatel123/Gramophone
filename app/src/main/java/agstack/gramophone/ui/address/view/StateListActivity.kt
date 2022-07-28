package agstack.gramophone.ui.address.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityStateListBinding
import agstack.gramophone.ui.address.StateNavigator
import agstack.gramophone.ui.address.adapter.StateListAdapter
import agstack.gramophone.ui.address.model.State
import agstack.gramophone.ui.address.viewmodel.StateSelectionViewModel
import agstack.gramophone.utils.Constants
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_state_list.*

@AndroidEntryPoint
class StateListActivity :
    BaseActivityWrapper<ActivityStateListBinding, StateNavigator, StateSelectionViewModel>(),
    StateNavigator {

    private val stateSelectionViewModel: StateSelectionViewModel by viewModels()


    var otherActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                stateSelectionViewModel.setStateSelection(data?.getStringExtra(Constants.STATE_NAME))
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)){
            openAndFinishActivity(AddOrUpdateAddressActivity::class.java,null)
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openAndFinishActivity(AddOrUpdateAddressActivity::class.java,null)
            }else{
                setupUI()
                fetchStateList()
            }


    }
    private fun fetchStateList() {
        stateSelectionViewModel.fetchStateList()
    }


    private fun setupUI() {
        rvStates.setHasFixedSize(true)
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_state_list
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): StateSelectionViewModel {
        return stateSelectionViewModel
    }


    override fun onLoading() {

    }

    override fun onSuccess(message: String?) {
        super.onSuccess(message)
    }

    override fun updateStateList(
        stateListAdapter: StateListAdapter,
        onStateSelected: (State) -> Unit
    ) {
        stateListAdapter.selectedState = onStateSelected
        rvStates.adapter = stateListAdapter
    }

    override fun moveToNext(bundle: Bundle) {
        openActivity(AddOrUpdateAddressActivity::class.java, bundle)
    }

    override fun selectOtherState(bundle: Bundle) {
        otherActivityLauncher.launch(Intent(this, SelectOtherStateActivity::class.java).apply {
            if (bundle != null)
                putExtras(bundle)
        })
    }

    override fun onRemoveSelection() {
        tvOthers.visibility = VISIBLE
        rlStateSelected.visibility = GONE
        tvSelectedState.text = ""
        stateSelectionViewModel.resetStateSelection()
    }

    override fun onStateSelected() {
        tvOthers.visibility = GONE
        rlStateSelected.visibility = VISIBLE

    }

    override fun onError(message: String?) {
        Toast.makeText(this@StateListActivity, message, Toast.LENGTH_LONG).show()
    }


}