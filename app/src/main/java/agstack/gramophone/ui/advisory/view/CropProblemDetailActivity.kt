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
import agstack.gramophone.ui.home.adapter.ShopByCategoryAdapter
import agstack.gramophone.ui.home.subcategory.ProductListAdapter
import agstack.gramophone.ui.home.subcategory.SubCategoryNavigator
import agstack.gramophone.ui.home.subcategory.SubCategoryViewModel
import agstack.gramophone.ui.home.view.fragments.market.model.Banner
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.home.view.fragments.market.model.ProductSkuListItem
import agstack.gramophone.ui.home.view.fragments.market.model.PromotionListItem
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CropProblemDetailActivity :
    BaseActivityWrapper<ActivityCropProblemDetailBinding, SubCategoryNavigator, SubCategoryViewModel>(),
    SubCategoryNavigator {

    private val subCategoryViewModel: SubCategoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolBar(true, "", R.drawable.ic_action_navigation_arrow_back, true)
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
        onProductDetailClick: (productId: Int) -> Unit
    ) {
        // Don't write anything here. This method is only used in ArticleWebViewActivity
    }

    override fun openAddToCartDialog(
        mSKUList: ArrayList<ProductSkuListItem?>,
        mSkuOfferList: ArrayList<PromotionListItem?>,
        productData: ProductData
    ) {
        // Don't write anything here. This method is only used in ArticleWebViewActivity
    }

    override fun openProductDetailsActivity(productData: ProductData) {
        // Don't write anything here. This method is only used in ArticleWebViewActivity
    }

    override fun updateOfferApplicabilityOnDialog(
        isOfferApplicable: Boolean,
        promotionId: String?,
        message: String
    ) {
        // Don't write anything here. This method is only used in ArticleWebViewActivity
    }

    override fun updateOfferOnAddToCartDialog(mSkuOfferList: ArrayList<PromotionListItem?>) {
        // Don't write anything here. This method is only used in ArticleWebViewActivity
    }

    override fun disableSortAndFilter() {
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

    override fun setProductList(
        recommendedLinkedProductsListAdapter: RecommendedLinkedProductsListAdapter,
        onAddToCartClciked: (GpApiResponseData) -> Unit
    ) {
        recommendedLinkedProductsListAdapter.onAddToCartClicked = onAddToCartClciked
        viewDataBinding.rvRecommendedProduct.layoutManager = LinearLayoutManager(this)
        viewDataBinding.rvRecommendedProduct.setHasFixedSize(true)
        viewDataBinding.rvRecommendedProduct.adapter = recommendedLinkedProductsListAdapter
    }
}