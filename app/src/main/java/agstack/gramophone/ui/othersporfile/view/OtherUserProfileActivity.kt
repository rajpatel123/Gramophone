package agstack.gramophone.ui.othersporfile.view

import agstack.gramophone.BR
import agstack.gramophone.BuildConfig
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityOtheruserprofileBinding
import agstack.gramophone.databinding.BlockUserDailogueBinding
import agstack.gramophone.databinding.DeletePostDailogueBinding
import agstack.gramophone.databinding.ReportPostDailogueBinding
import agstack.gramophone.ui.home.adapter.CommunityPostAdapter
import agstack.gramophone.ui.home.view.fragments.CommunityFragmentNavigator
import agstack.gramophone.ui.home.view.fragments.community.model.quiz.Option
import agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.Data
import agstack.gramophone.ui.home.view.fragments.community.viewmodel.CommunityViewModel
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.Constants.BLOCKED_STATUS
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RadioGroup
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.amnix.xtension.extensions.isNotNullOrEmpty
import com.amnix.xtension.extensions.runOnUIThread
import com.bumptech.glide.Glide
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_otheruserprofile.*

@AndroidEntryPoint
class OtherUserProfileActivity :
    BaseActivityWrapper<ActivityOtheruserprofileBinding, CommunityFragmentNavigator, CommunityViewModel>(),
    CommunityFragmentNavigator, CommunityPostAdapter.SetImage {

    private val otherProfileViewModel: CommunityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        otherProfileViewModel.uuid = intent.extras?.getString(Constants.AUTHER_UUID)!!
        otherProfileViewModel.id = intent.extras?.getString(Constants.AUTHER_ID)!!
        otherProfileViewModel.redirectionSource =
            intent.extras?.getString(Constants.REDIRECTION_SOURCE)!!
        if (intent.extras?.getString(Constants.POST_ID).isNotNullOrEmpty())
            otherProfileViewModel.postId = intent.extras?.getString(Constants.POST_ID)!!

        otherProfileViewModel.getProfile(intent.getIntExtra(BLOCKED_STATUS,0))
        otherProfileViewModel.getPostByUUID(otherProfileViewModel.uuid,otherProfileViewModel.id)
        sendOtherUserProfileViewMoEngageEvent()
    }


    override fun getLayoutID(): Int = R.layout.activity_otheruserprofile

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): CommunityViewModel = otherProfileViewModel
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
        onProfileImageClicked: (post: Data) -> Unit,
    ) {

        runOnUIThread {//will be removed while api integrations
            communityPostAdapter.onItemCommentsClicked = onItemCommentsClicked
            communityPostAdapter.onItemLikesClicked = onItemLikesClicked
            communityPostAdapter.onItemDetailClicked = onItemDetailClicked
            communityPostAdapter.onShareClicked = onWhatsAppClicked
            communityPostAdapter.onTripleDotMenuClicked = onTripleDotMenuClicked
            communityPostAdapter.onMenuOptionClicked = onMenuOptionClicked
            communityPostAdapter.onLikeClicked = onLikeClicked
            communityPostAdapter.onBookMarkClicked = onBookMarkClicked
            communityPostAdapter.onFollowClicked = onFollowClicked
            communityPostAdapter.onProfileImageClicked = onProfileImageClicked
            communityPostAdapter.setImage = this
            rvMyPost.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            rvMyPost.setHasFixedSize(false)
            rvMyPost.adapter = communityPostAdapter
            if (communityPostAdapter.dataList?.size!! > 0) {
                rvMyPost.visibility = View.GONE
                rvMyPost.visibility = View.VISIBLE
            } else {
                rvMyPost.visibility = View.VISIBLE
                rvMyPost.visibility = View.GONE
            }
            progressBar.visibility = View.GONE


        }
    }

    override fun sharePost(link: String) {
        val whatsappIntent = Intent(Intent.ACTION_SEND)
        whatsappIntent.type = "text/plain"
        whatsappIntent.setPackage("com.whatsapp")
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, link)
        try {
            startActivity(whatsappIntent)
        } catch (ex: ActivityNotFoundException) {
            showToast("Whatsapp have not been installed.")
        }

    }

    override fun deletePostDialog() {
        val mDialogView =
            LayoutInflater.from(this).inflate(R.layout.delete_post_dailogue, null)
        val dialogBinding = DeletePostDailogueBinding.bind(mDialogView)
        dialogBinding.setVariable(BR.viewModel, otherProfileViewModel)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
        //show dialog
        val mAlertDialog = mBuilder.show()
        otherProfileViewModel.setDialog(mAlertDialog)
        mAlertDialog.getWindow()?.setBackgroundDrawableResource(R.drawable.transparent_background);

    }

    override fun reportPostDialog() {
        val mDialogView =
            LayoutInflater.from(this).inflate(R.layout.report_post_dailogue, null)
        val dialogBinding = ReportPostDailogueBinding.bind(mDialogView)
        otherProfileViewModel.unWanted.set(getMessage(R.string.unwanted))
        otherProfileViewModel.hateStr.set(getMessage(R.string.hate))
        otherProfileViewModel.securitStr.set(getMessage(R.string.security))
        otherProfileViewModel.harasgStr.set(getMessage(R.string.harash))
        dialogBinding.setVariable(BR.viewModel, otherProfileViewModel)

        dialogBinding.rbReportReson.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            otherProfileViewModel.getReason(checkedId)

        })


        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
        //show dialog
        val mAlertDialog = mBuilder.show()
        otherProfileViewModel.setDialog(mAlertDialog)
        mAlertDialog.getWindow()?.setBackgroundDrawableResource(R.drawable.transparent_background);

    }

    override fun blockUserDialog() {
        val mDialogView =
            LayoutInflater.from(this).inflate(R.layout.block_user_dailogue, null)
        val dialogBinding = BlockUserDailogueBinding.bind(mDialogView)
        dialogBinding.setVariable(BR.viewModel, otherProfileViewModel)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
        //show dialog
        val mAlertDialog = mBuilder.show()
        otherProfileViewModel.setDialog(mAlertDialog)
        mAlertDialog.getWindow()?.setBackgroundDrawableResource(R.drawable.transparent_background);


    }

    override fun getReportReason(): String {
        return ""
    }

    override fun setProfileImage(profileImage: String) {
        if (profileImage.isNotNullOrEmpty() && !profileImage.equals("null"))
            Glide.with(this).load(profileImage).into(cv_profile_pic)
    }

    override fun stopRefresh() {
    }

    override fun hideViews() {
    }

    override fun selecttab(i: Int) {
    }

    override fun onImageSet(imageUrl: String, iv: ImageView) {
        Glide.with(this).load(imageUrl).into(iv)
    }

    private fun sendOtherUserProfileViewMoEngageEvent() {
        val properties = Properties()
            .addAttribute("Profile ID", otherProfileViewModel.id)
            .addAttribute("Source screen", otherProfileViewModel.redirectionSource)
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(this, "KA_2nd user_Profile", properties)
    }

    override fun sendBlockUserMoEngageEvent() {
        val properties = Properties()
            .addAttribute("Profile ID", otherProfileViewModel.id)
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(this, "KA_2nd KA_Block_User", properties)
    }

    override fun sendEditFollowStatusMoEngageEvent() {
        val properties = Properties()
            .addAttribute("Profile ID", otherProfileViewModel.id)
            .addAttribute("Source screen", otherProfileViewModel.redirectionSource)
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(this, "KA_2nd KA_Edit_Follow_status", properties)
    }
}