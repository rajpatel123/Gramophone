package agstack.gramophone.ui.address.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivitySelectOtherStateAcitivyBinding
import agstack.gramophone.ui.address.OtherStateNavigator
import agstack.gramophone.ui.address.adapter.OtherStateListAdapter
import agstack.gramophone.ui.address.model.GpApiResponseData
import agstack.gramophone.ui.address.model.State
import agstack.gramophone.ui.address.viewmodel.OtherStateViewModel
import agstack.gramophone.utils.Constants
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_state_list.*

@AndroidEntryPoint
class SelectOtherStateActivity :
    BaseActivityWrapper<ActivitySelectOtherStateAcitivyBinding, OtherStateNavigator, OtherStateViewModel>(),
    OtherStateNavigator {

    private val otherStateViewModel: OtherStateViewModel by viewModels()

    private lateinit var stateList: GpApiResponseData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.extras?.containsKey(Constants.STATE_LIST) == true){
            stateList = intent.extras?.get(Constants.STATE_LIST) as GpApiResponseData
            otherStateViewModel.loadList(stateList)
        }else{
            otherStateViewModel.getStateList()
        }

    }

    override fun getLayoutID(): Int {
        return R.layout.activity_select_other_state_acitivy
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): OtherStateViewModel {
        return otherStateViewModel
    }

    override fun updateStateList(
        stateListAdapter: OtherStateListAdapter,
        onStateSelected: (State) -> Unit
    ) {
        stateListAdapter.selectedState = onStateSelected
        rvStates.adapter = stateListAdapter
    }

    override fun onStateSelected(name: String?) {
        val data = Intent()
        data.putExtra(Constants.STATE_NAME, name)
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    override fun closeStateList() {
        finish()
    }


}