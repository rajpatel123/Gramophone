package agstack.gramophone.ui.offerslist

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.promotions.PromotionsRepository
import agstack.gramophone.ui.home.view.LostConnectionActivity
import agstack.gramophone.ui.offer.OfferDetailActivity
import agstack.gramophone.ui.offerslist.adapter.OffersListAdapter
import agstack.gramophone.ui.offerslist.model.DataItem
import agstack.gramophone.ui.offerslist.model.OfferListRequestModel
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.os.Bundle
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.miguelcatalan.materialsearchview.MaterialSearchView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject


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
    var searchedString: String? = ""
    var noInternetFound = ObservableField<Boolean>(false)

    fun getAllOffersData() {
        if (offersJob!=null){
            offersJob.cancelIfActive()
        }
        offersJob = checkNetworkThenRun {
            progressLoader.set(true)
            try {

                val languageCode = getNavigator()?.getLanguageCode()

                offerListRequestModel.limit = 10
                offerListRequestModel.requested_source = "app"
                offerListRequestModel.customer_id = SharedPreferencesHelper.instance?.getString(
                    SharedPreferencesKeys.CUSTOMER_ID
                )
                offerListRequestModel.search = searchedString
                offerListRequestModel.page = 1

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
                    if (allOfferslist.size == 0) {
                        getNavigator()?.ShowNoListView(true)

                    } else {
                        getNavigator()?.ShowNoListView(false)
                    }
                } else {
                    getNavigator()?.showToast(offersListResponse.body()?.gpApiMessage)
                }
            } catch (ex: Exception) {
                getNavigator()?.ShowNoListView(true)
                when (ex) {
                    is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.network_failure)!!)
//                    else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
                }
            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        getFilteredListFromAPI(query, 1)
        return true
    }

    private fun getFilteredListFromAPI(query: String?, currentPage: Int) {

        searchedString = query

        offersJob.cancelIfActive()
        getNavigator()?.showLoaderFooter()
        var filteredOfferList: ArrayList<DataItem?> = ArrayList()
        offersJob = viewModelScope.launch {


            offerListRequestModel.search = searchedString
            val offersListResponse = promotionsRepository.getAllOffersList(offerListRequestModel)

            if (offersListResponse.body()?.gpApiStatus.equals(Constants.GP_API_STATUS)) {

                filteredOfferList.addAll(offersListResponse.body()?.gpApiResponseData?.data as ArrayList<DataItem>)



                allOfferslist.clear()
                allOfferslist.addAll(filteredOfferList)
                val lastpage = offersListResponse.body()?.gpApiResponseData?.lastPage
                if (currentPage == lastpage) {
                    isLastPage = true

                }
                getNavigator()?.onListUpdated()

            }
        }

    }

    override fun onQueryTextChange(newText: String?): Boolean {

        newText?.let {
            if (newText?.length >= 3) {
                onQueryTextSubmit(newText)
                return true
            } else if (newText.length == 0) {
                onSearchViewClosed()
                return true
            }
        }
        return true
    }

    override fun onSearchViewShown() {

    }

    override fun onSearchViewClosed() {
        getFilteredListFromAPI("", 1)

    }


    fun loadMore(currentPage: Int) {


        offersJob.cancelIfActive()
        getNavigator()?.showLoaderFooter()
        offersJob = viewModelScope.launch {
            offerListRequestModel.page = currentPage


            val offersListResponse =
                promotionsRepository.getAllOffersList(offerListRequestModel)

            if (offersListResponse.body()?.gpApiStatus.equals(Constants.GP_API_STATUS)) {
                allOfferslist.addAll(offersListResponse.body()?.gpApiResponseData?.data as ArrayList<DataItem>)

                val lastpage = offersListResponse.body()?.gpApiResponseData?.lastPage
                if (currentPage >= lastpage!!) {
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

    fun onRetryButtonClick() {
        if (getNavigator()?.isNetworkAvailable() == true) {
            noInternetFound.set(false)
        }
        getAllOffersData()

    }


}
