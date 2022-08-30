package agstack.gramophone.ui.home.view.fragments.community

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityLikedpostUserListBinding
import agstack.gramophone.ui.home.view.fragments.community.viewmodel.LikedUserViewModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LikedpostUserListActivity : BaseActivityWrapper<ActivityLikedpostUserListBinding, LikedUserNavigator, LikedUserViewModel>(), LikedUserNavigator {

    private val likedUserViewModel: LikedUserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutID(): Int = R.layout.activity_likedpost_user_list

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): LikedUserViewModel = likedUserViewModel
}