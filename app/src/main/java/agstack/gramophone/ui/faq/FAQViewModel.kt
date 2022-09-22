package agstack.gramophone.ui.faq

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.settings.SettingsRepository
import agstack.gramophone.ui.faq.model.FAQModel
import agstack.gramophone.ui.referandearn.model.GramcashFaqItem
import agstack.gramophone.ui.referandearn.transaction.AllTransactionsListActivity
import androidx.databinding.ObservableField
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class FAQViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : BaseViewModel<FAQNavigator>() {
    var faqData = ArrayList<FAQModel>()
    var FAQList = ObservableField<ArrayList<GramcashFaqItem>>()
    fun getFAQData() {

        getNavigator()?.setFAQAdapter(FAQAdapter(FAQList.get()!!))
    }


}