package agstack.gramophone.ui.home.view.fragments.gramophone

import agstack.gramophone.BR
import agstack.gramophone.BuildConfig
import agstack.gramophone.R
import agstack.gramophone.base.BaseFragment
import agstack.gramophone.databinding.FragmentMyGramophoneBinding
import agstack.gramophone.ui.articles.ArticlesWebViewActivity
import agstack.gramophone.ui.comments.view.CommentsActivity
import agstack.gramophone.ui.farm.model.FarmResponse
import agstack.gramophone.ui.farm.view.AddFarmActivity
import agstack.gramophone.ui.farm.view.SelectCropActivity
import agstack.gramophone.ui.farm.view.ViewAllFarmsActivity
import agstack.gramophone.ui.gramcash.GramCashActivity
import agstack.gramophone.ui.home.view.HomeActivity
import agstack.gramophone.ui.home.view.fragments.community.LikedPostUserListActivity
import agstack.gramophone.ui.home.view.fragments.community.model.likes.LikeUpdate
import agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.CommunityHomeResponseModel
import agstack.gramophone.ui.home.view.fragments.gramophone.model.MyGramophoneResponseModel
import agstack.gramophone.ui.home.view.fragments.gramophone.viewmodel.MyGramophoneFragmentViewModel
import agstack.gramophone.ui.home.view.fragments.market.model.CropData
import agstack.gramophone.ui.offerslist.OffersListActivity
import agstack.gramophone.ui.order.model.GpApiResponseData
import agstack.gramophone.ui.order.view.OrderListActivity
import agstack.gramophone.ui.orderdetails.OrderDetailsActivity
import agstack.gramophone.ui.referandearn.ReferAndEarnActivity
import agstack.gramophone.ui.referandearn.model.GramCashResponseModel
import agstack.gramophone.utils.*
import agstack.gramophone.view.activity.CreatePostActivity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_community.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class MyGramophoneFragment :
    BaseFragment<FragmentMyGramophoneBinding, MyGramophoneFragmentNavigator, MyGramophoneFragmentViewModel>(),
    MyGramophoneFragmentNavigator ,ShareSheetPresenter.GenericUriHandler{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val myGramophoneFragmentViewModel: MyGramophoneFragmentViewModel by viewModels()
    private var shareSheetPresenter: ShareSheetPresenter? = null

    /**
     * Create Binding
     */
    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentMyGramophoneBinding = FragmentMyGramophoneBinding.inflate(inflater, container, false)

    /**
     * onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    /**
     * onViewCreated
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing=true
            setUpUI()
        }
        setUpUI()

    }

    private fun setUpUI() {
        binding?.tvViewProfile?.setOnClickListener {
            myGramophoneFragmentViewModel.onViewProfileClicked()
        }
        shareSheetPresenter = this.let { ShareSheetPresenter(activity as HomeActivity, it) }
        shareSheetPresenter!!.shareDynamicLink()
        myGramophoneFragmentViewModel.initProfile()
        myGramophoneFragmentViewModel.getGramCash()
        myGramophoneFragmentViewModel.getMyPost()
        myGramophoneFragmentViewModel.getPlacedOrder()
        myGramophoneFragmentViewModel.getFarms()
        myGramophoneFragmentViewModel.getMyGramophoneData()

        swipeRefresh.isRefreshing=false

    }


    override fun getLayoutID(): Int {
        return R.layout.fragment_my_gramophone
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): MyGramophoneFragmentViewModel {
        return myGramophoneFragmentViewModel
    }

    override fun setUserName() {
        binding?.tvName?.text =
            SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.USERNAME)
    }

    override fun setUserAddress() {
        binding?.tvLocation?.text =
            SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.CUSTOMER_ADDRESS)
    }

    override fun setUserImage() {
        Glide.with(this)
            .load(SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.USER_IMAGE))
            .into(
                binding?.ivUserProfile!!
            )
    }

    override fun updateGramCash(gramCashResponseModel: GramCashResponseModel) {
        binding?.layoutGramCash?.tvTotalAmount?.text =
            gramCashResponseModel.gpApiResponseData?.gramcashTotal.toString()
        binding?.layoutGramCash?.txtAmountAvailable?.text =
            gramCashResponseModel.gpApiResponseData?.gramcashAvailable.toString()
        binding?.layoutGramCash?.txtPendingAmount?.text =
            gramCashResponseModel.gpApiResponseData?.gramcashPending.toString()
        binding?.layoutGramCash?.tvCashDetails?.setOnClickListener {
            openActivity(GramCashActivity::class.java, null)
        }
        binding?.layoutReferral?.tvReferralCount?.text = gramCashResponseModel.gpApiResponseData?.myReferrals?.size.toString()
        binding?.layoutReferral?.ivNext1?.setOnClickListener {
            openActivity(ReferAndEarnActivity::class.java, null)
        }

        binding?.layoutReferral?.itemReferral?.setOnClickListener {
            openActivity(ReferAndEarnActivity::class.java, null)
        }

        binding?.layoutReferral?.btnShareApp?.setOnClickListener {
            var shareMessage = gramCashResponseModel.gpApiResponseData?.share_message!!+"\n"+getMessage(R.string.app_url)


            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type="text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent,""))
        }
    }

    override fun updateCommunity(communityHomeResponseModel: CommunityHomeResponseModel) {
       if (communityHomeResponseModel.data.size>0){
           binding?.layoutMyPost?.itemPost?.visibility= VISIBLE
           binding?.layoutMyPost?.tvGoToPosts?.visibility= VISIBLE
           binding?.layoutMyPost?.ivNext1?.visibility= VISIBLE
           binding?.layoutMyPost?.llNoPost?.visibility= GONE


           binding?.layoutMyPost?.tvPostDesc?.text = communityHomeResponseModel.data[0].description
           binding?.layoutMyPost?.dateTime?.text = getMessage(R.string.posted_on).plus(" ")
               .plus(communityHomeResponseModel.data[0].createdDate)
           binding?.layoutMyPost?.tvLikes?.text =
               communityHomeResponseModel.data[0].likesCount.toString().plus(" ")
                   .plus(getMessage(R.string.like))
           binding?.layoutMyPost?.tvComment?.text =
               communityHomeResponseModel.data[0].commentsCount.toString().plus(" ")
                   .plus(getMessage(R.string.comment_count))

           binding?.layoutFavorite?.tvPostCount?.text =  communityHomeResponseModel.meta.pages.toString()

           if (communityHomeResponseModel.data[0].images != null && communityHomeResponseModel.data[0].images.size > 0)
               Glide.with(this).load(communityHomeResponseModel.data[0].images[0].url)
                   .into(binding?.layoutMyPost?.ivPost!!)

           binding?.layoutMyPost?.tvGoToPosts?.setOnClickListener {
               if (activity is HomeActivity) {
                   (activity as HomeActivity).showCommunityFragment("gramophone")
               }
           }

           binding?.layoutMyPost?.tvLikes?.setOnClickListener {
               openActivity(
                   LikedPostUserListActivity::class.java,
                   Bundle().apply {
                       putString(Constants.POST_ID, communityHomeResponseModel.data[0]._id)
                   })
           }

           binding?.layoutMyPost?.tvComment?.setOnClickListener {
               openActivity(
                   CommentsActivity::class.java,
                   Bundle().apply {
                       putString(Constants.POST_ID, communityHomeResponseModel.data[0]._id)
                   })
           }

       }else{
           binding?.layoutMyPost?.itemPost?.visibility= GONE
           binding?.layoutMyPost?.tvGoToPosts?.visibility= GONE
           binding?.layoutMyPost?.ivNext1?.visibility= GONE
           binding?.layoutMyPost?.llNoPost?.visibility= VISIBLE

       }
        binding?.layoutMyPost?.myPostTitle?.text =
            String.format(getMessage(R.string.my_post), communityHomeResponseModel.meta.pages)

        binding?.layoutMyPost?.llCreatePost?.setOnClickListener {
            openActivity(CreatePostActivity::class.java)
        }

        binding?.layoutMyPost?.ivLike?.setOnClickListener {
            myGramophoneFragmentViewModel.likePost(communityHomeResponseModel.data[0]._id)
        }



    }

    override fun updateLike(data: LikeUpdate?) {
        binding?.layoutMyPost?.tvLikes?.text = data?.likesCount.toString().plus(" ").plus(getMessage(R.string.like))

        if (data?.liked == true){
            binding?.layoutMyPost?.ivLike?.setImageResource(R.drawable.ic_liked)
        }else{
            binding?.layoutMyPost?.ivLike?.setImageResource(R.drawable.ic_like)
        }
    }

    override fun updateOrderSection(placedList: GpApiResponseData?) {

        if (placedList==null){
            binding?.layoutOrder?.llNoOrder?.visibility= VISIBLE
            binding?.layoutOrder?.rlCheckoutOffer?.visibility= VISIBLE
            binding?.layoutOrder?.rlOrder?.visibility= GONE
            binding?.layoutOrder?.tvGoToOrders?.visibility= GONE
            binding?.layoutOrder?.ivNext1?.visibility= GONE

            binding?.layoutOrder?.rlCheckoutOffer?.setOnClickListener {
                openActivity(OffersListActivity::class.java)
            }
        }else{
            binding?.layoutOrder?.llNoOrder?.visibility= GONE
            binding?.layoutOrder?.rlOrder?.visibility= VISIBLE
            binding?.layoutOrder?.myOrderTitle?.text = String.format(getMessage(R.string.total_orders),placedList.total)
            binding?.layoutOrder?.tvOrderNumber?.text = "#".plus(placedList.data[0].order_id.toString())
            binding?.layoutOrder?.tvTotalAmount?.text = getMessage(R.string.rupee).plus(placedList.data[0].price.toString())
            binding?.layoutOrder?.dateTime?.text = placedList.data[0].order_date.toString().plus("/").plus(placedList.data[0].quantity).plus("Items")
            binding?.layoutOrder?.tvOrderStatus?.text = placedList.data[0].order_status_name.toString()

            if (placedList.data[0].product_image!=null)
                Glide.with(this).load(placedList.data[0].product_image).into(binding?.layoutOrder?.productImage!!)

            binding?.layoutOrder?.tvDetail?.setOnClickListener {
                openActivity(OrderDetailsActivity::class.java,Bundle().apply {
                    putString(Constants.ORDER_ID, placedList.data[0].order_id.toString())
                    putString(Constants.ORDER_TYPE, Constants.PLACED)
                })
            }

            binding?.layoutOrder?.tvGoToOrders?.setOnClickListener {
                openActivity(OrderListActivity::class.java)
            }
        }


        binding?.layoutOrder?.btnShopNow?.setOnClickListener {
            if (activity is HomeActivity) {
                (activity as HomeActivity).showHomeFragment()
            }
        }


    }

    override fun updateFarms(farms: FarmResponse?) {

        if (farms?.gp_api_response_data?.customer_farm?.data==null){
           binding?.llNoFarm?.visibility= VISIBLE
           binding?.tvGoToFarms?.visibility= GONE
           binding?.ivNext?.visibility= GONE
           binding?.llLayoutFarm?.visibility= GONE
            binding?.myFarmsTitle?.text = String.format(
                getMessage(R.string.myfarm),
                0
            )
        }else{
            binding?.llNoFarm?.visibility= GONE
            binding?.tvGoToFarms?.visibility= VISIBLE
            binding?.ivNext?.visibility= VISIBLE
            binding?.llLayoutFarm?.visibility= VISIBLE

            binding?.myFarmsTitle?.text = String.format(
                getMessage(R.string.myfarm),
                farms.gp_api_response_data.customer_farm.total
            )
            binding?.layoutFarm?.txtCropName?.text =
                farms.gp_api_response_data.customer_farm.data[0][0].crop_name
            binding?.layoutFarm?.txtCropCode?.text =
                farms.gp_api_response_data.customer_farm.data[0][0].farm_ref_id
            binding?.layoutFarm?.txtDate?.text = String.format(
                getMessage(R.string.crop_sowing_date),
                farms.gp_api_response_data.customer_farm.data[0][0].crop_sowing_date
            )
            binding?.layoutFarm?.txtArea?.text = String.format(
                getMessage(R.string.farm_area),
                farms.gp_api_response_data.customer_farm.data[0][0].farm_area
            )

            if (farms.gp_api_response_data.customer_farm.data[0][0].duration != null)
                binding?.layoutFarm?.txtDuration?.text = String.format(
                    getMessage(R.string.crop_duration),
                    farms.gp_api_response_data.customer_farm.data[0][0].duration
                )
            else {
                binding?.layoutFarm?.txtDuration?.visibility = GONE
            }
            binding?.layoutFarm?.txtStage?.text = String.format(
                getMessage(R.string.stage_name),
                farms.gp_api_response_data.customer_farm.data[0][0].days,
                farms.gp_api_response_data.customer_farm.data[0][0].stage_name
            )
            if (farms.gp_api_response_data.customer_farm.data[0][0].crop_image != null) {
                Glide.with(this).load(farms.gp_api_response_data.customer_farm.data[0][0].crop_image)
                    .into(binding?.layoutFarm?.imageSelectCorpItem!!)
            }
        }
        binding?.tvGoToFarms?.setOnClickListener {
            openActivity(ViewAllFarmsActivity::class.java)
        }

        binding?.btnAddfarm?.setOnClickListener {
            openActivity(SelectCropActivity::class.java)
        }

        binding?.layoutFarm?.txtAddFarm?.setOnClickListener {
            openActivity(AddFarmActivity::class.java, Bundle().apply {
                putParcelable("selectedCrop", CropData().apply {
                    cropImage= farms?.gp_api_response_data?.customer_farm?.data!![0][0].crop_image
                    cropId = farms?.gp_api_response_data?.customer_farm?.data!![0][0].crop_id
                    cropName = farms?.gp_api_response_data?.customer_farm?.data!![0][0].crop_name
                })
            })
        }


    }

    override fun updateMyFavoriteSection(myGramophoneResponseModel: MyGramophoneResponseModel) {
        binding?.layoutFavorite?.tvProductCount?.text =
            myGramophoneResponseModel.gp_api_response_data.my_gramophone_stats.products.toString()
        binding?.layoutFavorite?.tvArticleCount?.text =
            myGramophoneResponseModel.gp_api_response_data.my_gramophone_stats.articles.toString()
        binding?.layoutFavorite?.tvTVCount?.text =
            myGramophoneResponseModel.gp_api_response_data.my_gramophone_stats.gramophone_tv.toString()

        binding?.layoutFavorite?.llPostLinearLayout?.setOnClickListener {
            if (activity is HomeActivity) {
                (activity as HomeActivity).showCommunityFragment("gramophone")
            }
        }

        binding?.layoutFavorite?.llArticleLinearLayout?.setOnClickListener {
              openActivity(ArticlesWebViewActivity::class.java, Bundle().apply {
                  putString(Constants.PAGE_URL,
                      BuildConfig.BASE_URL_SINGLE_ARTICLE+"/")

                  putString(Constants.PAGE_SOURCE,
                      "gramo")
              })
        }

        binding?.layoutFavorite?.llProductLinearLayout?.setOnClickListener {
            if (activity is HomeActivity) {
                (activity as HomeActivity).showHomeFragment()
            }
        }

        binding?.layoutFavorite?.llTVLinearLayout?.setOnClickListener {

        }

    }

    override fun processGenericUri(genericUri: Uri) {
    }


}