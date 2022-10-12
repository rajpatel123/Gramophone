package agstack.gramophone.ui.feedback

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.FeedbackActivityBinding
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Typeface
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FeedbackActivity :
    BaseActivityWrapper<FeedbackActivityBinding, FeedbackNavigator, FeedbackViewModel>(),
    FeedbackNavigator {
    private val feedbackViewModel: FeedbackViewModel by viewModels()

    override fun getLayoutID(): Int {
        return R.layout.feedback_activity
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): FeedbackViewModel {
        return feedbackViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolBar(true, resources.getString(R.string.leave_feedback), R.drawable.ic_arrow_left)

    }

    override fun showFeedbackSubmitted() {
        val feedbackSubmitSuccessAlert = AlertDialog.Builder(this)
        feedbackSubmitSuccessAlert.setTitle(resources.getString(R.string.feedback_submit_success_alert_title))
        feedbackSubmitSuccessAlert.setMessage(resources.getString(R.string.feedback_submit_success_alert_message))
        feedbackSubmitSuccessAlert.setPositiveButton(resources.getString(R.string.ok)) { dialog, id -> onBackPressed() }
        val feedbackSubmitSuccessAlertDialog = feedbackSubmitSuccessAlert.create()
        feedbackSubmitSuccessAlertDialog.show()
        boldPositiveButton(feedbackSubmitSuccessAlertDialog)
    }
    fun boldPositiveButton(alertDialog: AlertDialog) {
        val positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
        positiveButton.setTextColor(ContextCompat.getColor(this, R.color.brand_color))
        positiveButton.setTypeface(null, Typeface.BOLD)
    }

    override fun finishActivity() {
        finish()
    }
}