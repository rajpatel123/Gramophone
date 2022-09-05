package agstack.gramophone.ui.home.adapter


import agstack.gramophone.databinding.*
import agstack.gramophone.ui.home.subcategory.SubCategoryActivity
import agstack.gramophone.ui.home.view.fragments.market.ProductListAdapter
import agstack.gramophone.ui.home.view.fragments.market.model.*
import agstack.gramophone.utils.Constants
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class HomeAdapter(
    private val homeScreenSequenceList: List<String>,
    val allBannerResponse: BannerResponse?,
    val categoryResponse: CategoryResponse?,
    val productList: ArrayList<ProductData>,
    val cropResponse: CropResponse?,
    val storeResponse: StoreResponse?,
    val companyResponse: CompanyResponse?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var onItemClicked: ((id: String) -> Unit)? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType) {
            0->{
                return Banner1ViewHolder(
                    ItemHomeBannerBinding.inflate(LayoutInflater.from(viewGroup.context))
                )
            }
            1->{
                return ShopByCategoryViewHolder(
                    ItemHomeShopByCategoryBinding.inflate(LayoutInflater.from(viewGroup.context))
                )
            }
            2->{
                return FeaturedProductsViewHolder(
                    ItemHomeFeaturedProductBinding.inflate(LayoutInflater.from(viewGroup.context))
                )
            }
            3->{
                return ShopByCropViewHolder(
                    ItemHomeShopByCropBinding.inflate(LayoutInflater.from(viewGroup.context))
                )
            }
            4->{
                return ShopByStoreViewHolder(
                    ItemHomeShopByStoreBinding.inflate(LayoutInflater.from(viewGroup.context))
                )
            }
            5->{
                return ShopByCompanyViewHolder(
                    ItemHomeShopByCompanyBinding.inflate(LayoutInflater.from(viewGroup.context))
                )
            }
        }
        return Banner1ViewHolder(
            ItemHomeBannerBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, i: Int) {
        when (holder) {
            is Banner1ViewHolder -> {
                holder.binding.viewPager.adapter = ViewPagerAdapter(allBannerResponse?.gpApiResponseData?.homeBanner1!!)
                holder.binding.dotsIndicator.attachTo(holder.binding.viewPager)
            }
            is ShopByCategoryViewHolder -> {
                val categoryAdapter = ShopByCategoryAdapter(categoryResponse?.gp_api_response_data?.product_app_categories_list){ it ->

                    /*openActivity(SubCategoryActivity::class.java, Bundle().apply {
                        putString(Constants.CATEGORY_ID,
                            it)*/
                    Intent(holder.itemView.context, SubCategoryActivity::class.java).apply {

                            putExtras(Bundle().apply {
                                putString(Constants.CATEGORY_ID,
                                    it)
                            })
                        holder.itemView.context.startActivity(this)
                    }

                }
                /*categoryAdapter.itemClicked = onCategoryClick*/
                holder.binding.rvShopByCat.adapter = categoryAdapter
            }
            is FeaturedProductsViewHolder -> {
                val featuredAdapter = ProductListAdapter(productList)
                holder.binding.rvFeatureProduct.adapter = featuredAdapter
            }
            is ShopByCropViewHolder -> {
                val cropList = ArrayList<CropData>()
                cropList.addAll(cropResponse?.gpApiResponseData?.cropsList!!)

                val tempCropList: List<CropData> = if (cropList.size >= 9)
                    cropList.subList(0, 9)
                else cropList
                val cropAdapter = ShopByCropsAdapter(tempCropList)
                holder.binding.rvShopByCrops.adapter = cropAdapter
            }
            is ShopByStoreViewHolder -> {
                val storeList = ArrayList<StoreData>()
                storeList.addAll(storeResponse?.gpApiResponseData?.storeList!!)

                val tempStoreList: List<StoreData> = if (storeList.size >= 4)
                    storeList.subList(0, 4)
                else storeList
                val storeAdapter = ShopByStoresAdapter(tempStoreList)
                holder.binding.rvShopByStore.adapter = storeAdapter
            }
            is ShopByCompanyViewHolder -> {
                val companyList = ArrayList<CompanyData>()
                companyList.addAll(companyResponse?.gpApiResponseData?.companiesList!!)

                val tempCompanyList: List<CompanyData> = if (companyList.size >= 6)
                    companyList.subList(0, 6)
                else companyList
                val companyAdapter = ShopByCompanyAdapter(tempCompanyList)
                holder.binding.rvShopByCompany.adapter = companyAdapter
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        when(homeScreenSequenceList[position]) {
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