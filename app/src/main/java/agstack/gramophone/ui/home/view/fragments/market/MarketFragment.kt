package agstack.gramophone.ui.home.view.fragments.market

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseFragment
import agstack.gramophone.databinding.FragmentMarketBinding
import agstack.gramophone.ui.cart.model.CartItem
import agstack.gramophone.ui.dialog.AppTourDialog
import agstack.gramophone.ui.dialog.LocationAccessDialog
import agstack.gramophone.ui.home.adapter.*
import agstack.gramophone.ui.home.featured.FeaturedProductActivity
import agstack.gramophone.ui.home.product.activity.ProductDetailsActivity
import agstack.gramophone.ui.home.shop.ShopByActivity
import agstack.gramophone.ui.home.subcategory.SubCategoryActivity
import agstack.gramophone.ui.home.view.fragments.market.model.*
import agstack.gramophone.utils.Constants
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


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
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
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
    }

    private fun setUpUI() {
        binding?.swipeRefresh?.setColorSchemeResources(R.color.blue)
        binding?.swipeRefresh?.setOnRefreshListener {
            callApi()
            binding?.swipeRefresh?.isRefreshing = false
        }
        binding?.viewAllFeaturedProduct?.setOnClickListener {
            openActivity(FeaturedProductActivity::class.java, null)
        }
        binding?.viewAllCrops?.setOnClickListener {
            openActivity(ShopByActivity::class.java, Bundle().apply {
                putString(Constants.SHOP_BY_TYPE, Constants.SHOP_BY_CROP)
                putParcelable(Constants.SHOP_BY_CROP, marketFragmentViewModel.cropResponse)
            })
        }
        binding?.viewAllStores?.setOnClickListener {
            openActivity(ShopByActivity::class.java, Bundle().apply {
                putString(Constants.SHOP_BY_TYPE, Constants.SHOP_BY_STORE)
                putParcelable(Constants.SHOP_BY_STORE, marketFragmentViewModel.storeResponse)
            })
        }
        binding?.viewAllCompanies?.setOnClickListener {
            openActivity(ShopByActivity::class.java, Bundle().apply {
                putString(Constants.SHOP_BY_TYPE, Constants.SHOP_BY_COMPANY)
                putParcelable(Constants.SHOP_BY_COMPANY, marketFragmentViewModel.companyResponse)
            })
        }

        binding?.rvMandiRates?.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding?.rvMandiRates?.setHasFixedSize(true)
        binding?.rvMandiRates?.adapter = MandiRatesAdapter()


        binding?.rvArticles?.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding?.rvArticles?.setHasFixedSize(true)
        binding?.rvArticles?.adapter = ArticlesAdapter()

        binding?.rvPopularProducts?.layoutManager = GridLayoutManager(activity, 2)
        binding?.rvPopularProducts?.setHasFixedSize(true)
        binding?.rvPopularProducts?.adapter = PopularProductAdapter()
    }

    override fun setHomeAdapter(adapter: HomeAdapter, onItemClick: (String) -> Unit) {
        homeAdapter = adapter
        adapter.onItemClicked = onItemClick
        binding?.rvHome?.adapter = homeAdapter
    }

    override fun notifyHomeAdapter(
        allBannerResponse: BannerResponse?,
        categoryResponse: CategoryResponse?,
        productList: ArrayList<ProductData>,
        cropResponse: CropResponse?,
        storeResponse: StoreResponse?,
        companyResponse: CompanyResponse?,
        cartList: List<CartItem>?,
    ) {
        homeAdapter?.notifyAdapterOnDataChange(allBannerResponse, categoryResponse, productList,
            cropResponse,
            storeResponse, companyResponse, cartList)
    }

    override fun setViewPagerAdapter(bannerList: List<Banner>?) {
        val adapter = ViewPagerAdapter(bannerList!!)
        binding?.viewPager?.adapter = adapter
        binding?.dotsIndicator?.attachTo(binding?.viewPager!!)
    }

    override fun setFeaturedProductsAdapter(
        adapter: ProductListAdapter,
        onProductListItemClick: (ProductData) -> Unit,
    ) {
        uiScope.launch {
            adapter.selectedProduct = onProductListItemClick
            binding?.rvFeatureProduct?.adapter = adapter
        }
    }

    override fun setCategoryAdapter(
        adapter: ShopByCategoryAdapter,
        onCategoryClick: (String) -> Unit,
    ) {
        adapter.itemClicked = onCategoryClick
        binding?.rvShopByCat?.adapter = adapter
    }

    override fun setCropAdapter(adapter: ShopByCropsAdapter, onItemClick: (String) -> Unit) {
        adapter.onItemClicked = onItemClick
        binding?.rvShopByCrops?.adapter = adapter
    }

    override fun setCompanyAdapter(adapter: ShopByCompanyAdapter, onItemClick: (String) -> Unit) {
        adapter.onItemClicked = onItemClick
        binding?.rvShopByCompany?.adapter = adapter
    }

    override fun setStoreAdapter(adapter: ShopByStoresAdapter, onItemClick: (String) -> Unit) {
        adapter.onItemClicked = onItemClick
        binding?.rvShopByStores?.adapter = adapter
    }

    override fun setExclusiveBannerAdapter(
        adapter: ExclusiveBannerAdapter,
        onItemClick: (String) -> Unit,
    ) {
        adapter.itemClicked = onItemClick
        binding?.rvExclusive?.adapter = adapter
    }

    override fun startProductDetailsActivity(it: ProductData) {
        val bundle = Bundle()
        bundle.putParcelable("product", it)
        openActivity(ProductDetailsActivity::class.java, bundle)
    }

    override fun openSubCategoryActivity(bundle: Bundle) {
        openActivity(SubCategoryActivity::class.java, bundle)
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

    /* private fun initializeYoutube() {
         binding?.ytPlayer?.initialize(
             "api_key",
             object : YouTubePlayer.OnInitializedListener {
                 // Implement two methods by clicking on red
                 // error bulb inside onInitializationSuccess
                 // method add the video link or the playlist
                 // link that you want to play In here we
                 // also handle the play and pause
                 // functionality
                 override fun onInitializationSuccess(
                     provider: YouTubePlayer.Provider,
                     youTubePlayer: YouTubePlayer, b: Boolean,
                 ) {
                     youTubePlayer.loadVideo("HzeK7g8cD0Y")
                     youTubePlayer.play()
                 }

                 // Inside onInitializationFailure
                 // implement the failure functionality
                 // Here we will show toast
                 override fun onInitializationFailure(
                     provider: YouTubePlayer.Provider,
                     youTubeInitializationResult: YouTubeInitializationResult,
                 ) {
                     Toast.makeText(activity,
                         "Video player Failed",
                         Toast.LENGTH_SHORT).show()
                 }
             })
     }*/
}