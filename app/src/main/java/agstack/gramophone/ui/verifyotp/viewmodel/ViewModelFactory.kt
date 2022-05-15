package agstack.gramophone.ui.verifyotp.viewmodel

import agstack.gramophone.retrofit.ApiHelper
import agstack.gramophone.ui.verifyotp.repository.VerifyOtpRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(VerifyOtpViewModel::class.java)) {
            return VerifyOtpViewModel(VerifyOtpRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}

