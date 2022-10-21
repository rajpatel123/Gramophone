package agstack.gramophone.ui.home.view.fragments.gramophone

import agstack.gramophone.base.BaseNavigator

interface MyGramophoneFragmentNavigator :BaseNavigator {
    fun getUserName(): String?
    fun getUserAddress(): String?
    fun setUserImage()
}