package agstack.gramophone.ui.dialog

import agstack.gramophone.base.BaseNavigator
import android.content.Intent
import android.os.Bundle

interface DialogNavigator: BaseNavigator {
    fun getBundle(): Bundle?
    fun callNow(intent: Intent) {

    }

    fun closeDialog()
}