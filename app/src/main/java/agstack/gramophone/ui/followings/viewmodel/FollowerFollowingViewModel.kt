package agstack.gramophone.ui.followings.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.community.CommunityRepository
import agstack.gramophone.ui.followings.FollowerFollowingNavigator
import agstack.gramophone.ui.followings.FollowsAdapter
import agstack.gramophone.ui.followings.model.Data
import agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.FollowRequestModel
import agstack.gramophone.ui.othersporfile.view.OtherUserProfileActivity
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.Constants.PAGE
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import agstack.gramophone.utils.Utility
import android.os.Bundle
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class FollowerFollowingViewModel @Inject constructor(
    private val communityRepository: CommunityRepository
) : BaseViewModel<FollowerFollowingNavigator>() {

    val followerTxt = ObservableField<String>()
    val followeeTxt = ObservableField<String>()
    val progress = ObservableField<Boolean>()
    val isFollowerSelected = ObservableField<Boolean>()
    fun getFollowers() {
        if (getNavigator()?.isNetworkAvailable() == true) {
            progress.set(true)
            viewModelScope.launch {
                val response = communityRepository.getFollowers(
                    FollowRequestModel(
                        SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.UUIdKey)
                            .toString()
                    )
                )
                progress.set(false)

                if (response.isSuccessful && response.body() != null) {
                    followerTxt.set(
                        String.format(
                            getNavigator()?.getMessage(R.string.followers_with_count)!!,
                            response.body()!!.data.size
                        )
                    )
                    getNavigator()?.updateList(FollowsAdapter(response.body()!!.data), {
                        followUser(it)
                    }, {
                        getNavigator()?.openActivity(
                            OtherUserProfileActivity::class.java,
                            Bundle().apply {
                                putString(Constants.AUTHER_ID, it._id)
                                putString(Constants.AUTHER_UUID, it.uuid)
                            })
                    })
                } else {
                    getNavigator()?.showToast(Utility.getErrorMessage(response.errorBody()))
                }

            }
        }
    }


    fun getFollowing() {
        if (getNavigator()?.isNetworkAvailable() == true) {
            progress.set(true)
            viewModelScope.launch {
                val response = communityRepository.getFollowings(
                    FollowRequestModel(
                        SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.UUIdKey)
                            .toString()
                    )
                )
                progress.set(false)

                if (response.isSuccessful && response.body() != null) {
                    followerTxt.set(
                        String.format(
                            getNavigator()?.getMessage(R.string.following_with_count)!!,
                            response.body()!!.data.size
                        )
                    )
                    getNavigator()?.updateListFollowee(FollowsAdapter(response.body()!!.data),
                        {
                            followPost(it)
                        },
                        {
                            getNavigator()?.openActivity(
                                OtherUserProfileActivity::class.java,
                                Bundle().apply {
                                    putString(Constants.AUTHER_ID, it._id)
                                    putString(Constants.AUTHER_UUID, it.uuid)
                                })
                        })
                } else {
                    getNavigator()?.showToast(Utility.getErrorMessage(response.errorBody()))
                }

            }
        }
    }

    fun initList() {
        followerTxt.set(getNavigator()?.getMessage(R.string.followers))
        followeeTxt.set(getNavigator()?.getMessage(R.string.following))
        if (getNavigator()?.getBundle()?.getString(PAGE)?.equals(PAGE) == true) {
            getFollowers()
            isFollowerSelected.set(true)
        } else {
            getFollowing()
            isFollowerSelected.set(false)

        }
    }

    private fun followPost(it: Data) {
        viewModelScope.launch {

            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    val response =
                        communityRepository.followPost(FollowRequestModel(it.uuid))
                    if (response.isSuccessful) {
                        getFollowing()
                    } else {
                        getNavigator()?.showToast(Utility.getErrorMessage(response.errorBody()))
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
    private fun followUser(it: Data) {
        viewModelScope.launch {

            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    val response =
                        communityRepository.followPost(FollowRequestModel(it.uuid))
                    if (response.isSuccessful) {
                        getFollowers()
                    } else {
                        getNavigator()?.showToast(Utility.getErrorMessage(response.errorBody()))
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

}