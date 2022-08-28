package agstack.gramophone.ui.userprofile

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.EditProfileActivityBinding
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileActivity :
    BaseActivityWrapper<EditProfileActivityBinding, EditProfileNavigator, EditProfileViewModel>(),
    EditProfileNavigator {

    private val editProfileViewModel: EditProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolBar(true, resources.getString(R.string.editprofile), R.drawable.ic_arrow_left)
    }
    override fun getLayoutID(): Int {
        return R.layout.edit_profile_activity
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): EditProfileViewModel {
        return editProfileViewModel
    }

}