package agstack.gramophone.ui.home.view.fragments.profile

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.ui.home.repository.HomeRepository
import javax.inject.Inject

class ProfileFragmentViewModel
@Inject constructor(
    private val homeRepository: HomeRepository
) : BaseViewModel<ProfileFragmentNavigator>() {

}