package agstack.gramophone.ui.farm.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.farm.adapter.SelectCropAdapter
import agstack.gramophone.ui.farm.navigator.SelectCropNavigator
import agstack.gramophone.ui.farm.view.AddFarmActivity
import agstack.gramophone.ui.home.view.fragments.market.model.CropData
import agstack.gramophone.ui.home.view.fragments.market.model.CropResponse
import agstack.gramophone.utils.Constants
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SelectCropViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : BaseViewModel<SelectCropNavigator>() {


    var selectedCrop : CropData? = null
    var progress = MutableLiveData<Boolean>()
    var showSearchView = MutableLiveData<Boolean>()

    var cropResponse: CropResponse? = null

    init {
        progress.value = false
        showSearchView.value = false
    }

    fun getCrops() {
        progress.value = true
        viewModelScope.launch {
            try {
                val cropList = ArrayList<CropData>()
                val response = productRepository.getCrops()
                if (response.isSuccessful && response.body()?.gpApiStatus == Constants.GP_API_STATUS
                    && response.body()?.gpApiResponseData?.cropsList?.size!! > 0
                ) {
                    cropResponse = response.body()
                    cropList.addAll(cropResponse?.gpApiResponseData?.cropsList!!)
                }
                progress.value = false
                // add header item in the list
                if (cropList.size > 0) {
                    cropList.add(
                        0,
                        CropData(
                            isSectionHeader = true,
                            sectionTitle = "Popular in your area",
                            sectionSubTitle = "With best practices and modal farm",
                        )
                    )
                }

                if (cropList.size > 4) {
                    cropList.add(
                        4,
                        CropData(
                            isSectionHeader = true,
                            sectionTitle = "More Crops",
                        )
                    )
                }
                getNavigator()?.updateCropAdapter(cropList)

            } catch (ex: Exception) {
                progress.value = false
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
        }
    }

    fun onClickSaveAndContinue() {
        if (selectedCrop != null) {
            getNavigator()?.openAndFinishActivity(
                AddFarmActivity::class.java,
                Bundle().apply {
                    putParcelable("selectedCrop", selectedCrop)
                })
        } else {
            getNavigator()?.showToast(getNavigator()?.getMessage(R.string.message_select_crop))
        }
    }

    fun clearSelection(){
        selectedCrop = null
    }

    fun onSearchViewBackPress(){
        showSearchView.value = false
        onSearchViewClearClick()
    }

    fun onSearchViewClearClick(){
        getNavigator()?.onSearchViewClearClick()
    }

    fun onSearchMenuItemClick (){
        showSearchView.value = true
    }
}