package agstack.gramophone.ui.userprofile.firm

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.EditProfileActivityBinding
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddFirmActivity :
    BaseActivityWrapper<EditProfileActivityBinding, AddFirmNavigator, AddFirmViewModel>(),
    AddFirmNavigator {

    private val editProfileViewModel: AddFirmViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolBar(true, resources.getString(R.string.add_firm), R.drawable.ic_arrow_left)
    }
    override fun getLayoutID(): Int {
        return R.layout.add_firm_activity
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): AddFirmViewModel {
        return editProfileViewModel
    }


}