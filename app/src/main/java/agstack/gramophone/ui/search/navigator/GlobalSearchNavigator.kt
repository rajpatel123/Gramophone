package agstack.gramophone.ui.search.navigator

import agstack.gramophone.base.BaseNavigator

interface GlobalSearchNavigator : BaseNavigator {
    fun notifyAdapter(suggestions: List<String>)
    fun onBackPressClick()
    fun onClearSearchClick()
}