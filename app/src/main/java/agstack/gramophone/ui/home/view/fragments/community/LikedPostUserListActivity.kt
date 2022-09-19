package agstack.gramophone.ui.home.view.fragments.community

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityLikedpostUserListBinding
import agstack.gramophone.ui.home.adapter.LikedUsersAdapter
import agstack.gramophone.ui.home.view.fragments.community.model.likes.LikedUsers
import agstack.gramophone.ui.home.view.fragments.community.viewmodel.LikedUserViewModel
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_likedpost_user_list.*

@AndroidEntryPoint
class LikedPostUserListActivity : BaseActivityWrapper<ActivityLikedpostUserListBinding, LikedUserNavigator, LikedUserViewModel>(), LikedUserNavigator {

    private val likedUserViewModel: LikedUserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        likedUserViewModel.loadUsers()
    }

    override fun getLayoutID(): Int = R.layout.activity_likedpost_user_list

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): LikedUserViewModel = likedUserViewModel


    override fun updateUserList(
        likedUsersAdapter: LikedUsersAdapter,
        onLanguageClicked: (LikedUsers) -> Unit
    ) {
        rvUserList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvUserList.setHasFixedSize(false)
        rvUserList.adapter = likedUsersAdapter
    }

    override fun setUpToolBar(size: Int) {
       runOnUiThread(Runnable {
           setUpToolBar(
               enableBackButton = true,
               getString(R.string.likes,size),
               R.drawable.ic_arrow_left
           )
       })
    }
}