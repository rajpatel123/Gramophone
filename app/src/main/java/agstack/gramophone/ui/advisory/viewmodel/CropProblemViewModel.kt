package agstack.gramophone.ui.advisory.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.advisory.AllCropProblemNavigator
import agstack.gramophone.ui.advisory.adapter.CropIssueListAdapter
import agstack.gramophone.ui.advisory.models.cropproblems.CropProblemRequestModel
import agstack.gramophone.ui.advisory.view.AllCropProblemsActivity
import agstack.gramophone.utils.Constants
import android.os.Bundle
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CropProblemViewModel @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository
):BaseViewModel<AllCropProblemNavigator>() {
    val progress = ObservableField<Boolean>()
    fun getCropProblemList(stageID: Int?, cropId: Int?) {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.set(true)
                    val response =
                        onBoardingRepository.getCropProblems(
                            CropProblemRequestModel(
                                crop_id = cropId!!, stageID!!
                            )
                        )
                    progress.set(false)

                    if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS) {
                        if (response.body()?.gp_api_response_data?.size!! > 0) {
                            val cropIssueListAdapter =
                                CropIssueListAdapter(response.body()?.gp_api_response_data!!)
                            getNavigator()?.setAdvisoryProblemsActivity(cropIssueListAdapter){

                            }
                        }
                    }
                } else {
                    getNavigator()?.showToast(getNavigator()?.getMessage(R.string.no_internet))
                }
            } catch (ex: Exception) {
                progress.set(false)

            }
        }

    }
}