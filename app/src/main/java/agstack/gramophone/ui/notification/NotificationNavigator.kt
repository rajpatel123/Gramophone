package agstack.gramophone.ui.notification

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.notification.model.Data

interface NotificationNavigator : BaseNavigator {
     fun updateNotificationList(body: NotificationsAdapter, notificationClicked: ((Data) -> Unit)? )
}