package agstack.gramophone.ui.language

import agstack.gramophone.base.BaseNavigator
import android.os.Bundle

interface LanguageActivityNavigator :BaseNavigator {
     fun <T> moveToNext(cls :Class<T>)
}