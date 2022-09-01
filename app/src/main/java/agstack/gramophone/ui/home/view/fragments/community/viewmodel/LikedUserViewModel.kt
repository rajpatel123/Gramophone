package agstack.gramophone.ui.home.view.fragments.community.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.ui.home.adapter.LikedUsersAdapter
import agstack.gramophone.ui.home.view.fragments.community.LikedUserNavigator
import agstack.gramophone.ui.home.view.fragments.community.model.LikedUsers
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LikedUserViewModel @Inject constructor(

): BaseViewModel<LikedUserNavigator>(){
    fun loadUsers() {
        viewModelScope.launch(Dispatchers.Default) {

            val finalList = ArrayList<LikedUsers>()

            for (i in 1..20) {


                finalList.add(LikedUsers(
                    name="Testname"+i,
                    image = R.drawable.dummy_profile,
                    location = "Gurgaon"
                ))
            }

          getNavigator()?.updateUserList(LikedUsersAdapter(finalList)){
              getNavigator()?.showToast(it.name)
          }
        }
    }


}