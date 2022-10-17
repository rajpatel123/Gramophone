package agstack.gramophone.ui.home.view.fragments.market

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseFragment
import agstack.gramophone.databinding.FragmentMarketBinding
import agstack.gramophone.ui.cart.model.CartItem
import agstack.gramophone.ui.dialog.AppTourDialog
import agstack.gramophone.ui.farm.model.FarmResponse
import agstack.gramophone.ui.home.adapter.HomeAdapter
import agstack.gramophone.ui.home.view.fragments.market.model.FeaturedArticlesResponse
import agstack.gramophone.ui.home.view.fragments.market.model.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MarketFragment.newInstance] factory method to
 * create an instance of this fragment.
 */


@AndroidEntryPoint
class MarketFragment :
    BaseFragment<FragmentMarketBinding, MarketFragmentNavigator, MarketFragmentViewModel>(),
    MarketFragmentNavigator {
    private var param1: String? = null
    private var param2: String? = null
    private val marketFragmentViewModel: MarketFragmentViewModel by viewModels()
    var homeAdapter: HomeAdapter? = null

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ConnectedFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MarketFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    /**
     * Create Binding
     */
    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): FragmentMarketBinding = FragmentMarketBinding.inflate(inflater, container, false)

    /**
     * onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    /**
     * onViewCreated
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
        callApi()
        AppTourDialog().show(childFragmentManager, null)
    }

    private fun callApi() {
        marketFragmentViewModel.getHomeData()
        marketFragmentViewModel.getBanners()
        marketFragmentViewModel.getFeaturedProducts()
        marketFragmentViewModel.getCategories()
        marketFragmentViewModel.getCrops()
        marketFragmentViewModel.getStores()
        marketFragmentViewModel.getCompanies()
        marketFragmentViewModel.getCartProducts()
        marketFragmentViewModel.getFarms()
        marketFragmentViewModel.getFeaturedArticles()
    }

    private fun setUpUI() {
        binding?.swipeRefresh?.setColorSchemeResources(R.color.blue)
        binding?.swipeRefresh?.setOnRefreshListener {
            callApi()
            binding?.swipeRefresh?.isRefreshing = false
        }
    }

    override fun setHomeAdapter(adapter: HomeAdapter, onItemClick: (String) -> Unit) {
        homeAdapter = adapter
        adapter.onItemClicked = onItemClick
        binding?.rvHome?.adapter = homeAdapter
    }

    override fun notifyHomeAdapter(
        allBannerResponse: BannerResponse?,
        categoryResponse: CategoryResponse?,
        allProductsResponse: AllProductsResponse?,
        cropResponse: CropResponse?,
        storeResponse: StoreResponse?,
        companyResponse: CompanyResponse?,
        cartList: List<CartItem>?,
        farmResponse: FarmResponse?,
        featuredArticlesList: ArrayList<FormattedArticlesData>,
    ) {
        homeAdapter?.notifyAdapterOnDataChange(allBannerResponse,
            categoryResponse,
            allProductsResponse,
            cropResponse,
            storeResponse,
            companyResponse,
            cartList,
            farmResponse,
            featuredArticlesList)
    }

    override fun onResume() {
        super.onResume()
        marketFragmentViewModel.getBanners()
    }

    override fun getLayoutID(): Int {
        return R.layout.fragment_market
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): MarketFragmentViewModel {
        return marketFragmentViewModel
    }
}