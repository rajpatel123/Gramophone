package agstack.gramophone.ui.home.view.fragments.community

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityLikedpostUserListBinding
import agstack.gramophone.ui.home.adapter.LikedUsersAdapter
import agstack.gramophone.ui.home.view.fragments.community.model.likes.DataX
import agstack.gramophone.ui.home.view.fragments.community.viewmodel.LikedUserViewModel
import agstack.gramophone.utils.Constants.POST_ID
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_likedpost_user_list.*

@AndroidEntryPoint
class LikedPostUserListActivity :
    BaseActivityWrapper<ActivityLikedpostUserListBinding, LikedUserNavigator, LikedUserViewModel>(),
    LikedUserNavigator, LikedUsersAdapter.SetImage {

    private val likedUserViewModel: LikedUserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolBar(
            enableBackButton = true,
            getString(R.string.likes, 0),
            R.drawable.ic_arrow_left
        )
        intent.extras?.getString(POST_ID)?.let { likedUserViewModel.loadUsers(it) }
    }

    override fun getLayoutID(): Int = R.layout.activity_likedpost_user_list

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): LikedUserViewModel = likedUserViewModel


    override fun updateUserList(
        likedUsersAdapter: LikedUsersAdapter,
        onLanguageClicked: (DataX) -> Unit
    ) {

        likedUsersAdapter.setImage=this
        likedUsersAdapter.followClicked=onLanguageClicked
        rvUserList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvUserList.setHasFixedSize(true)
        rvUserList.adapter = likedUsersAdapter
    }

    override fun setUpToolBar(size: Int) {
        runOnUiThread(Runnable {
            setUpToolBar(
                enableBackButton = true,
                getString(R.string.likes, size),
                R.drawable.ic_arrow_left
            )
        })
    }

    override fun onImageSet(imageUrl: String, iv: ImageView) {
        Glide.with(this).load(imageUrl).into(iv)
    }
}