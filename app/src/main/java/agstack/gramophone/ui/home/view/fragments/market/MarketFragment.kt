package agstack.gramophone.ui.home.view.fragments.market

import agstack.gramophone.R
import agstack.gramophone.base.BaseFragment
import agstack.gramophone.databinding.FragmentMarketBinding
import agstack.gramophone.ui.home.adapter.*
import agstack.gramophone.ui.home.model.Banner
import agstack.gramophone.ui.home.viewmodel.HomeViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MarketFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MarketFragment : BaseFragment<FragmentMarketBinding,MarketFragmentNavigator,MarketFragmentViewModel>(),MarketFragmentNavigator {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var items: ArrayList<Banner>
    private val marketFragmentViewModel: MarketFragmentViewModel by viewModels()

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
        savedInstanceState: Bundle?
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
    }

    private fun setUpUI() {
        initCards()

        binding?.dotsIndicator?.setOnClickListener { }
        val adapter = ViewPagerAdapter(items)
        binding?.viewPager?.adapter = adapter
        binding?.dotsIndicator?.attachTo(binding?.viewPager!!)

        binding?.rvShopByCat?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding?.rvShopByCat?.setHasFixedSize(true)
        binding?.rvShopByCat?.adapter = ShopByCategoryAdapter()

        binding?.rvFeatureProduct?.layoutManager = GridLayoutManager(activity, 2)
        binding?.rvFeatureProduct?.setHasFixedSize(true)
        binding?.rvFeatureProduct?.adapter = FeatureProductAdapter()

        binding?.rvMandiRates?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding?.rvMandiRates?.setHasFixedSize(true)
        binding?.rvMandiRates?.adapter = MandiRatesAdapter()

        binding?.rvShopByCrops?.layoutManager = GridLayoutManager(activity, 3)
        binding?.rvShopByCrops?.setHasFixedSize(true)
        binding?.rvShopByCrops?.adapter = ShopByCropsAdapter()

        binding?.rvShopByStores?.layoutManager = GridLayoutManager(activity, 2)
        binding?.rvShopByStores?.setHasFixedSize(true)
        binding?.rvShopByStores?.adapter = ShopByStoresAdapter()

        binding?.rvRecentlyViewed?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding?.rvRecentlyViewed?.setHasFixedSize(true)
        binding?.rvRecentlyViewed?.adapter = RecentlyViewedAdapter()

        binding?.rvCart?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding?.rvCart?.setHasFixedSize(true)
        binding?.rvCart?.adapter = RecentlyViewedAdapter()

        binding?.rvArticles?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding?.rvArticles?.setHasFixedSize(true)
        binding?.rvArticles?.adapter = ArticlesAdapter()

        binding?.rvPopularProducts?.layoutManager = GridLayoutManager(activity, 2)
        binding?.rvPopularProducts?.setHasFixedSize(true)
        binding?.rvPopularProducts?.adapter = PopularProductAdapter()
    }

    private fun initCards() {
        items = ArrayList<Banner>()

        val cardConnected = Banner(
            R.drawable.dummy_banner,
            getString(R.string.connected),
            getString(R.string.connected_sub_msg)
        )
        items.add(cardConnected)

        val cardDelivery = Banner(
            R.drawable.dummy_banner_2,
            getString(R.string.delivery),
            getString(R.string.delivery_sub_msg)
        )
        items.add(cardDelivery)

        val cardUpdates = Banner(
            R.drawable.dummy_banner,
            getString(R.string.midea),
            getString(R.string.midea_sub_msg)
        )
        items.add(cardUpdates)
    }
}