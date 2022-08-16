package agstack.gramophone.ui.settings.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityBlockedUsersBinding
import agstack.gramophone.ui.language.model.languagelist.Language
import agstack.gramophone.ui.settings.BlockedUsersAdapter
import agstack.gramophone.ui.settings.BlockedUsersNavigator
import agstack.gramophone.ui.settings.model.blockedusers.BlockedUser
import agstack.gramophone.ui.settings.viewmodel.BlockedUsersViewModel
import android.os.Bundle
import androidx.activity.viewModels
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

    override fun updateUserList(blockedUsersAdapter: BlockedUsersAdapter,  onClicked: (BlockedUser) -> Unit) {
        blockedUsersAdapter.selectedUser = onClicked
        rvBlockedUsers.adapter=blockedUsersAdapter
    }
}