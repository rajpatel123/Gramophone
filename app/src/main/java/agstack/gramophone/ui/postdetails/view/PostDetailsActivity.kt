package agstack.gramophone.ui.postdetails.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityPostDetailsBinding
import agstack.gramophone.ui.postdetails.PostDetailNavigator
import agstack.gramophone.ui.postdetails.viewmodel.PostDetailViewModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostDetailsActivity : BaseActivityWrapper<ActivityPostDetailsBinding,PostDetailNavigator,PostDetailViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolBar(
            enableBackButton = true,
            "",
            R.drawable.ic_arrow_left
        )
    }

    private val postDetailViewModel : PostDetailViewModel by viewModels()

    override fun getLayoutID(): Int = R.layout.activity_post_details

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): PostDetailViewModel = postDetailViewModel


}