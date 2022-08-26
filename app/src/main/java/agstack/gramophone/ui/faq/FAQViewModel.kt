package agstack.gramophone.ui.faq

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.settings.SettingsRepository
import agstack.gramophone.ui.faq.model.FAQModel
import agstack.gramophone.ui.referandearn.transaction.AllTransactionsListActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FAQViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : BaseViewModel<FAQNavigator>() {
    var faqData = ArrayList<FAQModel>()
    fun getFAQData() {
        faqData.add(FAQModel(1, "Question1", "Answer1"))
        faqData.add(FAQModel(2, "Question2", "Answer2"))


        getNavigator()?.setFAQAdapter(FAQAdapter(faqData))
    }


}