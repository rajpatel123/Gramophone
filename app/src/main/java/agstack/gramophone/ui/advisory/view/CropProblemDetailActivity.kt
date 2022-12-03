package agstack.gramophone.ui.advisory.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityCropProblemDetailBinding
import agstack.gramophone.ui.advisory.CropProblemDetailNavigator
import agstack.gramophone.ui.advisory.adapter.ActivityListAdapter
import agstack.gramophone.ui.advisory.adapter.CropIssueListAdapter
import agstack.gramophone.ui.advisory.adapter.RecommendedLinkedProductsListAdapter
import agstack.gramophone.ui.advisory.models.recomondedproducts.GpApiResponseData
import agstack.gramophone.ui.dialog.cart.AddToCartBottomSheetDialog
import agstack.gramophone.ui.home.adapter.ShopByCategoryAdapter
import agstack.gramophone.ui.home.product.activity.ProductDetailsActivity
import agstack.gramophone.ui.home.subcategory.ProductListAdapter
import agstack.gramophone.ui.home.subcategory.SubCategoryNavigator
import agstack.gramophone.ui.home.subcategory.SubCategoryViewModel
import agstack.gramophone.ui.home.view.fragments.market.model.Banner
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.home.view.fragments.market.model.ProductSkuListItem
import agstack.gramophone.ui.home.view.fragments.market.model.PromotionListItem
import agstack.gramophone.ui.offer.OfferDetailActivity
import agstack.gramophone.ui.offerslist.model.DataItem
import agstack.gramophone.utils.Constants
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.amnix.xtension.extensions.isNotNull
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CropProblemDetailActivity :
    BaseActivityWrapper<ActivityCropProblemDetailBinding, SubCategoryNavigator, SubCategoryViewModel>(),
    SubCategoryNavigator {

    private val subCategoryViewModel: SubCategoryViewModel by viewModels()
    var bottomSheet: AddToCartBottomSheetDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolBar(true, "", R.drawable.ic_arrow_left, true)
        subCategoryViewModel.getRecommendedProduct()
        subCategoryViewModel.setDiseaseDetails()
    }

    override fun getLayoutID(): Int = R.layout.activity_crop_problem_detail

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): SubCategoryViewModel {
        return subCategoryViewModel
    }

    override fun getBundle(): Bundle? {
        return intent.extras
    }

    override fun setViewPagerAdapter(bannerList: List<Banner>?) {
        // Don't write anything here. This method is only used in ArticleWebViewActivity
    }

    override fun setSubCategoryAdapter(subCategoryAdapter: ShopByCategoryAdapter) {
        // Don't write anything here. This method is only used in ArticleWebViewActivity
    }

    override fun getSubCategoryDetail(
        categoryId: String,
        subCategoryId: String,
        subCategoryName: String,
        subCategoryImage: String
    ) {
        // Don't write anything here. This method is only used in ArticleWebViewActivity
    }

    override fun setProductListAdapter(
        productListAdapter: ProductListAdapter,
        onAddToCartClick: (productId: Int) -> Unit,
        onProductDetailClicked: (productId: Int) -> Unit
    ) {
        // Don't write anything here. This method is only used in ArticleWebViewActivity
    }

    override fun openAddToCartDialog(
        mSKUList: ArrayList<ProductSkuListItem?>,
        mSkuOfferList: ArrayList<PromotionListItem?>,
        productData: ProductData
    ) {
        bottomSheet = AddToCartBottomSheetDialog({
            //Offer detail activity from here
            openActivity(
                OfferDetailActivity::class.java,
                Bundle().apply {

                    val offersDataItem = DataItem()
                    offersDataItem.endDate = it.valid_till
                    offersDataItem.productName = it.title
                    offersDataItem.productsku = it.applicable_on_sku
                    offersDataItem.image = it.image
                    offersDataItem.termsConditions = it.t_c
                    putParcelable(Constants.OFFERSDATA, offersDataItem)

                })
        }, {
            subCategoryViewModel.checkOfferApplicability(it)
        }, {
            subCategoryViewModel.onAddToCartClicked(it)
        }, { productId, quantity ->
            subCategoryViewModel.loadOffersData(productId, quantity, true)
        })
        bottomSheet?.mSKUList = mSKUList
        bottomSheet?.productData = productData
        bottomSheet?.mSkuOfferList = mSkuOfferList
        bottomSheet?.show(
            supportFragmentManager,
            Constants.BOTTOM_SHEET
        )
    }

    override fun openProductDetailsActivity(productData: ProductData) {
        val bundle = Bundle()
        bundle.putParcelable(Constants.PRODUCT, productData)
        openActivity(ProductDetailsActivity::class.java, bundle)
    }

    override fun updateOfferApplicabilityOnDialog(
        isOfferApplicable: Boolean,
        promotionId: String?,
        message: String
    ) {
        if (bottomSheet.isNotNull())
            bottomSheet?.updateDialog(isOfferApplicable, promotionId!!, message)
    }

    override fun updateOfferOnAddToCartDialog(mSkuOfferList: ArrayList<PromotionListItem?>) {
        if (bottomSheet.isNotNull())
            bottomSheet?.updateOffer(mSkuOfferList)
    }

    override fun disableSortAndFilter() {
        // Don't write anything here. This method is only used in ArticleWebViewActivity
    }

    override fun disableFilterOnly() {
        // Don't write anything here. This method is only used in ArticleWebViewActivity
    }

    override fun enableSortAndFilter() {
        // Don't write anything here. This method is only used in ArticleWebViewActivity
    }

    override fun loadUrl(url: String) {
        // Don't write anything here. This method is only used in ArticleWebViewActivity
    }

    override fun reload() {
        // Don't write anything here. This method is only used in ArticleWebViewActivity
    }

    override fun setAdvisoryActivity(
        activityListAdapter: ActivityListAdapter,
        function: (agstack.gramophone.ui.advisory.models.advisory.GpApiResponseData) -> Unit,
        infoClicked: (agstack.gramophone.ui.advisory.models.advisory.GpApiResponseData) -> Unit
    ) {
        // Don't write anything here. This method is only used in ArticleWebViewActivity
    }

    override fun updateActivitiesList(it: agstack.gramophone.ui.advisory.models.advisory.GpApiResponseData) {
        // Don't write anything here. This method is only used in ArticleWebViewActivity
    }

    override fun setAdvisoryProblemsActivity(
        activityListAdapter: CropIssueListAdapter,
        function: (agstack.gramophone.ui.advisory.models.cropproblems.GpApiResponseData) -> Unit
    ) {
        // Don't write anything here. This method is only used in ArticleWebViewActivity
    }

    override fun showInfoBottomSheet() {
        // Don't write anything here. This method is only used in ArticleWebViewActivity
    }

    override fun openIssueImagesBottomSheet(it: agstack.gramophone.ui.advisory.models.advisory.GpApiResponseData) {
        // Don't write anything here. This method is only used in ArticleWebViewActivity

    }

    override fun updateCartCount(cartCount: Int) {
        // Don't write anything here. This method is only used in FeaturedActivity & SubCategoryActivity
    }

    override fun setProductList(
        recommendedLinkedProductsListAdapter: RecommendedLinkedProductsListAdapter,
        onAddToCartClick: (productId: Int) -> Unit,
        onProductDetailClick: (productId: Int) -> Unit
    ) {
        recommendedLinkedProductsListAdapter.onAddToCartClick = onAddToCartClick
        recommendedLinkedProductsListAdapter.onProductDetailClick = onProductDetailClick
        viewDataBinding.rvRecommendedProduct.layoutManager = LinearLayoutManager(this)
        viewDataBinding.rvRecommendedProduct.setHasFixedSize(true)
        viewDataBinding.rvRecommendedProduct.adapter = recommendedLinkedProductsListAdapter
    }
}