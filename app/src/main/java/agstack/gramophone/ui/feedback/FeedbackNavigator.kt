package agstack.gramophone.ui.feedback

import agstack.gramophone.base.BaseNavigator

interface FeedbackNavigator :BaseNavigator {
    fun finishActivity()
     fun getFeedbackText(): String?
}