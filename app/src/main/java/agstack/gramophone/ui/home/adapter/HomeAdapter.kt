package agstack.gramophone.ui.home.adapter


import agstack.gramophone.R
import agstack.gramophone.databinding.*
import agstack.gramophone.ui.cart.model.CartItem
import agstack.gramophone.ui.cart.view.CartActivity
import agstack.gramophone.ui.home.cropdetail.CropDetailActivity
import agstack.gramophone.ui.home.featured.FeaturedProductActivity
import agstack.gramophone.ui.home.product.activity.ProductDetailsActivity
import agstack.gramophone.ui.home.shop.ShopByActivity
import agstack.gramophone.ui.home.shopbydetail.ShopByDetailActivity
import agstack.gramophone.ui.home.subcategory.SubCategoryActivity
import agstack.gramophone.ui.home.view.fragments.market.ProductListAdapter
import agstack.gramophone.ui.home.view.fragments.market.model.*
import agstack.gramophone.ui.offer.OfferDetailActivity
import agstack.gramophone.utils.Constants
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class HomeAdapter(
    private val homeScreenSequenceList: List<String>,
    private var allBannerResponse: BannerResponse?,
    private var categoryResponse: CategoryResponse?,
    private var productList: ArrayList<ProductData>,
    private var cropResponse: CropResponse?,
    private var storeResponse: StoreResponse?,
    private var companyResponse: CompanyResponse?,
    private var cartList: List<CartItem>?,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var onItemClicked: ((id: String) -> Unit)? = null

    fun notifyAdapterOnDataChange(
        allBannerResponse: BannerResponse?, categoryResponse: CategoryResponse?,
        productList: ArrayList<ProductData>, cropResponse: CropResponse?,
        storeResponse: StoreResponse?, companyResponse: CompanyResponse?,
        cartList: List<CartItem>?,
    ) {
        this.allBannerResponse = allBannerResponse
        this.categoryResponse = categoryResponse
        this.productList = productList
        this.cropResponse = cropResponse
        this.storeResponse = storeResponse
        this.companyResponse = companyResponse
        this.cartList = cartList
        notifyDataSetChanged()
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
        }
        return Banner1ViewHolder(
            ItemHomeBannerBinding.inflate(LayoutInflater.from(viewGroup.context))
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
                        ShopByCategoryAdapter(categoryResponse!!.gp_api_response_data.product_app_categories_list) {
                            openActivity(holder.itemView.context,
                                SubCategoryActivity::class.java,
                                Bundle().apply {
                                    putString(Constants.CATEGORY_ID,
                                        it)
                                })

                        }
                    holder.binding.rvShopByCat.adapter = categoryAdapter
                } else {
                    holder.binding.itemView.visibility = View.GONE
                }
            }
            is FeaturedProductsViewHolder -> {
                if (!productList.isNullOrEmpty()) {
                    holder.binding.itemView.visibility = View.VISIBLE
                    val featuredAdapter = ProductListAdapter(productList) {
                        openActivity(holder.itemView.context,
                            ProductDetailsActivity::class.java,
                            Bundle().apply {
                                putParcelable(Constants.PRODUCT,
                                    it)
                            })
                    }
                    holder.binding.rvFeatureProduct.adapter = featuredAdapter
                    holder.binding.viewAllFeaturedProduct.setOnClickListener {
                        openActivity(holder.itemView.context,
                            FeaturedProductActivity::class.java,
                            null)
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
                    val storeAdapter = ShopByStoresAdapter(tempStoreList) {
                        openActivity(holder.itemView.context,
                            ShopByDetailActivity::class.java,
                            Bundle().apply {
                                putString(Constants.SHOP_BY_TYPE, Constants.SHOP_BY_CROP)
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
                    val companyAdapter = ShopByCompanyAdapter(tempCompanyList) {
                        openActivity(holder.itemView.context,
                            FeaturedProductActivity::class.java,
                            null)
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
                                .load(exclusiveBanner[0])
                                .transition(DrawableTransitionOptions.withCrossFade())
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
        }
        return super.getItemViewType(position)
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
}