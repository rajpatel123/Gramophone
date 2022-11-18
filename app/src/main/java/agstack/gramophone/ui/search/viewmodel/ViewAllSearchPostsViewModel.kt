package agstack.gramophone.ui.search.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.community.CommunityRepository
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.home.view.fragments.community.model.likes.PostRequestModel
import agstack.gramophone.ui.search.navigator.ViewAllSearchPostsNavigator
import agstack.gramophone.utils.Utility
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ViewAllSearchPostsViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val communityRepository: CommunityRepository,
) : BaseViewModel<ViewAllSearchPostsNavigator>() {
    var progress = MutableLiveData<Boolean>()

    init {
        progress.value = false
    }

    fun likePost(body: PostRequestModel) {
        progress.value = true
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    val response = communityRepository.likePost(body)
                    if (response.isSuccessful) {
                        getNavigator()?.onLikePostSuccess(response.body())
                    } else {
                        getNavigator()?.onError(Utility.getErrorMessage(response.errorBody()))
                    }
                } else {
                    getNavigator()?.onError(getNavigator()?.getMessage(R.string.no_internet)!!)
                }
                progress.value = false
            } catch (ex: Exception) {
                progress.value = false
                when (ex) {
                    is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.network_failure)!!)
                    else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
                }
            }
        }
    }
}