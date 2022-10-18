package agstack.gramophone.ui.dialog.posts

import agstack.gramophone.R
import agstack.gramophone.databinding.ShowPostDateBottomSheetDialogBinding
import agstack.gramophone.ui.createnewpost.adapter.CropTagAdapter
import agstack.gramophone.ui.home.view.fragments.market.model.CropData
import agstack.gramophone.ui.home.view.fragments.market.model.CropResponse
import agstack.gramophone.ui.postdetails.view.PostDetailsActivity
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.DatePicker
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class BottomSheetShowPostDateDialog(
    private val cropResponse: CropResponse,
    private val listener: OnSelectionShowDone
) : BottomSheetDialogFragment(), CropTagAdapter.OnOtherClick,
    BottomSheetCropsDialog.OnSelectionDone,AdapterView.OnItemSelectedListener {
    private var  unit: String? = null
    private var  date: String? = null
    private lateinit var addCropAdapter: CropTagAdapter
    var binding: ShowPostDateBottomSheetDialogBinding? = null
    val cropList = ArrayList<CropData>()
    val cropListOnSheet = ArrayList<CropData>()
     val cal = Calendar.getInstance()
    lateinit var mActivity : PostDetailsActivity


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = activity as  PostDetailsActivity

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = ShowPostDateBottomSheetDialogBinding.inflate(inflater)
        // cropResponse.gpApiResponseData?.cropsList?.set(cropResponse.gpApiResponseData?.cropsList?.size!!+1,CropData(0,getString(R.string.other),"",false,"","",false))
        setupUi()
        return binding!!.root
    }

    private fun setupUi() {
        if (cropResponse.gpApiResponseData?.cropsList?.size!! > 10) {
            cropListOnSheet.addAll(cropResponse.gpApiResponseData?.cropsList!!.subList(0, 7))
        }
        addCropAdapter = CropTagAdapter(cropList = cropListOnSheet, this)
        val layoutManager = GridLayoutManager(activity, 4)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return 1
            }
        }
        binding?.rvTags?.layoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding?.rvTags?.setHasFixedSize(true)
        binding?.rvTags?.adapter = addCropAdapter


        binding?.doneButton?.setOnClickListener {
            addCropAdapter.cropList?.forEach {
                if (it.isSelected) cropList.add(it)
            }

            if (TextUtils.isEmpty(binding!!.tvDate.text)){
                mActivity.showToast(getString(R.string.please_select_date))
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(binding!!.edtArea.text)){
                mActivity.showToast(getString(R.string.please_enter_farm_area))
                return@setOnClickListener
            }

            val farmArea = JSONObject()
            farmArea.put("area",binding!!.edtArea.text.toString())
            farmArea.put("unit",unit)
            listener.onSelectionDone(cropList,farmArea,binding?.tvDate?.text.toString())
            dismiss()
        }



        binding?.unitSpinner?.onItemSelectedListener= this

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }

        // when you click on the button, show DatePickerDialog that is set with OnDateSetListener
        binding?.ivDate!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(
                    activity!!,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }

        })
    }

    private fun updateDateInView() {
        val myFormat = "yyyy/MM/dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding!!.tvDate.text=sdf.format(cal.getTime())
    }

    override fun getTheme(): Int = R.style.CustomBottomSheetDialogTheme
    interface OnSelectionShowDone {
        fun onSelectionDone(cropList: MutableList<CropData>, unit: JSONObject, date: String?)
    }

    override fun onOtherClicked() {
        val bottomSheet = BottomSheetCropsDialog(cropResponse, this)
        bottomSheet.show(
            parentFragmentManager,
            ""
        )
    }

    override fun onCropSelectionDone(cropList: MutableList<CropData>) {

        if (cropList.size > 0) {
            cropList.forEach {
                if (!cropListOnSheet.contains(it)) cropListOnSheet.add(it)
            }
            addCropAdapter.setData(cropListOnSheet)
            addCropAdapter.notifyDataSetChanged()
        }

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
       unit = resources.getStringArray(R.array.units_array)[position]
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

}



