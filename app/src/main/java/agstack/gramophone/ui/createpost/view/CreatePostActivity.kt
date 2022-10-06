package agstack.gramophone.ui.createpost.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityCreatePostBinding
import agstack.gramophone.ui.createpost.CreatePostNavigator
import agstack.gramophone.ui.createpost.viewmodel.CreatePostViewModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreatePostActivity : BaseActivityWrapper<ActivityCreatePostBinding, CreatePostNavigator,CreatePostViewModel>(), CreatePostNavigator {

    private val createPostViewModel: CreatePostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun getLayoutID(): Int = R.layout.activity_create_post

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): CreatePostViewModel = createPostViewModel
}