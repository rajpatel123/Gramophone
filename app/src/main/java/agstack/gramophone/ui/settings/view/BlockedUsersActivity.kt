package agstack.gramophone.ui.settings.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityBlockedUsersBinding
import agstack.gramophone.ui.settings.BlockedUsersNavigator
import agstack.gramophone.ui.settings.viewmodel.BlockedUsersViewModel
import android.os.Bundle
import androidx.activity.viewModels

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

        //call api  bind data with ui  call unblock api  bind adapter

    }

    override fun getLayoutID() = R.layout.activity_blocked_users

    override fun getBindingVariable() = BR.viewModel

    override fun getViewModel() = blockedUsersViewModel
}