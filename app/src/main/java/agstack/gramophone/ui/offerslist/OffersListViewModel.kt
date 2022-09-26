package agstack.gramophone.ui.offerslist

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.promotions.PromotionsRepository
import agstack.gramophone.data.repository.settings.SettingsRepository
import agstack.gramophone.ui.home.view.fragments.market.model.PromotionListItem
import agstack.gramophone.ui.offer.OfferDetailActivity

import agstack.gramophone.ui.offerslist.adapter.OffersListAdapter
import agstack.gramophone.ui.offerslist.model.DataItem
import agstack.gramophone.ui.offerslist.model.OfferListRequestModel
import agstack.gramophone.ui.referandearn.transaction.TransactionRequestModel
import agstack.gramophone.ui.referandearn.transaction.model.GramcashTxnItem
import agstack.gramophone.ui.userprofile.model.UpdateProfileModel
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.miguelcatalan.materialsearchview.MaterialSearchView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
import android.os.Bundle


@HiltViewModel
class OffersListViewModel @Inject constructor(
    private val promotionsRepository: PromotionsRepository
) : BaseViewModel<OffersListNavigator>(), MaterialSearchView.OnQueryTextListener,
    MaterialSearchView.SearchViewListener {

    var allOfferslist: ArrayList<DataItem?> = ArrayList()
    private var offersJob: Job? = null
    var progressLoader = ObservableField<Boolean>(false)
    var isLastPage = false
    var offerListRequestModel = OfferListRequestModel()

    fun getAllOffersData() {


        offersJob.cancelIfActive()
        offersJob = checkNetworkThenRun {
            progressLoader.set(true)
            try {

                val languageCode = getNavigator()?.getLanguageCode()

                offerListRequestModel.limit = 10
                offerListRequestModel.requested_source = "b2c"
                offerListRequestModel.customer_id = SharedPreferencesHelper.instance?.getString(
                    SharedPreferencesKeys.CUSTOMER_ID
                )

                offerListRequestModel.business_type = "customer"
                offerListRequestModel.language = languageCode

                val offersListResponse =
                    promotionsRepository.getAllOffersList(offerListRequestModel)
                progressLoader.set(false)
                if (offersListResponse.body()?.gpApiStatus!!.equals(Constants.GP_API_STATUS)) {
                    val promotionList: List<DataItem?>? =
                        offersListResponse.body()?.gpApiResponseData?.data
                    promotionList?.let {
                        allOfferslist = promotionList as ArrayList<DataItem?>
                        getNavigator()?.setOffersListAdapter(OffersListAdapter(allOfferslist)) {


                            getNavigator()?.openActivity(
                                OfferDetailActivity::class.java,
                                Bundle().apply {
                                    putParcelable(Constants.OFFERSDATA_OFFERSLIST, it)

                                })
                        }
                    }

                    getNavigator()?.showToast(offersListResponse.body()?.gpApiMessage)

                } else {

                    getNavigator()?.showToast(offersListResponse.body()?.gpApiMessage)
                }
            } catch (ex: Exception) {
                when (ex) {
                    is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.network_failure)!!)
                    else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
                }
            }
        }


    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    override fun onSearchViewShown() {

    }

    override fun onSearchViewClosed() {

    }


    fun loadMore(currentPage: Int) {


        offersJob.cancelIfActive()
        getNavigator()?.showLoaderFooter()
        offersJob = viewModelScope.launch {


            val offersListResponse =
                promotionsRepository.getAllOffersList(offerListRequestModel)

            if (offersListResponse.body()?.gpApiStatus.equals(Constants.GP_API_STATUS)) {
                allOfferslist.addAll(offersListResponse.body()?.gpApiResponseData?.data as ArrayList<DataItem>)

                val lastpage = offersListResponse.body()?.gpApiResponseData?.lastPage
                if (currentPage == lastpage) {
                    isLastPage = true

                }
                getNavigator()?.onListUpdated()
            } else {
                getNavigator()?.onListUpdated()
            }
        }
    }


    private fun checkNetworkThenRun(runCode: (suspend () -> Unit)): Job {
        return viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    runCode.invoke()
                } else {
                    getNavigator()?.showToast(R.string.nointernet)
                }
            } catch (e: Exception) {
                Log.d("Exception", e.toString())
            }
        }
    }


}
