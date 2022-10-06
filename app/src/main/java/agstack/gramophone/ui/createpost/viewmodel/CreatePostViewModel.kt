package agstack.gramophone.ui.createpost.viewmodel

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.community.CommunityRepository
import agstack.gramophone.ui.createpost.CreatePostNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val communityRepository: CommunityRepository
):BaseViewModel<CreatePostNavigator>() {

}