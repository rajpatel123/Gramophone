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



}