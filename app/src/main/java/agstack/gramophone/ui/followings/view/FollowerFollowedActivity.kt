package agstack.gramophone.ui.followings.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityFollowerFaloweeBinding
import agstack.gramophone.ui.followings.FollowerFollowingNavigator
import agstack.gramophone.ui.followings.FollowsAdapter
import agstack.gramophone.ui.followings.model.Data
import agstack.gramophone.ui.followings.viewmodel.FollowerFollowingViewModel
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_community.*

@AndroidEntryPoint
class FollowerFollowedActivity :
    BaseActivityWrapper<ActivityFollowerFaloweeBinding, FollowerFollowingNavigator, FollowerFollowingViewModel>(),
    FollowerFollowingNavigator, FollowsAdapter.SetImage {
    private val followerFollowingViewModel: FollowerFollowingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        followerFollowingViewModel.initList()
        setUpToolBar(true, getString(R.string.followers).plus(" & ").plus(getString(R.string.following)), R.drawable.ic_arrow_left)

        viewDataBinding.tabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
           override fun onTabSelected(tab: TabLayout.Tab) {

                when (tab.position) {
                    0 -> {
                        followerFollowingViewModel.getFollowers()
                        viewDataBinding.rvFollowerFollowed.visibility= GONE
                    }

                    1 -> {
                        viewDataBinding.rvFollower.visibility= GONE
                        followerFollowingViewModel.getFollowing()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }


    override fun getLayoutID(): Int {
        return R.layout.activity_follower_falowee
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): FollowerFollowingViewModel {
        return followerFollowingViewModel
    }

    override fun updateList(followsAdapter: FollowsAdapter, followClciked: (Data) -> Unit,profileClciked: (Data) -> Unit) {
        followsAdapter.followClicked = followClciked
        followsAdapter.profileClicked = profileClciked
        followsAdapter.setImage = this
        viewDataBinding.rvFollower.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        viewDataBinding.rvFollower.setHasFixedSize(true)
        viewDataBinding.rvFollower.adapter = followsAdapter
        viewDataBinding.rvFollowerFollowed.visibility= GONE
        viewDataBinding.rvFollower.visibility= VISIBLE
        val tab = viewDataBinding?.tabLayout?.getTabAt(0)
        tab!!.select()

    }

    override fun updateListFollowee(followsAdapter: FollowsAdapter, followClciked: (Data) -> Unit,profileClciked: (Data) -> Unit) {
        followsAdapter.followClicked = followClciked
        followsAdapter.profileClicked = profileClciked
        followsAdapter.setImage = this
        viewDataBinding.rvFollowerFollowed.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        viewDataBinding.rvFollowerFollowed.setHasFixedSize(true)
        viewDataBinding.rvFollowerFollowed.adapter = followsAdapter
        viewDataBinding.rvFollowerFollowed.visibility= VISIBLE
        viewDataBinding.rvFollower.visibility= GONE
        val tab = viewDataBinding?.tabLayout?.getTabAt(1)
        tab!!.select()
    }

    override fun getBundle(): Bundle {
        return intent.extras!!
    }

    override fun onImageSet(imageUrl: String, iv: ImageView) {
        Glide.with(this).load(imageUrl).into(iv)
    }
}