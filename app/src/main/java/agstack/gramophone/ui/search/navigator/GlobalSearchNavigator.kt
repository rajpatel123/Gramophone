package agstack.gramophone.ui.search.navigator

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.search.model.Data

interface GlobalSearchNavigator : BaseNavigator {
    fun notifySuggestionAdapter(suggestions: List<String>)
    fun notifySearchResultAdapter(result: List<Data>)
    fun onBackPressClick()
    fun onClearSearchClick()
}