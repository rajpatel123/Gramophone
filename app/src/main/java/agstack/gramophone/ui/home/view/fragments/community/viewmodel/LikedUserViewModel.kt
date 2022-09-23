package agstack.gramophone.ui.home.view.fragments.community.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.community.CommunityRepository
import agstack.gramophone.ui.home.adapter.CommunityPostAdapter
import agstack.gramophone.ui.home.adapter.LikedUsersAdapter
import agstack.gramophone.ui.home.view.fragments.community.LikedPostUserListActivity
import agstack.gramophone.ui.home.view.fragments.community.LikedUserNavigator
import agstack.gramophone.ui.home.view.fragments.community.model.likes.LikedUsers
import agstack.gramophone.ui.home.view.fragments.community.model.likes.PostRequestModel
import agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.CommunityRequestModel
import agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.LikedUsersRequestModel
import agstack.gramophone.ui.postdetails.view.PostDetailsActivity
import agstack.gramophone.utils.Constants
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.amnix.xtension.extensions.runOnUIThread
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class LikedUserViewModel @Inject constructor(
    private val communityRepository: CommunityRepository
): BaseViewModel<LikedUserNavigator>(){

    var isDataAvailable    = ObservableField<Boolean>()

    fun loadUsers(postId: String) {
        viewModelScope.launch(Dispatchers.Default) {
            getUsers(postId)
        }
    }


    private suspend fun getUsers(postId:String) {

        getNavigator()?.onLoading()
        try {
            if (getNavigator()?.isNetworkAvailable() == true) {
                val response = communityRepository.getLikedUsers(PostRequestModel(postId,""))
                if (response.isSuccessful) {
                  runOnUIThread {
                      val data =  response.body()?.data
                      if (data!=null && data.size>0){
                          isDataAvailable.set(true)
                          getNavigator()?.setUpToolBar(data.size)
                          getNavigator()?.updateUserList(
                              LikedUsersAdapter(data),
                              {//follow click

                              })
                      }else{
                          isDataAvailable.set(false)

                      }
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