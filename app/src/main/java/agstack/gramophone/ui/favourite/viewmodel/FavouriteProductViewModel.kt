package agstack.gramophone.ui.favourite.viewmodel

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.favourite.FavouriteProductAdapter
import agstack.gramophone.ui.favourite.FavouriteProductNavigator
import agstack.gramophone.ui.favourite.model.FavouriteRequestModel
import agstack.gramophone.ui.home.product.activity.ProductDetailsActivity
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.Utility
import android.os.Bundle
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteProductViewModel @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository
):BaseViewModel<FavouriteProductNavigator>() {

    val progress = ObservableField<Boolean>(false)
    val isDataAvailable = ObservableField<Boolean>(true)


    fun getFavouriteProduct(){
     if (getNavigator()?.isNetworkAvailable() == true){
         progress.set(true)
         viewModelScope.launch {
             val response = onBoardingRepository.getFavouriteProduct(FavouriteRequestModel(99,1))
             if (response.isSuccessful){
                 if (response.body()!=null){
                  if (response.body()!!.gp_api_response_data.data.size>0){
                      isDataAvailable.set(true)
                      getNavigator()?.updateProductList(FavouriteProductAdapter(response.body()!!.gp_api_response_data.data)) {
                      getNavigator()?.openActivity(ProductDetailsActivity::class.java, Bundle().apply {
                          putParcelable(Constants.PRODUCT, ProductData().apply {
                              product_id = it.product_id
                          })
                      })
                      }
                  }else{
                      isDataAvailable.set(false)

                  }

                 }else{
                     isDataAvailable.set(false)
                     getNavigator()?.showToast(Utility.getErrorMessage(response.errorBody()))
                 }
             }

             progress.set(false)
         }
     }
    }

}