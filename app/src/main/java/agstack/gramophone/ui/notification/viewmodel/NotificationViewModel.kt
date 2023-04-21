package agstack.gramophone.ui.notification.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.data.repository.promotions.PromotionsRepository
import agstack.gramophone.ui.advisory.AdvisoryActivity
import agstack.gramophone.ui.advisory.view.CropProblemDetailActivity
import agstack.gramophone.ui.home.featured.FeaturedProductActivity
import agstack.gramophone.ui.home.subcategory.SubCategoryActivity
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.notification.NotificationNavigator
import agstack.gramophone.ui.notification.NotificationsAdapter
import agstack.gramophone.ui.notification.model.NotificationRequestModel
import agstack.gramophone.ui.offerslist.model.OfferDetailRequestModel
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.Utility
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository,
    private val productRepository: ProductRepository,
    private val promotionsRepository: PromotionsRepository
):BaseViewModel<NotificationNavigator>(){
    val limit =10
    val page = 1
    val  progress = ObservableField<Boolean>(false)
    fun getNotification() {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.set(true)
                    var producttoBeAdded = ProductData()
                    producttoBeAdded.product_id = null
                    producttoBeAdded.comments = ""

                    val notificationResponse = onBoardingRepository.getNotification(NotificationRequestModel(limit,page))
                    progress.set(false)


                    if (notificationResponse.body()?.gp_api_status!!.equals(Constants.GP_API_STATUS)) {

                        getNavigator()?.updateNotificationList(NotificationsAdapter(
                            notificationResponse.body()!!.gp_api_response_data.data)) {
//                            val intent = Intent()
//                            intent.data = Uri.parse(it.content.redirectUrl)
                            getNavigator()?.handleDeepLink(it)
//
//                            getNavigator()?.openActivity(
//                                URLHandlerActivity::class.java,
//                                Bundle().apply {
//                                    putString("url", it.content.redirectUrl)
//                                })
//

                        }
                    } else {
                        getNavigator()?.showToast(Utility.getErrorMessage(notificationResponse.errorBody()))
                    }
                } else {
                    getNavigator()?.showToast(getNavigator()?.getMessage(R.string.no_internet))
                }
            } catch (ex: Exception) {
                progress.set(false)
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    //else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
        }
    }
    fun getCategoryDetails(categoryId: String, categoryName: String?, subCatId: String){
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.set(true)
                    var producttoBeAdded = ProductData()
                    producttoBeAdded.product_id = null
                    producttoBeAdded.comments = ""

                    val catResponse = productRepository.getSubCategories(categoryId)
                    progress.set(false)
                    if (catResponse.body()?.gp_api_status!!.equals(Constants.GP_API_STATUS)) {

                        Log.d("Image",""+catResponse.body()?.gp_api_response_data?.product_app_category_image)
                        if (!TextUtils.isEmpty(subCatId)) {
                            getNavigator()?.openAndFinishActivity(FeaturedProductActivity::class.java, Bundle().apply {
                                putString(Constants.SHOP_BY_SUB_CATEGORY, categoryId)
                                putString(Constants.SUB_CATEGORY_ID, subCatId)
                                putString(Constants.SUB_CATEGORY_NAME, categoryName)
                                putString(Constants.SUB_CATEGORY_IMAGE, catResponse.body()?.gp_api_response_data?.product_app_category_image)
                            })
                        }else{
                            getNavigator()?.openAndFinishActivity(
                                SubCategoryActivity::class.java,
                                Bundle().apply {
                                        putString(Constants.CATEGORY_ID, categoryId)
                                        putString(Constants.CATEGORY_NAME, categoryName)
                                        putString(Constants.CATEGORY_IMAGE, catResponse.body()?.gp_api_response_data?.product_app_category_image)
                                })

                        }


                    } else {
                        getNavigator()?.showToast(Utility.getErrorMessage(catResponse.errorBody()))
                    }
                } else {
                    getNavigator()?.showToast(getNavigator()?.getMessage(R.string.no_internet))
                }
            } catch (ex: Exception) {
                progress.set(false)
                getNavigator()?.finishActivity()
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    //else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
        }
    }
    fun getCropDetails(farm_id: String?, crop_id: String?, isCustomerFarms: String?){
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.set(true)
                    val catResponse = onBoardingRepository.getCropDetails(farm_id!!,crop_id.toString())
                    progress.set(false)
                    if (catResponse.body()?.gp_api_status!!.equals(Constants.GP_API_STATUS)) {

                        val data = catResponse?.body()?.gp_api_response_data
                        getNavigator()?.openAndFinishActivity(AdvisoryActivity::class.java,
                            Bundle().apply {
                                putInt(Constants.FARM_ID,
                                  farm_id.toInt()!!
                                )
                                if (isCustomerFarms.equals("true")) {
                                    putString(Constants.FARM_TYPE, "customer_farm")
                                } else {
                                    putString(Constants.FARM_TYPE, "model_farm")
                                }
                                putString(Constants.CROP_NAME,data?.crop_name)
                                putString(Constants.CROP_IMAGE, data?.crop_image)
                                putString(Constants.CROP_REF_ID, data?.farm_ref_id)
                                putInt(Constants.CROP_ID, data?.crop_id!!)
                                putString(Constants.CROP_DURATION, data?.crop_sowing_date)
                                putString(Constants.CROP_END_DATE,
                                    data?.crop_anticipated_completed_date)
                                putString(Constants.CROP_STAGE, data?.stage_name)
                                putString(Constants.CROP_DAYS, "".plus(data?.days))
                            })

                    } else {
                        getNavigator()?.showToast(Utility.getErrorMessage(catResponse.errorBody()))
                        getNavigator()?.finishActivity()
                    }
                } else {
                    getNavigator()?.showToast(getNavigator()?.getMessage(R.string.no_internet))
                    getNavigator()?.finishActivity()
                }
            } catch (ex: Exception) {
                progress.set(false)
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    //else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
                getNavigator()?.finishActivity()

            }
        }
    }


    fun getCropProblemDetails(problemId: String){
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.set(true)
                    val catResponse = onBoardingRepository.getCropProblemDetails(problemId)
                    progress.set(false)
                    if (catResponse.body()?.gp_api_status!!.equals(Constants.GP_API_STATUS)) {

                        val data = catResponse?.body()?.gp_api_response_data
                        getNavigator()?.openActivity(
                            CropProblemDetailActivity::class.java,
                            Bundle().apply {
                                putInt(Constants.DESEASE_ID,problemId.toInt())
                                putString(Constants.DESEASE_NAME,data?.category_name)
                                putString(Constants.DESEASE_DESC,data?.category_description)
                                putString(Constants.DESEASE_IMAGE,data?.category_image)
                                putString(Constants.DESEASE_TYPE,data?.category_type)
                            }
                        )

                    } else {
                        getNavigator()?.showToast(Utility.getErrorMessage(catResponse.errorBody()))
                        getNavigator()?.finishActivity()
                    }
                } else {
                    getNavigator()?.showToast(getNavigator()?.getMessage(R.string.no_internet))
                    getNavigator()?.finishActivity()
                }
            } catch (ex: Exception) {
                progress.set(false)
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                   // else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
                getNavigator()?.finishActivity()

            }
        }
    }


    fun getSubCatDetails(subcatId: String){
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.set(true)
                    val catResponse = onBoardingRepository.getSubCatDetails(subcatId)
                    if (catResponse.body()?.gp_api_status!!.equals(Constants.GP_API_STATUS)) {

                        val data = catResponse?.body()?.gp_api_response_data
                        getNavigator()?. openAndFinishActivity(
                            SubCategoryActivity::class.java,
                            Bundle().apply {
                                putString(Constants.CATEGORY_ID, "".plus(data?.category_id))
                                putString(Constants.CATEGORY_NAME, data?.category_name)
                                putString(Constants.CATEGORY_IMAGE, data?.category_image)
                            })

                    } else {
                        getNavigator()?.showToast(Utility.getErrorMessage(catResponse.errorBody()))
                        getNavigator()?.finishActivity()
                    }
                } else {
                    getNavigator()?.showToast(getNavigator()?.getMessage(R.string.no_internet))
                    getNavigator()?.finishActivity()
                }
            } catch (ex: Exception) {
                progress.set(false)
                when (ex) {
//                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
//                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
                getNavigator()?.finishActivity()

            }
        }
    }


    fun getStoreDetails(storeId: String ){
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.set(true)
                    val catResponse = onBoardingRepository.getStoreDetails(storeId)
                    progress.set(false)
                    if (catResponse.body()?.gp_api_status!!.equals(Constants.GP_API_STATUS)) {

                        val data = catResponse?.body()?.gp_api_response_data
                        getNavigator()?.openAndFinishActivity(FeaturedProductActivity::class.java,
                            Bundle().apply {
                                putString(Constants.STORE_ID, "".plus(data?.id))
                                putString(Constants.STORE_NAME, data?.store_name)
                                putString(Constants.STORE_IMAGE, data?.store_image)
                            })

                    } else {
                        getNavigator()?.showToast(Utility.getErrorMessage(catResponse.errorBody()))
                        getNavigator()?.finishActivity()
                    }
                } else {
                    getNavigator()?.showToast(getNavigator()?.getMessage(R.string.no_internet))
                    getNavigator()?.finishActivity()
                }
            } catch (ex: Exception) {
                progress.set(false)
                when (ex) {
//                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
//                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
                getNavigator()?.finishActivity()

            }
        }
    }


    fun getOfferDetails(pramotionId: Int){
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.set(true)

                    val offerDetailRequestModel = getNavigator()?.getLanguage()
                        ?.let { OfferDetailRequestModel(it,pramotionId,"customer") }
                    val catResponse = offerDetailRequestModel?.let {
                        promotionsRepository.getOfferDetails(
                            it
                        )
                    }
//                    progress.set(false)
//                    if (catResponse.body()?.gp_api_status!!.equals(Constants.GP_API_STATUS)) {
//
//                        val data = catResponse?.body()?.gp_api_response_data
//                        getNavigator()?.openAndFinishActivity(FeaturedProductActivity::class.java,
//                            Bundle().apply {
//                                putString(Constants.STORE_ID, "".plus(data?.id))
//                                putString(Constants.STORE_NAME, data?.store_name)
//                                putString(Constants.STORE_IMAGE, data?.store_image)
//                            })
//
//                    } else {
//                        getNavigator()?.showToast(Utility.getErrorMessage(catResponse.errorBody()))
//                        getNavigator()?.finishActivity()
//                    }
                } else {
                    getNavigator()?.showToast(getNavigator()?.getMessage(R.string.no_internet))
                    getNavigator()?.finishActivity()
                }
            } catch (ex: Exception) {
                progress.set(false)
                when (ex) {
//                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
//                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
                getNavigator()?.finishActivity()

            }
        }
    }




}