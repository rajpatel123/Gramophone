package agstack.gramophone.ui.settings.view

import agstack.gramophone.BR
import agstack.gramophone.BuildConfig
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityBlockedUsersBinding
import agstack.gramophone.ui.language.model.languagelist.Language
import agstack.gramophone.ui.settings.BlockedUsersAdapter
import agstack.gramophone.ui.settings.BlockedUsersNavigator
import agstack.gramophone.ui.settings.model.blockedusers.BlockedUser
import agstack.gramophone.ui.settings.viewmodel.BlockedUsersViewModel
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.os.Build
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_blocked_users.*
import kotlinx.android.synthetic.main.activity_language.*

@AndroidEntryPoint
class BlockedUsersActivity :
    BaseActivityWrapper<ActivityBlockedUsersBinding, BlockedUsersNavigator, BlockedUsersViewModel>(),
    BlockedUsersNavigator {
    private val blockedUsersViewModel: BlockedUsersViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolBar(
            enableBackButton = true, getMessage(R.string.blocked_users),
            R.drawable.ic_arrow_left
        )

        blockedUsersViewModel.getBlockedUsersList()

    }

    override fun getLayoutID() = R.layout.activity_blocked_users

    override fun getBindingVariable() = BR.viewModel

    override fun getViewModel() = blockedUsersViewModel

    override fun updateUserList(
        blockedUsersAdapter: BlockedUsersAdapter?,
        onClicked: (BlockedUser) -> Unit,
    ) {
        if (blockedUsersAdapter != null) {
            blockedUsersAdapter?.selectedUser = onClicked
            rvBlockedUsers.adapter = blockedUsersAdapter
            rvBlockedUsers.visibility = VISIBLE
            tvNoDataFoud.visibility = GONE
        } else {
            rvBlockedUsers.visibility = GONE
            tvNoDataFoud.visibility = VISIBLE
        }
    }

    override fun sendUnBlockUserMoEngageEvent(unblockedUserProfileId: String) {
        val properties = Properties()
            .addAttribute("Profile ID",
                SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.CUSTOMER_ID)!!)
            .addAttribute("Unblocked User Profile_ID", unblockedUserProfileId)
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(this, "KA_Unblock_User", properties)
    }
}