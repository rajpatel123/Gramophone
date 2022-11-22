package agstack.gramophone.ui.advisory

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityAdvisoryBinding
import agstack.gramophone.databinding.ItemAdvisoryBinding
import agstack.gramophone.ui.advisory.adapter.*
import agstack.gramophone.ui.advisory.models.advisory.GpApiResponseData
import agstack.gramophone.ui.advisory.models.advisory.LinkedTechnical
import agstack.gramophone.ui.advisory.view.CropIssueBottomSheetDialog
import agstack.gramophone.ui.home.adapter.ShopByCategoryAdapter
import agstack.gramophone.ui.home.subcategory.ProductListAdapter
import agstack.gramophone.ui.home.subcategory.SubCategoryNavigator
import agstack.gramophone.ui.home.subcategory.SubCategoryViewModel
import agstack.gramophone.ui.home.view.fragments.market.model.Banner
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.home.view.fragments.market.model.ProductSkuListItem
import agstack.gramophone.ui.home.view.fragments.market.model.PromotionListItem
import agstack.gramophone.utils.Constants
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.amnix.xtension.extensions.inflater
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_advisory.*
import java.util.stream.Collectors


@AndroidEntryPoint
class AdvisoryActivity :
    BaseActivityWrapper<ActivityAdvisoryBinding, SubCategoryNavigator, SubCategoryViewModel>(),
    SubCategoryNavigator {

    private val subCategoryViewModel: SubCategoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subCategoryViewModel.updateProfileDetail()
        subCategoryViewModel.getCropAdvisoryDetails()
        viewDataBinding.llCommunityLL.goToCommunity.setOnClickListener {openCommunity()  }
    }


    override fun getLayoutID(): Int = R.layout.activity_advisory

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): SubCategoryViewModel = subCategoryViewModel
    override fun getBundle(): Bundle? = intent.getBundleExtra("bundle")

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

    @RequiresApi(Build.VERSION_CODES.N)
    override fun updateActivitiesList(gpApiResponseData: GpApiResponseData) {

        if (gpApiResponseData.activity.isEmpty()){
            viewDataBinding.llActivityDetails.removeAllViews()
            viewDataBinding.noActivityLL.visibility=VISIBLE
            viewDataBinding.llActivityDetails.visibility=GONE

        }else{
            gpApiResponseData.activity.forEach { activityToBeDone ->
                viewDataBinding.llActivityDetails.removeAllViews()
                viewDataBinding.noActivityLL.visibility= GONE
                viewDataBinding.llActivityDetails.visibility= VISIBLE
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
                    val sortedList: List<LinkedTechnical> =
                        activityToBeDone.linked_technicals.stream()
                            .sorted(Comparator.comparingInt(LinkedTechnical::product_id).reversed())
                            .collect(Collectors.toList())

                    val activityLinkedProductListAdapter =
                        ActivityLinkedProductsListAdapter(sortedList)
                    advisoryLayout.rvLikedProduct.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    advisoryLayout.rvLikedProduct.setHasFixedSize(true)
                    advisoryLayout.rvLikedProduct.adapter = activityLinkedProductListAdapter
                }
                viewDataBinding.llActivityDetails.addView(view)
                subCategoryViewModel.getCropIssues(activityToBeDone.stage_id)
            }

        }

    }

    override fun setAdvisoryProblemsActivity(
        cropIssueListAdapter: CropIssueListAdapter,
        onProblemSelected: (agstack.gramophone.ui.advisory.models.cropproblems.GpApiResponseData) -> Unit
    ) {

        if (cropIssueListAdapter.dataList.size>6){
            viewDataBinding.tvViewAllRl.visibility= VISIBLE
        }else{
            viewDataBinding.tvViewAllRl.visibility= VISIBLE

        }
        cropIssueListAdapter.onProblemSelected = onProblemSelected
        rvCropProblems.layoutManager = GridLayoutManager(this, 2)
        rvCropProblems.setHasFixedSize(true)
        rvCropProblems.adapter = cropIssueListAdapter

    }

    override fun showInfoBottomSheet() {
        val bottomSheet = CropIssueBottomSheetDialog()
        bottomSheet.bundle=intent.extras
        bottomSheet.show(
          supportFragmentManager,
            Constants.BOTTOM_SHEET
        )
    }

    override fun openIssueImagesBottomSheet(it: GpApiResponseData) {
        val bottomSheet = CropIssueBottomSheetDialog()
        bottomSheet.setData(it)
        bottomSheet.show(
            supportFragmentManager,
            Constants.BOTTOM_SHEET
        )
    }

    override fun setProductList(
        recommendedLinkedProductsListAdapter: RecommendedLinkedProductsListAdapter,
        function: (agstack.gramophone.ui.advisory.models.recomondedproducts.GpApiResponseData) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    private fun openCommunity() {
        val data = Intent();
        data.putExtra(Constants.GO_TO_COMMUNITY,"advisory")
        setResult(RESULT_OK, data);
        finish();
    }

    override fun setAdvisoryActivity(
        activityListAdapter: ActivityListAdapter,
        onActivityClciekd: (GpApiResponseData) -> Unit,
        onInfoClicked: (GpApiResponseData) -> Unit
    ) {

        if (activityListAdapter.dataList.isNotEmpty()){
            activityListAdapter.onActivitySelected = onActivityClciekd
            activityListAdapter.onActivityInfoClicked = onInfoClicked
            rvActivity.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            rvActivity.setHasFixedSize(true)
            rvActivity.adapter = activityListAdapter
            viewDataBinding.llActivityStageAvailable.visibility = VISIBLE
            viewDataBinding.llNoActivityStage.visibility = GONE
        }else{
            viewDataBinding.llActivityStageAvailable.visibility = GONE
            viewDataBinding.llNoActivityStage.visibility = VISIBLE
        }

    }

    override fun finishActivity() {
        finish()
    }
}