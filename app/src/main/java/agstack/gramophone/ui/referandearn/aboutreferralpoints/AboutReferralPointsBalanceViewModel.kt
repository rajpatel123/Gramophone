package agstack.gramophone.ui.referandearn.aboutreferralpoints

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.settings.SettingsRepository
import agstack.gramophone.ui.referandearn.model.ReferralPointsItem
import androidx.databinding.ObservableField
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class AboutReferralPointsBalanceViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : BaseViewModel<AboutReferralPointsBalanceNavigator>() {

    var ReferralPointsBalanceDataFromBundle = ObservableField<ArrayList<ReferralPointsItem>>()
    fun setAdapter() {
       /* getNavigator()?.setAboutReferalPointsAdapter(
            AboutReferalPointsAdapter(ArrayList(ReferralPointsBalanceDataFromBundle.get())
            )
        )*/
    }







}