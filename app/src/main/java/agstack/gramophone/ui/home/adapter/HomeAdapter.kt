package agstack.gramophone.ui.home.adapter


import agstack.gramophone.BuildConfig
import agstack.gramophone.R
import agstack.gramophone.databinding.*
import agstack.gramophone.ui.articles.ArticlesWebViewActivity
import agstack.gramophone.ui.cart.model.CartItem
import agstack.gramophone.ui.cart.view.CartActivity
import agstack.gramophone.ui.farm.adapter.FarmAdapter
import agstack.gramophone.ui.farm.model.FarmResponse
import agstack.gramophone.ui.farm.view.*
import agstack.gramophone.ui.home.cropdetail.CropDetailActivity
import agstack.gramophone.ui.home.featured.FeaturedProductActivity
import agstack.gramophone.ui.home.product.activity.ProductDetailsActivity
import agstack.gramophone.ui.home.shop.ShopByActivity
import agstack.gramophone.ui.home.subcategory.SubCategoryActivity
import agstack.gramophone.ui.home.view.fragments.market.model.*
import agstack.gramophone.ui.weather.WeatherActivity
import agstack.gramophone.ui.weather.model.WeatherResponse
import agstack.gramophone.utils.Constants
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amnix.xtension.extensions.isNotNull
import com.amnix.xtension.extensions.isNotNullOrEmpty
import com.bumptech.glide.Glide
import java.lang.StringBuilder


class HomeAdapter(
    private val homeScreenSequenceList: List<String>,
    private var allBannerResponse: BannerResponse?,
    private var categoryResponse: CategoryResponse?,
    private var allProductsResponse: AllProductsResponse?,
    private var cropResponse: CropResponse?,
    private var storeResponse: StoreResponse?,
    private var companyResponse: CompanyResponse?,
    private var cartList: List<CartItem>?,
    private var farmResponse: FarmResponse?,
    private var articlesData: HashMap<String, ArrayList<FormattedArticlesData>>,
    private var weatherResponse: WeatherResponse?,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var onItemClicked: ((String) -> Unit)? = null

    fun notifyAdapterOnDataChange(
        allBannerResponse: BannerResponse?, categoryResponse: CategoryResponse?,
        allProductsResponse: AllProductsResponse?, cropResponse: CropResponse?,
        storeResponse: StoreResponse?, companyResponse: CompanyResponse?,
        cartList: List<CartItem>?, farmResponse: FarmResponse?,
        articlesData: HashMap<String, ArrayList<FormattedArticlesData>>,
        weatherResponse: WeatherResponse?,
    ) {

    }

    fun notifyBannerItemInserted(allBannerResponse: BannerResponse?, position: Int) {
        this.allBannerResponse = allBannerResponse
        notifyItemChanged(position)
    }

    fun notifyCategoryItemInserted(categoryResponse: CategoryResponse?, position: Int) {
        this.categoryResponse = categoryResponse
        notifyItemChanged(position)
    }

    fun notifyFeaturedItemInserted(allProductsResponse: AllProductsResponse?, position: Int) {
        this.allProductsResponse = allProductsResponse
        notifyItemChanged(position)
    }

    fun notifyCropItemInserted(cropResponse: CropResponse?, position: Int) {
        this.cropResponse = cropResponse
        notifyItemChanged(position)
    }

    fun notifyStoreItemInserted(storeResponse: StoreResponse?, position: Int) {
        this.storeResponse = storeResponse
        notifyItemChanged(position)
    }

    fun notifyCompanyItemInserted(companyResponse: CompanyResponse?, position: Int) {
        this.companyResponse = companyResponse
        notifyItemChanged(position)
    }

    fun notifyCartItemInserted(cartList: List<CartItem>?, position: Int) {
        this.cartList = cartList
        notifyItemChanged(position)
    }

    fun notifyFarmItemInserted(farmResponse: FarmResponse?, position: Int) {
        this.farmResponse = farmResponse
        notifyItemChanged(position)
    }

    fun notifyArticleItemInserted(articlesData: HashMap<String, ArrayList<FormattedArticlesData>>, position: Int) {
        this.articlesData = articlesData
        notifyItemChanged(position)
    }

    fun notifyWeatherItemInserted(weatherResponse: WeatherResponse?, position: Int) {
        this.weatherResponse = weatherResponse
        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            Constants.HOME_BANNER_VIEW_TYPE -> {
                return Banner1ViewHolder(ItemHomeBannerBinding.inflate(LayoutInflater.from(viewGroup.context)))
            }
            Constants.HOME_BANNER_EXCLUSIVE_VIEW_TYPE -> {
                return ExclusiveBannerViewHolder(ItemHomeExclusiveBannerBinding.inflate(
                    LayoutInflater.from(
                        viewGroup.context)))
            }
            Constants.HOME_BANNER_REFERRAL_VIEW_TYPE -> {
                return ReferralBannerViewHolder(ItemHomeBannerBinding.inflate(
                    LayoutInflater.from(
                        viewGroup.context)))
            }
            Constants.HOME_SHOP_BY_CATEGORY_VIEW_TYPE -> {
                return ShopByCategoryViewHolder(ItemHomeShopByCategoryBinding.inflate(LayoutInflater.from(
                    viewGroup.context)))
            }
            Constants.HOME_FEATURED_PRODUCTS_VIEW_TYPE -> {
                return FeaturedProductsViewHolder(ItemHomeFeaturedProductBinding.inflate(
                    LayoutInflater.from(viewGroup.context)))
            }
            Constants.HOME_SHOP_BY_CROP_VIEW_TYPE -> {
                return ShopByCropViewHolder(ItemHomeShopByCropBinding.inflate(LayoutInflater.from(
                    viewGroup.context)))
            }
            Constants.HOME_SHOP_BY_STORE_VIEW_TYPE -> {
                return ShopByStoreViewHolder(ItemHomeShopByStoreBinding.inflate(LayoutInflater.from(
                    viewGroup.context)))
            }
            Constants.HOME_SHOP_BY_COMPANY_VIEW_TYPE -> {
                return ShopByCompanyViewHolder(ItemHomeShopByCompanyBinding.inflate(LayoutInflater.from(
                    viewGroup.context)))
            }
            Constants.HOME_GRAMOPHONE_PROMISE_VIEW_TYPE -> {
                return PromiseBannerViewHolder(ItemHomePromiseBannerBinding.inflate(LayoutInflater.from(
                    viewGroup.context)))
            }
            Constants.HOME_CART_VIEW_TYPE -> {
                return CartViewHolder(ItemHomeCartBinding.inflate(LayoutInflater.from(
                    viewGroup.context)))
            }
            Constants.HOME_FARMS_VIEW_TYPE -> {
                return FarmsViewHolder(ItemHomeFarmsViewBinding.inflate(LayoutInflater.from(
                    viewGroup.context)))
            }
            Constants.HOME_ARTICLES_VIEW_TYPE -> {
                return ArticlesViewHolder(ItemHomeArticlesBinding.inflate(LayoutInflater.from(
                    viewGroup.context)))
            }
            Constants.HOME_COMMUNITY_VIEW_TYPE -> {
                return CommunityViewHolder(ItemHomeCommunityBinding.inflate(LayoutInflater.from(
                    viewGroup.context)))
            }
            Constants.HOME_WEATHER_VIEW_TYPE -> {
                return WeatherViewHolder(ItemHomeWeatherBinding.inflate(LayoutInflater.from(
                    viewGroup.context)))
            }
        }
        return EmptyViewHolder(
            ItemHomeEmptyBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, i: Int) {
        when (holder) {
            is Banner1ViewHolder -> {
                if (allBannerResponse?.gpApiResponseData?.homeBanner1 != null
                    && allBannerResponse!!.gpApiResponseData?.homeBanner1?.isNotEmpty() == true
                ) {
                    holder.binding.itemView.visibility = View.VISIBLE
                    holder.binding.viewPager.adapter =
                        ViewPagerAdapter(allBannerResponse!!.gpApiResponseData?.homeBanner1!!)
                    holder.binding.dotsIndicator.attachTo(holder.binding.viewPager)
                } else {
                    holder.binding.itemView.visibility = View.GONE
                }
            }
            is ShopByCategoryViewHolder -> {
                if (categoryResponse?.gp_api_response_data?.product_app_categories_list != null
                    && categoryResponse!!.gp_api_response_data.product_app_categories_list.isNotEmpty()
                ) {
                    holder.binding.itemView.visibility = View.VISIBLE
                    val categoryAdapter =
                        ShopByCategoryAdapter(categoryResponse!!.gp_api_response_data.product_app_categories_list) { id, name, image ->
                            openActivity(holder.itemView.context,
                                SubCategoryActivity::class.java,
                                Bundle().apply {
                                    putString(Constants.CATEGORY_ID, id)
                                    putString(Constants.CATEGORY_NAME, name)
                                    putString(Constants.CATEGORY_IMAGE, image)
                                })

                        }
                    holder.binding.rvShopByCat.adapter = categoryAdapter
                } else {
                    holder.binding.itemView.visibility = View.GONE
                }
            }
            is FeaturedProductsViewHolder -> {
                val productList = ArrayList<Data>()
                if (allProductsResponse?.gp_api_response_data?.data != null
                    && allProductsResponse!!.gp_api_response_data.data.isNotEmpty()
                ) {
                    holder.binding.itemView.visibility = View.VISIBLE

                    productList.addAll(allProductsResponse?.gp_api_response_data?.data!!)

                    val tempProductList: List<Data> = if (productList.size >= 4)
                        productList.subList(0, 4)
                    else productList

                    val featuredAdapter = FeaturedProductListAdapter(tempProductList) {
                        val productData = ProductData()
                        productData.product_id = it
                        openActivity(holder.itemView.context,
                            ProductDetailsActivity::class.java,
                            Bundle().apply {
                                putParcelable(Constants.PRODUCT,
                                    productData)
                            })
                    }
                    holder.binding.rvFeatureProduct.adapter = featuredAdapter
                    holder.binding.viewAllFeaturedProduct.setOnClickListener {
                        openActivity(holder.itemView.context,
                            FeaturedProductActivity::class.java,
                            Bundle().apply {
                                putString(Constants.HOME_FEATURED_PRODUCTS,
                                    Constants.HOME_FEATURED_PRODUCTS)
                            })
                    }
                } else {
                    holder.binding.itemView.visibility = View.GONE
                }
            }
            is ShopByCropViewHolder -> {
                val cropList = ArrayList<CropData>()
                if (cropResponse?.gpApiResponseData?.cropsList != null
                    && cropResponse!!.gpApiResponseData?.cropsList?.isNotEmpty() == true
                ) {
                    holder.binding.itemView.visibility = View.VISIBLE
                    cropList.addAll(cropResponse!!.gpApiResponseData?.cropsList!!)

                    val tempCropList: List<CropData> = if (cropList.size >= 9)
                        cropList.subList(0, 9)
                    else cropList
                    val cropAdapter = ShopByCropsAdapter(tempCropList) {
                        openActivity(holder.itemView.context,
                            CropDetailActivity::class.java,
                            Bundle().apply {
                                putString(Constants.SHOP_BY_TYPE, Constants.SHOP_BY_CROP)
                            })
                    }
                    holder.binding.rvShopByCrops.adapter = cropAdapter
                    holder.binding.viewAllCrops.setOnClickListener {
                        openActivity(holder.itemView.context,
                            ShopByActivity::class.java,
                            Bundle().apply {
                                putString(Constants.SHOP_BY_TYPE, Constants.SHOP_BY_CROP)
                                putParcelable(Constants.SHOP_BY_CROP,
                                    cropResponse)
                            })
                    }
                } else {
                    holder.binding.itemView.visibility = View.GONE
                }
            }
            is ShopByStoreViewHolder -> {
                val storeList = ArrayList<StoreData>()
                if (storeResponse?.gpApiResponseData?.storeList != null
                    && storeResponse!!.gpApiResponseData?.storeList?.isNotEmpty() == true
                ) {
                    holder.binding.itemView.visibility = View.VISIBLE
                    storeList.addAll(storeResponse!!.gpApiResponseData?.storeList!!)

                    val tempStoreList: List<StoreData> = if (storeList.size >= 4)
                        storeList.subList(0, 4)
                    else storeList
                    val storeAdapter = ShopByStoresAdapter(tempStoreList) { id, name, image ->
                        openActivity(holder.itemView.context,
                            FeaturedProductActivity::class.java,
                            Bundle().apply {
                                putString(Constants.STORE_ID, id)
                                putString(Constants.STORE_NAME, name)
                                putString(Constants.STORE_IMAGE, image)
                            })
                    }
                    holder.binding.rvShopByStore.adapter = storeAdapter
                    holder.binding.viewAllStores.setOnClickListener {
                        openActivity(holder.itemView.context,
                            ShopByActivity::class.java,
                            Bundle().apply {
                                putString(Constants.SHOP_BY_TYPE, Constants.SHOP_BY_STORE)
                                putParcelable(Constants.SHOP_BY_STORE, storeResponse)
                            })
                    }
                } else {
                    holder.binding.itemView.visibility = View.GONE
                }
            }
            is ShopByCompanyViewHolder -> {
                val companyList = ArrayList<CompanyData>()
                if (companyResponse?.gpApiResponseData?.companiesList != null
                    && companyResponse!!.gpApiResponseData?.companiesList?.isNotEmpty() == true
                ) {
                    holder.binding.itemView.visibility = View.VISIBLE
                    companyList.addAll(companyResponse!!.gpApiResponseData?.companiesList!!)

                    val tempCompanyList: List<CompanyData> = if (companyList.size >= 6)
                        companyList.subList(0, 6)
                    else companyList
                    val companyAdapter = ShopByCompanyAdapter(tempCompanyList) { id, name, image ->
                        openActivity(holder.itemView.context,
                            FeaturedProductActivity::class.java,
                            Bundle().apply {
                                putString(Constants.COMPANY_ID, id)
                                putString(Constants.COMPANY_NAME, name)
                                putString(Constants.COMPANY_IMAGE, image)
                            })
                    }
                    holder.binding.rvShopByCompany.adapter = companyAdapter
                    holder.binding.viewAllCompanies.setOnClickListener {
                        openActivity(holder.itemView.context,
                            ShopByActivity::class.java,
                            Bundle().apply {
                                putString(Constants.SHOP_BY_TYPE, Constants.SHOP_BY_COMPANY)
                                putParcelable(Constants.SHOP_BY_COMPANY,
                                    companyResponse)
                            })
                    }
                } else {
                    holder.binding.itemView.visibility = View.GONE
                }
            }
            is ExclusiveBannerViewHolder -> {
                if (allBannerResponse?.gpApiResponseData?.homeGramophoneExclusive != null
                    && allBannerResponse!!.gpApiResponseData?.homeGramophoneExclusive?.isNotEmpty() == true
                ) {
                    holder.binding.itemView.visibility = View.VISIBLE
                    val exclusiveBanner =
                        allBannerResponse!!.gpApiResponseData?.homeGramophoneExclusive
                    val tempBanner: List<Banner>
                    if (exclusiveBanner?.size!! > 0) {
                        if (exclusiveBanner.size % 2 == 0) {
                            holder.binding.ivExclusiveBanner.visibility = View.GONE
                            tempBanner = exclusiveBanner.subList(0, exclusiveBanner.size)
                        } else {
                            holder.binding.ivExclusiveBanner.visibility = View.VISIBLE
                            tempBanner = exclusiveBanner.subList(1, exclusiveBanner.size)
                            Glide.with(holder.itemView.context)
                                .load(exclusiveBanner[0].bannerImage)
                                .into(holder.binding.ivExclusiveBanner)
                        }
                        val exclusiveBannerAdapter = ExclusiveBannerAdapter(tempBanner) {
                            /* Do anything on banner click */
                        }
                        holder.binding.rvExclusive.adapter = exclusiveBannerAdapter
                    }
                } else {
                    holder.binding.itemView.visibility = View.GONE
                }
            }
            is ReferralBannerViewHolder -> {
                if (allBannerResponse?.gpApiResponseData?.homeReferralBanner != null
                    && allBannerResponse!!.gpApiResponseData?.homeReferralBanner?.isNotEmpty() == true
                ) {
                    holder.binding.itemView.visibility = View.VISIBLE
                    val referralBanner = allBannerResponse!!.gpApiResponseData?.homeReferralBanner!!
                    /*val referralBannerAdapter = ReferralBannerAdapter(referralBanner) {
                    *//* Do anything on banner click *//*
                }
                holder.binding.rvReferral.adapter = referralBannerAdapter*/

                    holder.binding.viewPager.adapter =
                        ViewPagerAdapter(referralBanner)
                    holder.binding.rlDotsIndicator.visibility = View.GONE
                } else {
                    holder.binding.itemView.visibility = View.GONE
                }
            }
            is PromiseBannerViewHolder -> {
                // do nothing for this view holder
            }
            is CartViewHolder -> {
                if (!cartList.isNullOrEmpty()) {
                    holder.binding.itemView.visibility = View.VISIBLE
                    holder.binding.tvCartItemCount.text =
                        holder.itemView.context.getString(R.string.products_in_your_cart).plus(" (")
                            .plus(cartList?.size).plus(")")
                    holder.binding.rvCart.adapter = RecentlyViewedAdapter(cartList) {
                        openActivity(holder.itemView.context,
                            ProductDetailsActivity::class.java,
                            Bundle().apply {
                                putParcelable(Constants.PRODUCT,
                                    ProductData(it.toInt()))
                            })
                    }
                    holder.binding.frameCheckout.setOnClickListener {
                        openActivity(holder.itemView.context, CartActivity::class.java, null)
                    }
                } else {
                    holder.binding.itemView.visibility = View.GONE
                }
            }
            is FarmsViewHolder -> {
                holder.binding.itemView.visibility = View.VISIBLE

                var farmList: List<List<agstack.gramophone.ui.farm.model.Data>>? = null
                var isCustomerFarms = false
                if (farmResponse?.gp_api_response_data?.customer_farm != null &&
                    farmResponse?.gp_api_response_data?.customer_farm?.data?.isNotNullOrEmpty() == true
                ){
                    farmList = farmResponse?.gp_api_response_data?.customer_farm?.data
                    isCustomerFarms = true
                }else if (farmResponse?.gp_api_response_data?.model_farm != null &&
                    farmResponse?.gp_api_response_data?.model_farm?.data?.isNotNullOrEmpty() == true
                ) {
                    farmList = farmResponse?.gp_api_response_data?.model_farm?.data
                    isCustomerFarms = false
                }else{
                    holder.binding.itemView.visibility = View.VISIBLE
                }

                if (farmList != null && farmList.isNotEmpty()) {
                    holder.binding.rvFarms.adapter = FarmAdapter(
                        farmList,
                        headerListener = {

                        },
                        contentListener = {
                            openActivity(
                                holder.binding.itemView.context,
                                CropGroupExplorerActivity::class.java,
                                Bundle().apply {
                                    putBoolean("isCustomerFarm", isCustomerFarms)
                                    putParcelableArrayList(
                                        "cropList",
                                        it as ArrayList<agstack.gramophone.ui.farm.model.Data>
                                    )
                                })
                        },
                        footerListener = {
                            val selectedCrop = CropData(
                                cropId = it[0].crop_id,
                                cropName = it[0].crop_name,
                                cropImage = it[0].crop_image,
                            )
                            openActivity(
                                holder.binding.itemView.context,
                                AddFarmActivity::class.java,
                                Bundle().apply {
                                    putParcelable("selectedCrop", selectedCrop)
                                })
                        },
                    )
                }

                holder.binding.viewAllFarms.setOnClickListener {
                    openActivity(holder.itemView.context, ViewAllFarmsActivity::class.java, null)
                }

                holder.binding.addFarmWrapper.addFarmTitleLayout.setOnClickListener {
                    openActivity(holder.itemView.context, SelectCropActivity::class.java, null)
                }

                holder.binding.addFarmWrapper.txtWhyAddFarm.setOnClickListener {
                    openActivity(holder.itemView.context, WhyAddFarmActivity::class.java, null)
                }
            }
            is ArticlesViewHolder -> {
                if (articlesData.size > 0) {
                    if (articlesData.containsKey(Constants.FEATURED_ARTICLES) && articlesData[Constants.FEATURED_ARTICLES].isNotNullOrEmpty()) {
                        holder.binding.itemViewFeatured.visibility = View.VISIBLE
                        holder.binding.rvFeaturedArticles.adapter =
                            ArticlesAdapter(articlesData[Constants.FEATURED_ARTICLES]!!) {
                                openActivity(
                                    holder.binding.viewAllArticles.context,
                                    ArticlesWebViewActivity::class.java,
                                    Bundle().apply {
                                        putString(Constants.PAGE_URL,
                                            BuildConfig.BASE_URL_SINGLE_ARTICLE + "/" + it)
                                    }
                                )
                            }
                    } else {
                        holder.binding.itemViewFeatured.visibility = View.GONE
                    }
                    if (articlesData.containsKey(Constants.TRENDING_ARTICLES) && articlesData[Constants.TRENDING_ARTICLES].isNotNullOrEmpty()) {
                        holder.binding.itemViewTrending.visibility = View.VISIBLE
                        holder.binding.rvTrendingArticles.adapter =
                            ArticlesAdapter(articlesData[Constants.TRENDING_ARTICLES]!!) {
                                openActivity(
                                    holder.binding.viewAllArticles.context,
                                    ArticlesWebViewActivity::class.java,
                                    Bundle().apply {
                                        putString(Constants.PAGE_URL,
                                            BuildConfig.BASE_URL_SINGLE_ARTICLE + "/" + it)
                                    }
                                )
                            }
                    } else {
                        holder.binding.itemViewTrending.visibility = View.GONE
                    }
                    if (articlesData.containsKey(Constants.SUGGESTED_ARTICLES) && articlesData[Constants.SUGGESTED_ARTICLES].isNotNullOrEmpty()) {
                        holder.binding.itemViewSuggested.visibility = View.VISIBLE
                        holder.binding.rvSuggestedArticles.adapter =
                            ArticlesAdapter(articlesData[Constants.SUGGESTED_ARTICLES]!!) {
                                openActivity(
                                    holder.binding.viewAllArticles.context,
                                    ArticlesWebViewActivity::class.java,
                                    Bundle().apply {
                                        putString(Constants.PAGE_URL,
                                            BuildConfig.BASE_URL_SINGLE_ARTICLE + "/" + it)
                                    }
                                )
                            }
                    } else {
                        holder.binding.itemViewSuggested.visibility = View.GONE
                    }
                    holder.binding.viewAllArticles.setOnClickListener {
                        openActivity(
                            holder.binding.viewAllArticles.context,
                            ArticlesWebViewActivity::class.java,
                            Bundle().apply {
                                putString(Constants.PAGE_URL, BuildConfig.BASE_URL_ARTICLES)
                            }
                        )
                    }
                    holder.binding.viewAllArticles.visibility = View.VISIBLE
                    holder.binding.view.visibility = View.VISIBLE
                } else {
                    holder.binding.itemViewFeatured.visibility = View.GONE
                    holder.binding.itemViewTrending.visibility = View.GONE
                    holder.binding.itemViewSuggested.visibility = View.GONE
                    holder.binding.viewAllArticles.visibility = View.GONE
                    holder.binding.view.visibility = View.GONE
                }
            }
            is CommunityViewHolder -> {
                holder.binding.rlGoToCommunity.setOnClickListener {
                    onItemClicked?.invoke("")
                }
            }
            is WeatherViewHolder -> {
                if (weatherResponse.isNotNull() && weatherResponse?.gp_api_response_data.isNotNullOrEmpty()) {
                    holder.binding.itemView.visibility = View.VISIBLE
                    val data = weatherResponse?.gp_api_response_data?.get(0)
                    when {
                        data?.address.isNotNullOrEmpty() -> holder.binding.tvLocation.text =
                            data?.address
                        else -> holder.binding.tvLocation.text = holder.binding.tvLocation.context.getString(R.string.default_city_name)
                    }
                    val temp = StringBuilder("")
                    if (data?.temperature.isNotNull()) {
                        if (data?.temperature?.weather_condition.isNotNullOrEmpty())
                            temp.append(data?.temperature?.weather_condition)
                        if (data?.temperature?.min.isNotNullOrEmpty())
                            temp.append("-").append(data?.temperature?.min)
                                .append(holder.binding.itemView.context.getString(R.string.degee_temp_centigrade_lower_case))
                        if (data?.temperature?.max.isNotNullOrEmpty())
                            temp.append("-").append(data?.temperature?.max)
                                .append(holder.binding.itemView.context.getString(R.string.degee_temp_centigrade_lower_case))
                        holder.binding.tvWeather.text = temp.toString()
                        if (data?.temperature?.weather_icon.isNotNullOrEmpty())
                            Glide.with(holder.itemView.context)
                                .load(data?.temperature?.weather_icon)
                                .into(holder.binding.ivWeather)
                    }

                } else {
                    holder.binding.itemView.visibility = View.GONE
                }
                holder.binding.itemView.setOnClickListener {
                    openActivity(
                        holder.binding.itemView.context,
                        WeatherActivity::class.java,
                        null
                    )
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        when (homeScreenSequenceList[position]) {
            Constants.HOME_BANNER_1 -> {
                return Constants.HOME_BANNER_VIEW_TYPE
            }
            Constants.HOME_BANNER_EXCLUSIVE -> {
                return Constants.HOME_BANNER_EXCLUSIVE_VIEW_TYPE
            }
            Constants.HOME_BANNER_REFERRAL -> {
                return Constants.HOME_BANNER_REFERRAL_VIEW_TYPE
            }
            Constants.HOME_SHOP_BY_CATEGORY -> {
                return Constants.HOME_SHOP_BY_CATEGORY_VIEW_TYPE
            }
            Constants.HOME_FEATURED_PRODUCTS -> {
                return Constants.HOME_FEATURED_PRODUCTS_VIEW_TYPE
            }
            Constants.HOME_SHOP_BY_CROP -> {
                return Constants.HOME_SHOP_BY_CROP_VIEW_TYPE
            }
            Constants.HOME_SHOP_BY_STORE -> {
                return Constants.HOME_SHOP_BY_STORE_VIEW_TYPE
            }
            Constants.HOME_SHOP_BY_COMPANY -> {
                return Constants.HOME_SHOP_BY_COMPANY_VIEW_TYPE
            }
            Constants.HOME_GRAMOPHONE_PROMISE -> {
                return Constants.HOME_GRAMOPHONE_PROMISE_VIEW_TYPE
            }
            Constants.HOME_CART -> {
                return Constants.HOME_CART_VIEW_TYPE
            }
            Constants.HOME_FARMS -> {
                return Constants.HOME_FARMS_VIEW_TYPE
            }
            Constants.HOME_ARTICLES -> {
                return Constants.HOME_ARTICLES_VIEW_TYPE
            }
            Constants.HOME_COMMUNITY -> {
                return Constants.HOME_COMMUNITY_VIEW_TYPE
            }
            Constants.HOME_WEATHER -> {
                return Constants.HOME_WEATHER_VIEW_TYPE
            }
        }
        return Constants.HOME_EMPTY_VIEW_TYPE
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return homeScreenSequenceList.size
    }

    fun <T> openActivity(context: Context, cls: Class<T>, extras: Bundle?) {
        Intent(context, cls).apply {
            if (extras != null)
                putExtras(extras)
            context.startActivity(this)
        }
    }

    inner class EmptyViewHolder(var binding: ItemHomeEmptyBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class Banner1ViewHolder(var binding: ItemHomeBannerBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class ShopByCategoryViewHolder(var binding: ItemHomeShopByCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class FeaturedProductsViewHolder(var binding: ItemHomeFeaturedProductBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class ShopByCropViewHolder(var binding: ItemHomeShopByCropBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class ShopByStoreViewHolder(var binding: ItemHomeShopByStoreBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class ShopByCompanyViewHolder(var binding: ItemHomeShopByCompanyBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class ExclusiveBannerViewHolder(var binding: ItemHomeExclusiveBannerBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class ReferralBannerViewHolder(var binding: ItemHomeBannerBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class PromiseBannerViewHolder(var binding: ItemHomePromiseBannerBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class CartViewHolder(var binding: ItemHomeCartBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class FarmsViewHolder(var binding: ItemHomeFarmsViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class ArticlesViewHolder(var binding: ItemHomeArticlesBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class CommunityViewHolder(var binding: ItemHomeCommunityBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class WeatherViewHolder(var binding: ItemHomeWeatherBinding) :
        RecyclerView.ViewHolder(binding.root)
}