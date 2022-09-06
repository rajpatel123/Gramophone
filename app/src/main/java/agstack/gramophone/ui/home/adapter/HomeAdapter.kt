package agstack.gramophone.ui.home.adapter


import agstack.gramophone.databinding.*
import agstack.gramophone.ui.home.cropdetail.CropDetailActivity
import agstack.gramophone.ui.home.featured.FeaturedProductActivity
import agstack.gramophone.ui.home.product.activity.ProductDetailsActivity
import agstack.gramophone.ui.home.shop.ShopByActivity
import agstack.gramophone.ui.home.shopbydetail.ShopByDetailActivity
import agstack.gramophone.ui.home.subcategory.SubCategoryActivity
import agstack.gramophone.ui.home.view.fragments.market.ProductListAdapter
import agstack.gramophone.ui.home.view.fragments.market.model.*
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
    private val allBannerResponse: BannerResponse?,
    private val categoryResponse: CategoryResponse?,
    private val productList: ArrayList<ProductData>,
    private val cropResponse: CropResponse?,
    private val storeResponse: StoreResponse?,
    private val companyResponse: CompanyResponse?,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var onItemClicked: ((id: String) -> Unit)? = null

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
        }
        return Banner1ViewHolder(
            ItemHomeBannerBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, i: Int) {
        when (holder) {
            is Banner1ViewHolder -> {
                holder.binding.viewPager.adapter =
                    ViewPagerAdapter(allBannerResponse?.gpApiResponseData?.homeBanner1!!)
                holder.binding.dotsIndicator.attachTo(holder.binding.viewPager)
            }
            is ShopByCategoryViewHolder -> {
                val categoryAdapter =
                    ShopByCategoryAdapter(categoryResponse?.gp_api_response_data?.product_app_categories_list) {
                        openActivity(holder.itemView.context,
                            SubCategoryActivity::class.java,
                            Bundle().apply {
                                putString(Constants.CATEGORY_ID,
                                    it)
                            })

                    }
                holder.binding.rvShopByCat.adapter = categoryAdapter
            }
            is FeaturedProductsViewHolder -> {
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
                    openActivity(holder.itemView.context, FeaturedProductActivity::class.java, null)
                }
            }
            is ShopByCropViewHolder -> {
                val cropList = ArrayList<CropData>()
                cropList.addAll(cropResponse?.gpApiResponseData?.cropsList!!)

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
            }
            is ShopByStoreViewHolder -> {
                val storeList = ArrayList<StoreData>()
                storeList.addAll(storeResponse?.gpApiResponseData?.storeList!!)

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
            }
            is ShopByCompanyViewHolder -> {
                val companyList = ArrayList<CompanyData>()
                companyList.addAll(companyResponse?.gpApiResponseData?.companiesList!!)

                val tempCompanyList: List<CompanyData> = if (companyList.size >= 6)
                    companyList.subList(0, 6)
                else companyList
                val companyAdapter = ShopByCompanyAdapter(tempCompanyList) {
                    openActivity(holder.itemView.context, FeaturedProductActivity::class.java, null)
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
            }
            is ExclusiveBannerViewHolder -> {
                val exclusiveBanner = allBannerResponse?.gpApiResponseData?.homeGramophoneExclusive
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
            }
            is ReferralBannerViewHolder -> {
                val referralBanner = allBannerResponse?.gpApiResponseData?.homeReferralBanner!!
                /*val referralBannerAdapter = ReferralBannerAdapter(referralBanner) {
                    *//* Do anything on banner click *//*
                }
                holder.binding.rvReferral.adapter = referralBannerAdapter*/

                holder.binding.viewPager.adapter =
                    ViewPagerAdapter(referralBanner)
                holder.binding.dotsIndicator.attachTo(holder.binding.viewPager)
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
}