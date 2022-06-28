package agstack.gramophone.ui.home.view.fragments.trading

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseFragment
import agstack.gramophone.databinding.FragmentTradeBinding
import agstack.gramophone.ui.home.view.fragments.community.viewmodel.CommunityViewModel
import agstack.gramophone.ui.home.view.fragments.trading.viewmodel.TradingViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TradeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TradeFragment : BaseFragment<FragmentTradeBinding, TradingFragmentNavigator, TradingViewModel>(),
    TradingFragmentNavigator {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val tradingViewModel: TradingViewModel by viewModels()
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        uiScope.launch {
            //Call Api
        }
    }

    /**
     * Create Binding
     */
    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentTradeBinding = FragmentTradeBinding.inflate(inflater, container, false)


    override fun getLayoutID(): Int {
        return R.layout.fragment_trade
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): TradingViewModel {
        return tradingViewModel
    }




}