package agstack.gramophone.ui.home.view.fragments.community

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityLikedpostUserListBinding
import agstack.gramophone.ui.home.adapter.LikedUsersAdapter
import agstack.gramophone.ui.home.view.fragments.community.viewmodel.LikedUserViewModel
import agstack.gramophone.ui.language.model.languagelist.Language
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_community.*

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
        onLanguageClicked: (Language) -> Unit
    ) {
        rvPost.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvPost.setHasFixedSize(false)
        rvPost.adapter = likedUsersAdapter
    }
}