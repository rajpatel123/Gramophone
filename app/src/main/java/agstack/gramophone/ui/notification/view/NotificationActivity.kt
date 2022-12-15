package agstack.gramophone.ui.notification.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityNotificationBinding
import agstack.gramophone.ui.notification.NotificationNavigator
import agstack.gramophone.ui.notification.NotificationsAdapter
import agstack.gramophone.ui.notification.model.Data
import agstack.gramophone.ui.notification.viewmodel.NotificationViewModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_likedpost_user_list.*
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
        notificationsAdapter.notificationClicked=notificationClicked
        rvNotification.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvNotification.setHasFixedSize(true)
        rvNotification.adapter = notificationsAdapter
    }
}