package agstack.gramophone.ui.home.view.fragments.community

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseFragment
import agstack.gramophone.databinding.BlockUserDailogueBinding
import agstack.gramophone.databinding.DeletePostDailogueBinding
import agstack.gramophone.databinding.FragmentCommunityBinding
import agstack.gramophone.databinding.ReportPostDailogueBinding
import agstack.gramophone.ui.home.adapter.CommunityPostAdapter
import agstack.gramophone.ui.home.view.fragments.CommunityFragmentNavigator
import agstack.gramophone.ui.home.view.fragments.community.model.quiz.Option
import agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.Data
import agstack.gramophone.ui.home.view.fragments.community.viewmodel.CommunityViewModel
import agstack.gramophone.utils.ShareSheetPresenter
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amnix.xtension.extensions.runOnUIThread
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_community.*


@AndroidEntryPoint
class CommunityFragment : BaseFragment<FragmentCommunityBinding, CommunityFragmentNavigator,CommunityViewModel>() , CommunityFragmentNavigator,
    CommunityPostAdapter.SetImage {

    private lateinit var reportReason: CharSequence
    private var shareSheetPresenter: ShareSheetPresenter? = null
    private val communityViewModel: CommunityViewModel by viewModels()
    private var recyclerView: RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding?.swipeRefresh?.setOnRefreshListener {
            binding?.swipeRefresh?.isRefreshing=true
            communityViewModel.loadData(communityViewModel.sorting.get().toString())
        }

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
        shareSheetPresenter = this?.let { ShareSheetPresenter(requireActivity()) }
        communityViewModel.sorting.set("latest")
        communityViewModel.getQuiz()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }
    override fun updatePostList(
        communityPostAdapter: CommunityPostAdapter,
        quizPollAnswered: (option: Option) -> Unit,
        onItemDetailClicked: (postId: String) -> Unit,
        onItemLikesClicked: (postId: String) -> Unit,
        onItemCommentsClicked: (postId: String) -> Unit,
        onWhatsAppClicked: (postId: String) -> Unit,
        onTripleDotMenuClicked: (postId: String) -> Unit,
        onMenuOptionClicked: (post: Data) -> Unit,
        onLikeClicked: (post: Data) -> Unit,
        onBookMarkClicked: (post: Data) -> Unit,
        onFollowClicked: (post: Data) -> Unit,
        onProfileImageClicked: (post: Data) -> Unit

    ) {
        runOnUIThread {//will be removed while api integrations
            communityPostAdapter.quizPollAnswered=quizPollAnswered
            communityPostAdapter.onItemCommentsClicked=onItemCommentsClicked
            communityPostAdapter.onItemLikesClicked=onItemLikesClicked
            communityPostAdapter.onItemDetailClicked=onItemDetailClicked
            communityPostAdapter.onShareClicked=onWhatsAppClicked
            communityPostAdapter.onTripleDotMenuClicked=onTripleDotMenuClicked
            communityPostAdapter.onMenuOptionClicked=onMenuOptionClicked
            communityPostAdapter.onLikeClicked=onLikeClicked
            communityPostAdapter.onBookMarkClicked=onBookMarkClicked
            communityPostAdapter.onFollowClicked=onFollowClicked
            communityPostAdapter.onProfileImageClicked=onProfileImageClicked
            communityPostAdapter.setImage = this
            rvPost.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            rvPost.setHasFixedSize(false)
            rvPost.adapter = communityPostAdapter
            if (communityPostAdapter.dataList?.size!! >0){
                tvNoPost.visibility=GONE
                rvPost.visibility= VISIBLE
            }else{
                tvNoPost.visibility= VISIBLE
                rvPost.visibility= GONE
            }
            progress.visibility= GONE



            swipeRefresh.setOnRefreshListener {
                swipeRefresh.isRefreshing=true
                communityViewModel.loadData(communityViewModel.sorting.get().toString())
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // communityViewModel.loadData(communityViewModel.sorting.get().toString())
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                progress.visibility= VISIBLE
                communityViewModel.filterPost(tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
    override fun sharePost(link: String) {
        val whatsappIntent = Intent(Intent.ACTION_SEND)
        whatsappIntent.type = "text/plain"
        whatsappIntent.setPackage("com.whatsapp")
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, link)
        try {
            requireActivity().startActivity(whatsappIntent)
        } catch (ex: ActivityNotFoundException) {
            showToast("Whatsapp have not been installed.")
        }

    }

    override fun deletePostDialog() {
        val mDialogView =
            LayoutInflater.from(activity).inflate(R.layout.delete_post_dailogue, null)
        val dialogBinding = DeletePostDailogueBinding.bind(mDialogView)
        dialogBinding.setVariable(BR.viewModel, communityViewModel)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(activity)
            .setView(dialogBinding.root)
        //show dialog
        val mAlertDialog = mBuilder.show()
        communityViewModel.setDialog(mAlertDialog)
        mAlertDialog.getWindow()?.setBackgroundDrawableResource(R.drawable.transparent_background);

    }

    override fun reportPostDialog() {
        val mDialogView =
            LayoutInflater.from(activity).inflate(R.layout.report_post_dailogue, null)
        val dialogBinding = ReportPostDailogueBinding.bind(mDialogView)
        communityViewModel.unWanted.set(getMessage(R.string.unwanted))
        communityViewModel.hateStr.set(getMessage(R.string.hate))
        communityViewModel.securitStr.set(getMessage(R.string.security))
        communityViewModel.harasgStr.set(getMessage(R.string.harash))
        dialogBinding.setVariable(BR.viewModel, communityViewModel)

        dialogBinding.rbReportReson.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            communityViewModel.getReason(checkedId)

        })



        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(activity)
            .setView(dialogBinding.root)
        //show dialog
        val mAlertDialog = mBuilder.show()
        communityViewModel.setDialog(mAlertDialog)
        mAlertDialog.getWindow()?.setBackgroundDrawableResource(R.drawable.transparent_background);

    }

    override fun blockUserDialog() {
        val mDialogView =
            LayoutInflater.from(activity).inflate(R.layout.block_user_dailogue, null)
        val dialogBinding = BlockUserDailogueBinding.bind(mDialogView)
        dialogBinding.setVariable(BR.viewModel, communityViewModel)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(activity)
            .setView(dialogBinding.root)
        //show dialog
        val mAlertDialog = mBuilder.show()
        communityViewModel.setDialog(mAlertDialog)
        mAlertDialog.getWindow()?.setBackgroundDrawableResource(R.drawable.transparent_background);


    }

    override fun getReportReason(): String {
        TODO("Not yet implemented")
    }

    override fun setProfileImage(url:String) {
    }

    override fun stopRefresh() {
        swipeRefresh.isRefreshing = false
    }

    override fun hideViews() {
        tvNoPost.visibility = GONE
        rvPost.visibility = GONE
    }

    override fun selecttab(tabIndex: Int) {
//        val tab = binding?.tabLayout?.getTabAt(tabIndex)
//        tab!!.select()

    }

    override fun onImageSet(imageUrl: String, iv: ImageView) {
        activity?.let { Glide.with(it).load(imageUrl).into(iv) }
    }

    fun selectTab(from: String) {
        if (from.equals("gramophone")) {
            communityViewModel.loadData("bookmark")
            Handler().postDelayed(Runnable {
                if (communityViewModel.myFavoriteCount!! > 0) {
                    val tab = binding?.tabLayout?.getTabAt(5)
                    tab!!.select()
                } else {
                    communityViewModel.loadData("latest")
                    val tab = binding?.tabLayout?.getTabAt(0)
                    tab!!.select()
                }
            }, 300)
        } else if (from.equals("gramophone_my_post")) {
            Handler().postDelayed(Runnable {
                val tab = binding?.tabLayout?.getTabAt(4)
                tab!!.select()
            }, 300)
        }



    }


}