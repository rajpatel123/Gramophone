package agstack.gramophone.ui.verifyotp.viewmodel

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.ui.verifyotp.VerifyOTPNavigator
import agstack.gramophone.ui.verifyotp.repository.VerifyOtpRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VerifyOtpViewModel @Inject constructor(
    private val verifyOtpRepository: VerifyOtpRepository
) : BaseViewModel<VerifyOTPNavigator>() {

}