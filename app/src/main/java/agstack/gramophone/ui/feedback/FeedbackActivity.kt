package agstack.gramophone.ui.feedback

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.FeedbackActivityBinding
import agstack.gramophone.databinding.UnitConverterActivityBinding
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import androidx.activity.viewModels
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
        initViews()
    }

    private fun initViews() {
    }

    override fun getFeedbackText(): String? {
       return viewDataBinding.etFeeback?.toString()
    }

    override fun finishActivity() {
        finish()
    }
}