package agstack.gramophone.ui.settings

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.settings.model.blockedusers.BlockedUser

interface BlockedUsersNavigator : BaseNavigator {
    fun updateUserList(blockedUsersAdapter: BlockedUsersAdapter?,  onClicked: (BlockedUser) -> Unit)
    fun sendUnBlockUserMoEngageEvent(unblockedUserProfileId: String)
}