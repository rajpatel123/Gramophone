package agstack.gramophone.ui.language

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.language.adapter.LanguageAdapter
import agstack.gramophone.ui.language.model.languagelist.Language
import android.content.ContentResolver
import android.content.res.Resources

interface LanguageActivityNavigator :BaseNavigator {
    fun updateLanguageList(languageAdapter: LanguageAdapter, onLanguageClicked: (Language) -> Unit)
    fun getResource(): Resources
    abstract fun getContentResolver(): ContentResolver?
    fun initiateApp()
    fun closeActivity()
    fun getLanguageCode(): String?

}