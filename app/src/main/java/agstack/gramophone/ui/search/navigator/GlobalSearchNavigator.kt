package agstack.gramophone.ui.search.navigator

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.home.view.fragments.community.model.likes.LikePostResponseModel
import agstack.gramophone.ui.search.model.Data
import retrofit2.Response

interface GlobalSearchNavigator : BaseNavigator {
    fun notifySuggestionAdapter(suggestions: List<String>)
    fun notifySearchResultAdapter(result: List<Data>)
    fun onBackPressClick()
    fun onClearSearchClick()
    fun onLikePostSuccess(response: LikePostResponseModel?)
}