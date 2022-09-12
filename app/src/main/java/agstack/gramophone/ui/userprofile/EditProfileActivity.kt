package agstack.gramophone.ui.userprofile

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.EditProfileActivityBinding
import agstack.gramophone.ui.profile.model.GpApiResponseProfileData
import agstack.gramophone.ui.userprofile.verifyotp.VerifyOTPDialogFragment
import agstack.gramophone.utils.Constants
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileActivity :
    BaseActivityWrapper<EditProfileActivityBinding, EditProfileNavigator, EditProfileViewModel>(),
    EditProfileNavigator {

    var userData: GpApiResponseProfileData? = null
    private val editProfileViewModel: EditProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolBar(true, resources.getString(R.string.editprofile), R.drawable.ic_arrow_left)
        getBundleData()
    }

    private fun getBundleData() {

        userData =
            intent.extras?.getParcelable<GpApiResponseProfileData>(Constants.USER_PROFILE_DATA)
        if (userData != null) {
            mViewModel?.setUserData(userData!!)

        }
    }

    override fun getLayoutID(): Int {
        return R.layout.edit_profile_activity
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): EditProfileViewModel {
        return editProfileViewModel
    }

    override fun showVerifyOTPFragment(otp_reference_id:Int) {
        VerifyOTPDialogFragment.newInstance(viewDataBinding.etMobile.text.toString(),otp_reference_id)
            .show(supportFragmentManager, VerifyOTPDialogFragment.TAG)
    }

}