package agstack.gramophone.ui.dialog.filter

import agstack.gramophone.databinding.BottomSheetDialogFilterBinding
import agstack.gramophone.ui.home.subcategory.model.Brands
import agstack.gramophone.ui.home.subcategory.model.Crops
import agstack.gramophone.ui.home.subcategory.model.TechnicalData
import agstack.gramophone.ui.home.view.fragments.market.model.CategoryData
import agstack.gramophone.utils.Constants
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amnix.xtension.extensions.isNull
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetFilterDialog(
    private var mainFilterList: ArrayList<MainFilterData>?,
    private val subCategoryList: List<CategoryData>?,
    private val brandsList: List<Brands>?,
    private val cropsList: List<Crops>?,
    private val technicalDataList: List<TechnicalData>?,
    private val listener: (ArrayList<String>, ArrayList<String>, ArrayList<String>, ArrayList<String>) -> Unit,
) : BottomSheetDialogFragment() {
    var binding: BottomSheetDialogFilterBinding? = null
    private var mainFilterAdapter: MainFilterAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = BottomSheetDialogFilterBinding.inflate(inflater)

        setupUi()
        return binding!!.root
    }

    private fun setupUi() {
        if (mainFilterList.isNullOrEmpty()) mainFilterList = ArrayList()
        else mainFilterList!![0].isSelected = true
        mainFilterAdapter = MainFilterAdapter(mainFilterList!!) { mainFilterType, position ->
            setSubFilterAdapter(mainFilterType, position)
        }
        binding?.rvMainFilter?.adapter = mainFilterAdapter
        // set initially sub filter adapter for first position of main filter
        if (mainFilterList!!.size > 0) setSubFilterAdapter(mainFilterList!![0].name, 0)

        binding?.ivCloseDialog?.setOnClickListener {
            if (!mainFilterList.isNullOrEmpty()) {
                for (i in mainFilterList!!.indices) {
                    mainFilterList!![i].isSelected = i == 0
                }
            }
            dismiss()
        }
        binding?.tvReset?.setOnClickListener {
            if (!mainFilterList.isNullOrEmpty()) {
                for ((index, item) in mainFilterList!!.withIndex()) {
                    if (index == 0) {
                        item.isSelected = true
                        item.count = 0
                    } else {
                        item.isSelected = false
                        item.count = 0
                    }
                }
            }
            if (!subCategoryList.isNullOrEmpty())
                for (item in subCategoryList) {
                    item.isChecked = false
                }
            if (!brandsList.isNullOrEmpty())
                for (item in brandsList) {
                    item.isChecked = false
                }
            if (!cropsList.isNullOrEmpty())
                for (item in cropsList) {
                    item.isChecked = false
                }
            if (!technicalDataList.isNullOrEmpty())
                for (item in technicalDataList) {
                    item.isChecked = false
                }
            listener.invoke(ArrayList(), ArrayList(), ArrayList(), ArrayList())
            dismiss()
        }
        binding?.tvApply?.setOnClickListener {
            val subCategoryIds = ArrayList<String>()
            val brandIds = ArrayList<String>()
            val cropIds = ArrayList<String>()
            val technicalIds = ArrayList<String>()
            if (!mainFilterList.isNullOrEmpty()) {
                for (i in mainFilterList!!.indices) {
                    mainFilterList!![i].isSelected = i == 0
                }
            }
            if (!subCategoryList.isNullOrEmpty()) {
                for (i in subCategoryList.indices) {
                    if (subCategoryList[i].isChecked)
                        subCategoryIds.add(subCategoryList[i].category_id.toString())
                }
            }
            if (!brandsList.isNullOrEmpty()) {
                for (i in brandsList.indices) {
                    if (brandsList[i].isChecked)
                        brandIds.add(brandsList[i].brand_id.toString())
                }
            }
            if (!cropsList.isNullOrEmpty()) {
                for (i in cropsList.indices) {
                    if (cropsList[i].isChecked)
                        cropIds.add(cropsList[i].crop_id.toString())
                }
            }
            if (!technicalDataList.isNullOrEmpty()) {
                for (i in technicalDataList.indices) {
                    if (technicalDataList[i].isChecked)
                        technicalIds.add(technicalDataList[i].technical_code.toString())
                }
            }
            listener.invoke(subCategoryIds, brandIds, cropIds, technicalIds)
            dismiss()
        }
    }

    private fun setSubFilterAdapter(filterType: String, filterPosition: Int) {
        when (filterType) {
            Constants.SUB_CATEGORY -> {
                if (!subCategoryList.isNullOrEmpty())
                    binding?.rvSubFilter?.adapter = FilterSubCategoryAdapter(subCategoryList) {
                        var selectedFilterCount = 0
                        for (item in subCategoryList) {
                            if (item.isChecked) selectedFilterCount++
                        }
                        mainFilterList!![filterPosition].count = selectedFilterCount
                        mainFilterAdapter?.notifyDataSetChanged()
                    }
            }
            Constants.BRAND -> {
                if (!brandsList.isNullOrEmpty())
                    binding?.rvSubFilter?.adapter = FilterBrandAdapter(brandsList) {
                        var selectedFilterCount = 0
                        for (item in brandsList) {
                            if (item.isChecked) selectedFilterCount++
                        }
                        mainFilterList!![filterPosition].count = selectedFilterCount
                        mainFilterAdapter?.notifyDataSetChanged()
                    }
            }
            Constants.CROP -> {
                if (!cropsList.isNullOrEmpty())
                    binding?.rvSubFilter?.adapter = FilterCropsAdapter(cropsList) {
                        var selectedFilterCount = 0
                        for (item in cropsList) {
                            if (item.isChecked) selectedFilterCount++
                        }
                        mainFilterList!![filterPosition].count = selectedFilterCount
                        mainFilterAdapter?.notifyDataSetChanged()
                    }
            }
            Constants.TECHNICAL -> {
                if (!technicalDataList.isNullOrEmpty())
                    binding?.rvSubFilter?.adapter = FilterTechnicalAdapter(technicalDataList) {
                        var selectedFilterCount = 0
                        for (item in technicalDataList) {
                            if (item.isChecked) selectedFilterCount++
                        }
                        mainFilterList!![filterPosition].count = selectedFilterCount
                        mainFilterAdapter?.notifyDataSetChanged()
                    }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}