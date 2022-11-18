package agstack.gramophone.ui.advisory

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityAdvisoryBinding
import agstack.gramophone.databinding.ItemAdvisoryBinding
import agstack.gramophone.ui.advisory.adapter.ActivityLinkedIssuesListAdapter
import agstack.gramophone.ui.advisory.adapter.ActivityLinkedProductsListAdapter
import agstack.gramophone.ui.advisory.adapter.ActivityListAdapter
import agstack.gramophone.ui.advisory.models.advisory.GpApiResponseData
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
import com.amnix.xtension.extensions.inflater
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_advisory.*

@AndroidEntryPoint
class AdvisoryActivity :
    BaseActivityWrapper<ActivityAdvisoryBinding, SubCategoryNavigator, SubCategoryViewModel>(),
    SubCategoryNavigator {

    private val subCategoryViewModel: SubCategoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subCategoryViewModel.updateProfileDetail()
        subCategoryViewModel.getCropAdvisoryDetails()
    }


    override fun getLayoutID(): Int = R.layout.activity_advisory

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): SubCategoryViewModel = subCategoryViewModel
    override fun getBundle(): Bundle? = intent.extras

    override fun setViewPagerAdapter(bannerList: List<Banner>?) {
    }

    override fun setSubCategoryAdapter(subCategoryAdapter: ShopByCategoryAdapter) {
    }

    override fun getSubCategoryDetail(
        categoryId: String,
        subCategoryId: String,
        subCategoryName: String,
        subCategoryImage: String
    ) {
    }

    override fun setProductListAdapter(
        productListAdapter: ProductListAdapter,
        onAddToCartClick: (productId: Int) -> Unit,
        onProductDetailClick: (productId: Int) -> Unit
    ) {
    }

    override fun openAddToCartDialog(
        mSKUList: ArrayList<ProductSkuListItem?>,
        mSkuOfferList: ArrayList<PromotionListItem?>,
        productData: ProductData
    ) {
    }

    override fun openProductDetailsActivity(productData: ProductData) {
    }

    override fun updateOfferApplicabilityOnDialog(
        isOfferApplicable: Boolean,
        promotionId: String?,
        message: String
    ) {
    }

    override fun updateOfferOnAddToCartDialog(mSkuOfferList: ArrayList<PromotionListItem?>) {
    }

    override fun disableSortAndFilter() {
    }

    override fun enableSortAndFilter() {
    }

    override fun loadUrl(url: String) {
    }

    override fun reload() {
    }

    override fun updateActivitiesList(gpApiResponseData: GpApiResponseData) {
        gpApiResponseData.activity.forEach { activityToBeDone ->
            viewDataBinding.llActivityDetails.removeAllViews()
            val view = inflater.inflate(R.layout.item_advisory, null)
            val advisoryLayout = ItemAdvisoryBinding.bind(view)
            advisoryLayout.tvActivityName.text = activityToBeDone.activity_name
            advisoryLayout.tvBriefDesc.text = activityToBeDone.activity_brief_description
            advisoryLayout.tvShortDesc.text = activityToBeDone.short_application

            if (activityToBeDone.linked_issues != null && activityToBeDone.linked_issues.size > 0) {
                val activityLinkedIssuesListAdapter =
                    ActivityLinkedIssuesListAdapter(activityToBeDone.linked_issues)
                advisoryLayout.rvLikedIssues.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                advisoryLayout.rvLikedIssues.setHasFixedSize(true)
                advisoryLayout.rvLikedIssues.adapter = activityLinkedIssuesListAdapter
            }

            if (activityToBeDone.linked_technicals != null && activityToBeDone.linked_technicals.size > 0) {
                val activityLinkedProductListAdapter =
                    ActivityLinkedProductsListAdapter(activityToBeDone.linked_technicals)
                advisoryLayout.rvLikedProduct.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                advisoryLayout.rvLikedProduct.setHasFixedSize(true)
                advisoryLayout.rvLikedProduct.adapter = activityLinkedProductListAdapter
            }



            viewDataBinding.llActivityDetails.addView(view)
        }

    }

    override fun setAdvisoryActivity(
        activityListAdapter: ActivityListAdapter,
        onActivityClciekd: (GpApiResponseData) -> Unit
    ) {
        activityListAdapter.onActivitySelected = onActivityClciekd
        rvActivity.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvActivity.setHasFixedSize(true)
        rvActivity.adapter = activityListAdapter
    }

    override fun finishActivity() {
        finish()
    }
}