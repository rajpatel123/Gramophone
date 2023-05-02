package agstack.gramophone.ui.notification.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityNotificationBinding
import agstack.gramophone.ui.notification.NotificationNavigator
import agstack.gramophone.ui.notification.NotificationsAdapter
import agstack.gramophone.ui.notification.model.Data
import agstack.gramophone.ui.notification.viewmodel.NotificationViewModel
import agstack.gramophone.utils.Constants
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_notification.*

@AndroidEntryPoint
class NotificationActivity : BaseActivityWrapper<ActivityNotificationBinding, NotificationNavigator,NotificationViewModel>() , NotificationNavigator{

    private val notificationViewModel: NotificationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolBar(
            enableBackButton = true,
            getMessage(R.string.notifications),
            R.drawable.ic_arrow_left
        )

        notificationViewModel.getNotification()
    }

    override fun getLayoutID(): Int = R.layout.activity_notification

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): NotificationViewModel {
        return notificationViewModel
    }

    override fun updateNotificationList(
        notificationsAdapter: NotificationsAdapter,
        notificationClicked: ((Data) -> Unit)?
    ) {
        viewDataBinding.progressbar.visibility = GONE

        notificationsAdapter.notificationClicked=notificationClicked
        rvNotification.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvNotification.setHasFixedSize(true)
        rvNotification.adapter = notificationsAdapter
    }

    override fun handleDeepLink(it: Data) {
        try {

        } catch (e: Exception) {
            Log.d("Raj", "" + e.printStackTrace())
        }
        intent.data = Uri.parse(it.content.redirectUrl)
        viewDataBinding.progressbar.visibility = VISIBLE

        if (!TextUtils.isEmpty(it.content.redirectUrl) && it.content.redirectUrl.contains("category")){
            viewDataBinding.progressbar.visibility = GONE

            val intent = Intent(this@NotificationActivity, URLHandlerActivity::class.java)
            intent.putExtra(
                Constants.URI, it.content.redirectUrl
            )
            startActivity(intent)
            return
        }

        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                // Get deep link from result (may be null if no link is found)
                var deepLink: Uri? = null
                viewDataBinding.progressbar.visibility = GONE

                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                    val intent = Intent(this@NotificationActivity, URLHandlerActivity::class.java)
                    intent.putExtra(
                        Constants.URI, deepLink
                    )
                    startActivity(intent)


                }
            }
            .addOnFailureListener(this) { e ->
                viewDataBinding.progressbar.visibility = GONE
                Log.d("Raj", "" + e.message)
            }

    }
}