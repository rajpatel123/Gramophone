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
      /*  faqData.add(
            FAQModel(
                1,
                "What is Gramophone Referral Scheme?",
                "Gramophone referral scheme encourages your farmer friends to do smart farming just like you and gives you a golden opportunity to earn as referral village cash points. As soon as your farmer friends get connected by downloading the Gramophone Krishi App from the referral link, they also get referral village cash points."
            )
        )
        faqData.add(
            FAQModel(
                2,
                "What is the process and benefits of connecting Kisan Mitra with the app?",
                "Open the Gramophone app and tap the three-line symbol in the top left corner. By doing this a list will appear in front of you in which you have to go to the “Refer” option.\n" +
                        "\n" +
                        "A new page will open in front of you by going to the “Refer” option. On this page you will find a 'referral code'. You share this code with your farmer friend through Facebook and WhatsApp.\n" +
                        "\n" +
                        "From the shared link and code, when your friend installs and successfully registers Gramophone App, you will get 150 Referral Gram Cash Points and your friend will get 100 Referral Gram Cash Points.\n" +
                        "\n" +
                        "You can use 50 points out of 150 referral gram cash points you get immediately. You will be able to use the remaining 100 points when your friend makes the first successful purchase through the Gramophone app.\n" +
                        "\n" +
                        "Your friend has to make the first purchase within 30 days of joining the Gramophone app, then you will get the remaining 100 Referral Gram cash points. If this is not done then after 30 days your 100 points will be exhausted.\n" +
                        "\n" +
                        "In this way you can earn total 150 referral gram cash points in this whole process."
            )
        )
        faqData.add(
            FAQModel(
                1, "How many days will the referral point be valid", "\n" +
                        "The Referral Gram Cash Points earned by you will have a validity of 60 days.\n" +
                        "\n" +
                        "The Referral Village Cash Points received by your Kisan Mitra who have installed the app from the referral link will also have a validity of 60 days."
            )
        )
        faqData.add(
            FAQModel(
                1,
                "How many farmer friends can I connect with the gramophone?",
                "There is no limit to add Kisan Mitras to Gramophone, you can add as many Kisan Mitras as you want."
            )
        )
        faqData.add(
            FAQModel(
                1,
                "Can I connect a farmer of any other state with the gramophone?",
                "You can connect farmer of any state of India with gramophone. But you or the farmer joining through you will be able to get our services in the same areas where we are working. If you and the farmer connected through you are from the state under the jurisdiction of Bhai Gramophone, then both of you can buy from the referral village cash point obtained through referral.\n"
            )
        )
*/


        getNavigator()?.setFAQAdapter(FAQAdapter(FAQList.get()!!))
    }


}