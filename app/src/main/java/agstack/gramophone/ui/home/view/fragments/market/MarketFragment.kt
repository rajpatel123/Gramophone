package agstack.gramophone.ui.home.view.fragments.market

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseFragment
import agstack.gramophone.databinding.FragmentMarketBinding
import agstack.gramophone.ui.cart.model.CartItem
import agstack.gramophone.ui.dialog.AppTourDialog
import agstack.gramophone.ui.farm.model.FarmResponse
import agstack.gramophone.ui.home.adapter.HomeAdapter
import agstack.gramophone.ui.home.view.HomeActivity
import agstack.gramophone.ui.home.view.fragments.market.model.*
import agstack.gramophone.ui.weather.model.WeatherResponse
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.amnix.xtension.extensions.isNotNullOrEmpty
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
    val marketFragmentViewModel: MarketFragmentViewModel by viewModels()
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
        marketFragmentViewModel.showAppTourDialogIfApplicable()
    }

    private fun callApi() {
        marketFragmentViewModel.getHomeData()
        marketFragmentViewModel.getFeaturedProducts()
        marketFragmentViewModel.getCategories()
        marketFragmentViewModel.getCrops()
        marketFragmentViewModel.getStores()
        marketFragmentViewModel.getWeatherDetail()
        marketFragmentViewModel.getCompanies()
        marketFragmentViewModel.getCartProducts()
        marketFragmentViewModel.getFarms()
        marketFragmentViewModel.getFeaturedArticles()
    }

    private fun setUpUI() {
        binding?.swipeRefresh?.setColorSchemeResources(R.color.blue)
        binding?.swipeRefresh?.setOnRefreshListener {
            marketFragmentViewModel.getBanners(true)
            callApi()
            binding?.swipeRefresh?.isRefreshing = false
        }
    }

    override fun launchCommunityFragment() {
        if (activity is HomeActivity) {
            (activity as HomeActivity).showCommunityFragment("home")
        }
    }

    override fun setHomeAdapter(adapter: HomeAdapter, onItemClick: (String) -> Unit) {
        homeAdapter = adapter
        homeAdapter?.updateFragmentManager(activity?.supportFragmentManager!!)
        adapter.onItemClicked = onItemClick
        binding?.rvHome?.adapter = homeAdapter
    }

    override fun showAppTourDialog() {
        AppTourDialog().show(childFragmentManager, null)
    }

    override fun notifyBannerItemInserted(allBannerResponse: BannerResponse?, position: Int) {
        homeAdapter?.notifyBannerItemInserted(allBannerResponse, position)
    }

    override fun notifyCategoryItemInserted(categoryResponse: CategoryResponse?, position: Int) {
        homeAdapter?.notifyCategoryItemInserted(categoryResponse, position)
    }

    override fun notifyFeaturedItemInserted(
        allProductsResponse: AllProductsResponse?,
        position: Int,
    ) {
        homeAdapter?.notifyFeaturedItemInserted(allProductsResponse, position)
    }

    override fun notifyCropItemInserted(cropResponse: CropResponse?, position: Int) {
        homeAdapter?.notifyCropItemInserted(cropResponse, position)
    }

    override fun notifyStoreItemInserted(storeResponse: StoreResponse?, position: Int) {
        homeAdapter?.notifyStoreItemInserted(storeResponse, position)
    }

    override fun notifyCompanyItemInserted(companyResponse: CompanyResponse?, position: Int) {
        homeAdapter?.notifyCompanyItemInserted(companyResponse, position)
    }

    override fun notifyCartItemInserted(cartList: List<CartItem>?, position: Int) {
        homeAdapter?.notifyCartItemInserted(cartList, position)
        if (cartList.isNotNullOrEmpty()) {
            (activity as HomeActivity).updateCartCount(cartList!!.size)
        } else {
            (activity as HomeActivity).updateCartCount(0)
        }
    }

    override fun notifyFarmItemInserted(farmResponse: FarmResponse?, position: Int) {
        homeAdapter?.notifyFarmItemInserted(farmResponse, position)
    }

    override fun notifyArticleItemInserted(
        articlesData: HashMap<String, ArrayList<FormattedArticlesData>>,
        position: Int,
    ) {
        homeAdapter?.notifyArticleItemInserted(
            articlesData, position)
    }

    override fun notifyWeatherItemInserted(weatherResponse: WeatherResponse?, position: Int) {
        homeAdapter?.notifyWeatherItemInserted(weatherResponse, position)
    }

    override fun onResume() {
        super.onResume()
        marketFragmentViewModel.getBanners(false)
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

    override fun getActivityContext(): Activity {
        return activity as HomeActivity
    }
}