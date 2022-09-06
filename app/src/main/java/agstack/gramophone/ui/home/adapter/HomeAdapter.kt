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
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

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
            0 -> {
                return Banner1ViewHolder(ItemHomeBannerBinding.inflate(LayoutInflater.from(viewGroup.context)))
            }
            1 -> {
                return ShopByCategoryViewHolder(ItemHomeShopByCategoryBinding.inflate(LayoutInflater.from(
                    viewGroup.context)))
            }
            2 -> {
                return FeaturedProductsViewHolder(ItemHomeFeaturedProductBinding.inflate(
                    LayoutInflater.from(viewGroup.context)))
            }
            3 -> {
                return ShopByCropViewHolder(ItemHomeShopByCropBinding.inflate(LayoutInflater.from(
                    viewGroup.context)))
            }
            4 -> {
                return ShopByStoreViewHolder(ItemHomeShopByStoreBinding.inflate(LayoutInflater.from(
                    viewGroup.context)))
            }
            5 -> {
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
        }
    }

    override fun getItemViewType(position: Int): Int {
        when (homeScreenSequenceList[position]) {
            Constants.HOME_BANNER_1 -> {
                return 0
            }
            Constants.HOME_SHOP_BY_CATEGORY -> {
                return 1
            }
            Constants.HOME_FEATURED_PRODUCTS -> {
                return 2
            }
            Constants.HOME_SHOP_BY_CROP -> {
                return 3
            }
            Constants.HOME_SHOP_BY_STORE -> {
                return 4
            }
            Constants.HOME_SHOP_BY_COMPANY -> {
                return 5
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
}