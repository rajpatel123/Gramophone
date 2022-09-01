package agstack.gramophone.ui.home.view.fragments.community.viewmodel

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.ui.home.adapter.CommunityPostAdapter
import agstack.gramophone.ui.home.view.fragments.CommunityFragmentNavigator
import agstack.gramophone.ui.home.view.fragments.community.model.Data
import agstack.gramophone.ui.home.view.fragments.community.model.LikedUsers
import agstack.gramophone.ui.home.view.fragments.community.model.PagerItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CommunityViewModel @Inject constructor(

) : BaseViewModel<CommunityFragmentNavigator>() {
    private val _dataList = MutableLiveData<List<Data>>()
    private val userList = MutableLiveData<List<LikedUsers>>()
    val dataList: LiveData<List<Data>>
        get() = _dataList


    val likedUserList: LiveData<List<LikedUsers>>
        get() = userList

    fun loadData() {
        viewModelScope.launch(Dispatchers.Default) {

            val finalList = ArrayList<Data>()

            for (i in 1..20) {

                val data = if (i % 2 == 0) {
                    val pagerItemList = ArrayList<PagerItem>()
                    for (j in 1..10) {
                        pagerItemList.add(
                            PagerItem(
                                itemText = j.toString(),
                                itemImageUrl = imageUrls()[j]
                            )
                        )
                    }
                    Data(
                        viewType = CommunityPostAdapter.VIEW_TYPE_PAGER,
                        pagerItemList = pagerItemList
                    )
                } else {
                    Data(
                        viewType = CommunityPostAdapter.VIEW_TYPE_TEXT,
                        textItem = "List Item: $i"
                    )
                }

                finalList.add(data)
            }

            getNavigator()?.updatePostList(CommunityPostAdapter(finalList)){

            }
        }
    }

    private fun imageUrls(): Array<String> =
        arrayOf(
            "https://picsum.photos/300/200?image=60", "https://picsum.photos/300/200?image=8",
            "https://picsum.photos/300/200?image=34", "https://picsum.photos/300/200?image=54",
            "https://picsum.photos/300/200?image=62", "https://picsum.photos/300/200?image=12",
            "https://picsum.photos/300/200?image=15", "https://picsum.photos/300/200?image=27",
            "https://picsum.photos/300/200?image=35", "https://picsum.photos/300/200?image=69",
            "https://picsum.photos/300/200?image=3", "https://picsum.photos/300/200?image=5"
        )

}