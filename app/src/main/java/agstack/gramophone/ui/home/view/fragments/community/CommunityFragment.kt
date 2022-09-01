package agstack.gramophone.ui.home.view.fragments.community

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseFragment
import agstack.gramophone.databinding.FragmentCommunityBinding
import agstack.gramophone.ui.home.adapter.CommunityPostAdapter
import agstack.gramophone.ui.home.view.fragments.CommunityFragmentNavigator
import agstack.gramophone.ui.home.view.fragments.community.viewmodel.CommunityViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_community.*


@AndroidEntryPoint
class CommunityFragment : BaseFragment<FragmentCommunityBinding, CommunityFragmentNavigator,CommunityViewModel>() , CommunityFragmentNavigator{


    private val communityViewModel: CommunityViewModel by viewModels()
    private var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun getLayoutID(): Int = R.layout.fragment_community

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): CommunityViewModel = communityViewModel

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentCommunityBinding = FragmentCommunityBinding.inflate(inflater, container, false)


    override fun onResume() {
        super.onResume()
        communityViewModel.loadData()
    }

    override fun updatePostList(communityPostAdapter: CommunityPostAdapter, post: () -> Unit) {
        rvPost.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvPost.setHasFixedSize(false)
        rvPost.adapter = communityPostAdapter
    }
}