package agstack.gramophone.ui.dialog

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.utils.Constants.HELP_PHONE_NUMBER
import android.content.Intent
import android.net.Uri
import android.view.View
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DialogViewModel @Inject constructor(
) : BaseViewModel<DialogNavigator>() {

    fun onCallClicked(v: View){
        val customerSupportNumber= getNavigator()?.getBundle()!![HELP_PHONE_NUMBER]
        val intent = Intent(Intent.ACTION_CALL);
        intent.data = Uri.parse("tel:$customerSupportNumber")

        getNavigator()?.callNow(intent)
    }

    fun onCloseClicked(){
        getNavigator()?.closeDialog()
    }

}