package agstack.gramophone.ui.gramcash

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.faq.FAQAdapter

interface GramCashNavigator :BaseNavigator {
    fun setFAQAdapter(faqAdapter: FAQAdapter)
    fun setGramCashRulesAdapter(faqAdapter: FAQAdapter)
    fun manageaboutBottomPopup()
    fun sendMoEngageEvent(eventName: String)
}