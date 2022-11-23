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
import agstack.gramophone.ui.dialog.cart.AddToCartBottomSheetDialog
import agstack.gramophone.ui.home.adapter.ShopByCategoryAdapter
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
import com.amnix.xtension.extensions.isNotNull
import com.amnix.xtension.extensions.isNotNullOrEmpty
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_advisory.*
import java.util.stream.Collectors


@AndroidEntryPoint
class AdvisoryActivity :
    BaseActivityWrapper<ActivityAdvisoryBinding, SubCategoryNavigator, SubCategoryViewModel>(),
    SubCategoryNavigator {

    private val subCategoryViewModel: SubCategoryViewModel by viewModels()
    var bottomSheet: AddToCartBottomSheetDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subCategoryViewModel.updateProfileDetail()
        subCategoryViewModel.getCropAdvisoryDetails()
        viewDataBinding.llCommunityLL.goToCommunity.setOnClickListener {openCommunity()  }
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
    }

    override fun enableSortAndFilter() {
    }

    override fun loadUrl(url: String) {
    }

    override fun reload() {
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun updateActivitiesList(gpApiResponseData: GpApiResponseData) {

        if (gpApiResponseData.activity.isEmpty()) {
            viewDataBinding.llActivityDetails.removeAllViews()
            viewDataBinding.noActivityLL.visibility = VISIBLE
            viewDataBinding.llActivityDetails.visibility = GONE

        } else {
            gpApiResponseData.activity.forEach { activityToBeDone ->
                viewDataBinding.llActivityDetails.removeAllViews()
                viewDataBinding.noActivityLL.visibility = GONE
                viewDataBinding.llActivityDetails.visibility = VISIBLE
                val view = inflater.inflate(R.layout.item_advisory, null)
                val advisoryLayout = ItemAdvisoryBinding.bind(view)
                advisoryLayout.tvActivityName.text = activityToBeDone.activity_name
                advisoryLayout.tvBriefDesc.text = activityToBeDone.activity_brief_description
                advisoryLayout.tvShortDesc.text = activityToBeDone.short_application

                if (activityToBeDone.linked_issues.isNotNullOrEmpty()) {
                    val activityLinkedIssuesListAdapter =
                        ActivityLinkedIssuesListAdapter(activityToBeDone.linked_issues)
                    advisoryLayout.rvLikedIssues.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                    advisoryLayout.rvLikedIssues.setHasFixedSize(true)
                    advisoryLayout.rvLikedIssues.adapter = activityLinkedIssuesListAdapter
                }

                if (activityToBeDone.linked_technicals.isNotNullOrEmpty()) {
                    advisoryLayout.rvLikedProduct.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    advisoryLayout.rvLikedProduct.setHasFixedSize(true)
                    advisoryLayout.rvLikedProduct.adapter =
                        ActivityLinkedProductsListAdapter(activityToBeDone.linked_technicals) {
                            subCategoryViewModel.fetchProductDetail(it)
                        }
                }
                viewDataBinding.llActivityDetails.addView(view)
                subCategoryViewModel.getCropIssues(activityToBeDone.stage_id)
            }
        }
    }

    override fun setAdvisoryProblemsActivity(
        activityListAdapter: CropIssueListAdapter,
        function: (agstack.gramophone.ui.advisory.models.cropproblems.GpApiResponseData) -> Unit,
    ) {

        if (activityListAdapter.dataList.size > 6) {
            viewDataBinding.tvViewAllRl.visibility = VISIBLE
        } else {
            viewDataBinding.tvViewAllRl.visibility = VISIBLE

        }
        activityListAdapter.onProblemSelected = function
        rvCropProblems.layoutManager = GridLayoutManager(this, 2)
        rvCropProblems.setHasFixedSize(true)
        rvCropProblems.adapter = activityListAdapter

    }

    override fun showInfoBottomSheet() {
        val bottomSheet = CropIssueBottomSheetDialog()
        bottomSheet.bundle = intent.extras
        bottomSheet.show(
            supportFragmentManager,
            Constants.BOTTOM_SHEET
        )
    }

    override fun openIssueImagesBottomSheet(it: GpApiResponseData) {
        if (it.stage_description!=null || it.crop_stage_images.size>0){
            val bottomSheet = CropIssueBottomSheetDialog()
            bottomSheet.setData(it)
            bottomSheet.show(
                supportFragmentManager,
                Constants.BOTTOM_SHEET
            )
        }

    }

    override fun setProductList(
        recommendedLinkedProductsListAdapter: RecommendedLinkedProductsListAdapter,
        onAddToCartClick: (productId: Int) -> Unit,
        onProductDetailClick: (productId: Int) -> Unit,
    ) {
        // Don't write anything here. This method is only used in ArticleWebViewActivity
    }

    private fun openCommunity() {
        val data = Intent();
        data.putExtra(Constants.GO_TO_COMMUNITY,"advisory")
        setResult(RESULT_OK, data);
        finish();
    }

    override fun setAdvisoryActivity(
        activityListAdapter: ActivityListAdapter,
        function: (GpApiResponseData) -> Unit,
        infoClicked: (GpApiResponseData) -> Unit,
    ) {

        if (activityListAdapter.dataList.isNotEmpty()) {
            activityListAdapter.onActivitySelected = function
            activityListAdapter.onActivityInfoClicked = infoClicked
            rvActivity.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            rvActivity.setHasFixedSize(true)
            rvActivity.adapter = activityListAdapter
            viewDataBinding.llActivityStageAvailable.visibility = VISIBLE
            viewDataBinding.llNoActivityStage.visibility = GONE
        } else {
            viewDataBinding.llActivityStageAvailable.visibility = GONE
            viewDataBinding.llNoActivityStage.visibility = VISIBLE
        }

    }

    override fun finishActivity() {
        finish()
    }
}