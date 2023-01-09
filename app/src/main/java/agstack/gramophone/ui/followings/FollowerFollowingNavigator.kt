package agstack.gramophone.ui.followings

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.followings.model.Data
import android.os.Bundle

interface FollowerFollowingNavigator: BaseNavigator {
    fun updateList(followsAdapter: FollowsAdapter, followClicked: (Data) -> Unit,profileClicked: (Data) -> Unit)
    fun updateListFollowee(followsAdapter: FollowsAdapter, followClicked: (Data) -> Unit,profileClicked: (Data) -> Unit)
    fun getBundle(): Bundle
    fun sendFollowerMoEngageEvents(_id: String, isFollow: String, source: String)
    fun sendFollowingMoEngageEvents(_id: String, isFollow: String, source: String)
}