package agstack.gramophone.ui.userprofile

import agstack.gramophone.BR
import agstack.gramophone.BuildConfig
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.EditProfileActivityBinding
import agstack.gramophone.ui.profile.model.GpApiResponseProfileData
import agstack.gramophone.ui.userprofile.verifyotp.VerifyOTPDialogFragment
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
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


    override fun showVerifyOTPFragment(otp_reference_id: Int, onUpdateSuccess: (String) -> Unit) {
        val newInstance = VerifyOTPDialogFragment.newInstance(
            viewDataBinding.etMobile.text.toString(),
            otp_reference_id
        )
        newInstance.setOnSuccessListener { onUpdateSuccess }

        newInstance
            .show(supportFragmentManager, VerifyOTPDialogFragment.TAG)
    }


    override fun finishActivity() {
        finish()
    }

    override fun sendEditProfileMoEngageEvent() {
        val properties = Properties()
        properties.addAttribute("Profile ID",
                SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.CUSTOMER_ID)!!)
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(this, "KA_Edit Profile", properties)
    }
}