package agstack.gramophone.ui.userprofile.firm

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.AddFirmActivityBinding
import agstack.gramophone.databinding.EditProfileActivityBinding
import agstack.gramophone.ui.profile.model.GpApiResponseProfileData
import agstack.gramophone.utils.Constants
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddFirmActivity :
    BaseActivityWrapper<AddFirmActivityBinding, AddFirmNavigator, AddFirmViewModel>(),
    AddFirmNavigator {

    private val editProfileViewModel: AddFirmViewModel by viewModels()
    var userData: GpApiResponseProfileData? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolBar(true, resources.getString(R.string.add_firm), R.drawable.ic_arrow_left)
        getBundleData()
    }

    private fun getBundleData() {

        userData= intent.extras?.getParcelable<GpApiResponseProfileData>(Constants.USER_PROFILE_DATA)
        if(userData!=null){
            mViewModel?.setUserData(userData!!)

        }
    }
    override fun getLayoutID(): Int {
        return R.layout.add_firm_activity
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): AddFirmViewModel {
        return editProfileViewModel
    }


}