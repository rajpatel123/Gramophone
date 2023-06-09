package agstack.gramophone.ui.advisory

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityAdvisoryBinding
import agstack.gramophone.databinding.CropIssueIdialogBinding
import agstack.gramophone.databinding.ItemAdvisoryBinding
import agstack.gramophone.ui.advisory.adapter.*
import agstack.gramophone.ui.advisory.models.advisory.GpApiResponseData
import agstack.gramophone.ui.advisory.models.advisory.LinkedIssue
import agstack.gramophone.ui.advisory.view.CropIssueBottomSheetDialog
import agstack.gramophone.ui.cart.view.CartActivity
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
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.amnix.xtension.extensions.inflater
import com.amnix.xtension.extensions.isNotNull
import com.amnix.xtension.extensions.isNotNullOrEmpty
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_advisory.*
import kotlinx.android.synthetic.main.item_advisory.view.*


@AndroidEntryPoint
class AdvisoryActivity :
    BaseActivityWrapper<ActivityAdvisoryBinding, SubCategoryNavigator, SubCategoryViewModel>(),
    SubCategoryNavigator {

    private var isOpened: Boolean=false
    private lateinit var activityListAdapter: ActivityListAdapter
    private val subCategoryViewModel: SubCategoryViewModel by viewModels()
    var bottomSheet: AddToCartBottomSheetDialog? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subCategoryViewModel.updateProfileDetail()
        subCategoryViewModel.getCropAdvisoryDetails()
        viewDataBinding.llCommunityLL.goToCommunity.setOnClickListener { openCommunity() }
        viewDataBinding.llCommunityLL.tvDisclaimer.text =
            Html.fromHtml(getString(R.string.disclaimer), 0)
    }

    override fun onResume() {
        super.onResume()
        updateCartCount(SharedPreferencesHelper.instance?.getInteger(SharedPreferencesKeys.CART_ITEM_COUNT)!!)
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
        categoryId: String, subCategoryId: String, subCategoryName: String, subCategoryImage: String
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
        if (bottomSheet!=null && bottomSheet?.isVisible == true){
            return
        }
        bottomSheet = AddToCartBottomSheetDialog({
            //Offer detail activity from here
            openActivity(OfferDetailActivity::class.java, Bundle().apply {

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
            isOpened=false
        }, { productId, quantity ->

            subCategoryViewModel.loadOffersData(productId, quantity, true)
        })
        bottomSheet?.mSKUList = mSKUList
        bottomSheet?.productData = productData
        bottomSheet?.mSkuOfferList = mSkuOfferList
        isOpened=true

            bottomSheet?.show(
                supportFragmentManager, Constants.BOTTOM_SHEET
            )




    }

    override fun openProductDetailsActivity(productData: ProductData) {
    }

    override fun updateOfferApplicabilityOnDialog(
        isOfferApplicable: Boolean, promotionId: String?, message: String
    ) {
        if (bottomSheet.isNotNull()) bottomSheet?.updateDialog(
            isOfferApplicable,
            promotionId!!,
            message
        )
    }

    override fun updateOfferOnAddToCartDialog(mSkuOfferList: ArrayList<PromotionListItem?>) {
        if (bottomSheet.isNotNull()) bottomSheet?.updateOffer(mSkuOfferList)
    }

    override fun disableSortAndFilter() {
    }

    override fun enableSortAndFilter() {
    }

    override fun disableFilterOnly() {

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
            viewDataBinding.llActivityDetails.removeAllViews()
            var count = 1
            gpApiResponseData.activity.forEach { activityToBeDone ->

                viewDataBinding.noActivityLL.visibility = GONE
                viewDataBinding.llActivityDetails.visibility = VISIBLE


                val view = inflater.inflate(R.layout.item_advisory, null)

                if (count % 2 != 0) {
                    view.tvActivityName.background = AppCompatResources.getDrawable(
                        this, R.drawable.advisoryhead_bg
                    )

                    view.llActivityDetails.background = AppCompatResources.getDrawable(
                        this, R.drawable.advisory_border
                    )
                } else {

                    view.tvActivityName.background = AppCompatResources.getDrawable(
                        this, R.drawable.advisoryhead_blue_bg
                    )

                    view.llActivityDetails.background = AppCompatResources.getDrawable(
                        this, R.drawable.advisory_blue_border
                    )
                }
                count++

                val advisoryLayout = ItemAdvisoryBinding.bind(view)
                advisoryLayout.tvActivityName.text = activityToBeDone.activity_name
                advisoryLayout.tvBriefDesc.text = activityToBeDone.activity_brief_description
                advisoryLayout.tvShortDesc.text = activityToBeDone.short_application
                if (activityToBeDone.linked_issues.isNotNullOrEmpty()) {

                    advisoryLayout.rvLikedIssues.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                    advisoryLayout.rvLikedIssues.setHasFixedSize(true)
                    advisoryLayout.rvLikedIssues.adapter =
                        ActivityLinkedIssuesListAdapter(activityToBeDone.linked_issues) {
                            openProblemDialog(it)
                        }
                }

                if (activityToBeDone.linked_technicals.isNotNullOrEmpty()) {
                    advisoryLayout.rvLikedProduct.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    advisoryLayout.rvLikedProduct.setHasFixedSize(true)
                    advisoryLayout.rvLikedProduct.adapter =
                        ActivityLinkedProductsListAdapter(activityToBeDone.linked_technicals,
                            {
                                subCategoryViewModel.fetchProductDetail(it)
                            },
                            {
                                if (it > 0)
                                    openProductDetailsActivity(ProductData(it))
                            })
                }
                viewDataBinding.llActivityDetails.addView(view)
                subCategoryViewModel.getCropIssues(activityToBeDone.stage_id)
            }
        }
    }

    override fun setAdvisoryProblemsActivity(
        cropIssueListAdapter: CropIssueListAdapter,
        function: (agstack.gramophone.ui.advisory.models.cropproblems.GpApiResponseData) -> Unit,
    ) {

        if (cropIssueListAdapter.dataList.size > 0) {
            viewDataBinding.rlCropProblems.visibility = VISIBLE

            if (cropIssueListAdapter.dataList.size > 8) {
                viewDataBinding.tvViewAllRl.visibility = VISIBLE
            } else {
                viewDataBinding.tvViewAllRl.visibility = GONE
            }
            cropIssueListAdapter.onProblemSelected = function
            rvCropProblems.layoutManager = GridLayoutManager(this, 2)
            rvCropProblems.setHasFixedSize(true)
            rvCropProblems.adapter = cropIssueListAdapter
        } else {
            viewDataBinding.rlCropProblems.visibility = GONE
        }
    }

    override fun updateCartCount(cartCount: Int) {
        try {
            if (cartCount > 0) {
                viewDataBinding.tvCartCount.text = cartCount.toString()
                viewDataBinding.frameCartRedCircle.visibility = View.VISIBLE
            } else {
                viewDataBinding.frameCartRedCircle.visibility = View.GONE
            }
            viewDataBinding.rlItemMenuCart.setOnClickListener {
                openActivity(CartActivity::class.java, Bundle().apply {
                    putString(Constants.REDIRECTION_SOURCE, "Advisory")
                })
            }
            viewDataBinding.ivItemMenuCart.setColorFilter(ContextCompat.getColor(this, R.color.blakish), android.graphics.PorterDuff.Mode.SRC_IN)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun scrollToActivity(i: Int) {
        (rvActivity.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(i, 0)

    }

    override fun updateAddToCartButtonText(message: String) {
    }

    override fun showInfoBottomSheet() {
        val bottomSheet = CropIssueBottomSheetDialog()
        bottomSheet.bundle = intent.extras
        bottomSheet.show(
            supportFragmentManager, Constants.BOTTOM_SHEET
        )
    }

    override fun openIssueImagesBottomSheet(it: GpApiResponseData) {
            val bottomSheet = CropIssueBottomSheetDialog()
            bottomSheet.setData(it)
            bottomSheet.show(
                supportFragmentManager, Constants.BOTTOM_SHEET
            )

    }

    override fun setProductList(
        recommendedLinkedProductsListAdapter: RecommendedLinkedProductsListAdapter,
        onAddToCartClick: (productId: Int) -> Unit,
        onProductDetailClick: (productId: Int) -> Unit,
    ) {
        // Don't write anything here. This method is only used in ArticleWebViewActivity
    }

    private fun openCommunity() {
       subCategoryViewModel.openHelpClicked()
    }

    override fun setAdvisoryActivity(
        activityListAdapter: ActivityListAdapter,
        function: (GpApiResponseData) -> Unit,
        infoClicked: (GpApiResponseData) -> Unit,
        positionSelected: (Int) -> Unit,
    ) {
        viewDataBinding.contentLayouts.visibility= VISIBLE
        viewDataBinding.progressBar.visibility= GONE
        this.activityListAdapter= activityListAdapter
        if (activityListAdapter.dataList.isNotEmpty()) {
            activityListAdapter.selectedPosition = positionSelected
            activityListAdapter.onActivitySelected = function
            activityListAdapter.onActivityInfoClicked = infoClicked
            rvActivity.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            rvActivity.setHasFixedSize(true)
            rvActivity.adapter = activityListAdapter
            viewDataBinding.llActivityStageAvailable.visibility = VISIBLE
            viewDataBinding.llNoActivityStage.visibility = GONE
//            rvActivity.scrollToPosition(activityListAdapter.lastSelectedActivityPosition)

            viewDataBinding.progressBar.visibility= GONE



        } else {
            viewDataBinding.llActivityStageAvailable.visibility = GONE
            viewDataBinding.llNoActivityStage.visibility = VISIBLE
        }


        if (subCategoryViewModel.isCustomerFarm.get() == true) {
            viewDataBinding.llCommunityLL.communityLL.visibility = VISIBLE
        } else {
            viewDataBinding.llCommunityLL.communityLL.visibility = GONE

        }

    }

    fun openProblemDialog(linkedIssue: LinkedIssue) {
        //Inflate the dialog with custom view   use Binding
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.crop_issue_idialog, null)
        val dialogBinding = CropIssueIdialogBinding.bind(mDialogView)
        dialogBinding.setVariable(BR.model, linkedIssue)


        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this).setView(dialogBinding.root)
        //show dialog
        val mAlertDialog = mBuilder.show()

        mAlertDialog.getWindow()?.setBackgroundDrawableResource(R.drawable.transparent_background);
        dialogBinding.llCrossLinearLayout.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }


    override fun finishActivity() {
        finish()
    }
}