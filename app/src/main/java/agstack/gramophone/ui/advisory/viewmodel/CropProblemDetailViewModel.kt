package agstack.gramophone.ui.advisory.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.advisory.CropProblemDetailNavigator
import agstack.gramophone.ui.advisory.adapter.RecommendedLinkedProductsListAdapter
import agstack.gramophone.ui.advisory.models.recomondedproducts.RecommendedProductRequestModel
import agstack.gramophone.utils.Constants
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.amnix.xtension.extensions.isNotNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CropProblemDetailViewModel @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository
):BaseViewModel<CropProblemDetailNavigator>() {
    val issueName = ObservableField<String>()
    val issueImage = ObservableField<String>()
    val issueDescription = ObservableField<String>()
    val issueType = ObservableField<String>()
    val productCount = ObservableField<String>()



    fun getRecommendedProduct() {
       val bundle =  getNavigator()?.getBundle()

        viewModelScope.launch {
            val response = onBoardingRepository.getRecommendedProducts(
                RecommendedProductRequestModel(bundle?.getInt(Constants.DESEASE_ID)!!))

            if (response.isSuccessful && response.body().isNotNull()){
                productCount.set(" ".plus("").plus(response.body()?.gp_api_response_data!!.size).plus(getNavigator()?.getMessage(
                    R.string.recommended_product)))
              val recommendedLinkedProductsListAdapter= RecommendedLinkedProductsListAdapter(
                  response.body()?.gp_api_response_data!!
              )

                getNavigator()?.setProductList(recommendedLinkedProductsListAdapter){

                }
            }
        }

    }

    fun setDiseaseDetails() {
        val bundle = getNavigator()?.getBundle()
        issueName.set(bundle?.getString(Constants.DESEASE_NAME))
        issueImage.set(bundle?.getString(Constants.DESEASE_IMAGE))
        issueDescription.set(bundle?.getString(Constants.DESEASE_DESC))
        issueType.set(bundle?.getString(Constants.DESEASE_TYPE))
    }
}