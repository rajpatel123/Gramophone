package agstack.gramophone.ui.comments.viewModel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.community.CommunityRepository
import agstack.gramophone.ui.comments.CommentNavigator
import agstack.gramophone.ui.home.adapter.CommentsAdapter
import agstack.gramophone.ui.home.view.fragments.community.model.likes.PostRequestModel
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val communityRepository: CommunityRepository
):BaseViewModel<CommentNavigator> (){
    var commentsCount = ObservableField<Int>()

    fun getComments(postId: String) {
        viewModelScope.launch {

            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    val response = communityRepository.getPostComments(PostRequestModel(postId,""))
                    if (response.isSuccessful) {
                        val data = response.body()?.data

                        Log.d("Raj", Gson().toJson(data))
                        commentsCount.set(data?.size)
                        val commentsAdapter = CommentsAdapter(data)
                        getNavigator()?.updateCommentsList(commentsAdapter){

                        }
                    }
                } else
                    getNavigator()?.onError(getNavigator()?.getMessage(R.string.no_internet)!!)
            } catch (ex: Exception) {
                when (ex) {
                    is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.network_failure)!!)
                    else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
                }
            }


        }
    }

    fun onCloseClick(){
        getNavigator()?.finishActivity()
    }
}