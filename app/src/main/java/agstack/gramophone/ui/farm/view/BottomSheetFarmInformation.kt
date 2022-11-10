package agstack.gramophone.ui.farm.view

import agstack.gramophone.R
import agstack.gramophone.databinding.BottomSheetFarmInformationBinding
import agstack.gramophone.ui.farm.model.unit.GpApiResponseData
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFarmInformation(
    private val listener: (String, Int, String) -> Unit
) :
    BottomSheetDialogFragment() {
    var binding: BottomSheetFarmInformationBinding? = null

    var units: List<GpApiResponseData>? = null
    var selectedUnit: GpApiResponseData? = null
    var farmRefId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogThemeNoFloating)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetFarmInformationBinding.inflate(inflater)
        binding?.ivCloseDialog?.setOnClickListener {
            dismiss()
        }

        binding?.submit?.setOnClickListener {
            val farmReferenceId = farmRefId
            var outputQty = 0
            val unitId = selectedUnit?.unit_id

            try {
                outputQty = binding?.edtArea?.text.toString().toInt()
            } catch (nfe: NumberFormatException) {
                showToast(getString(R.string.invalid_value))
            }

            if (farmReferenceId.isNullOrEmpty()) {
                showToast(getString(R.string.invalid_farm_selected))
                return@setOnClickListener
            }
            if (outputQty < 1) {
                showToast(getString(R.string.Invalid_quantity_selected))
                return@setOnClickListener
            }

            if (unitId.isNullOrEmpty()) {
                showToast(getString(R.string.Invalid_unit_selected))
                return@setOnClickListener
            }

            listener.invoke(farmReferenceId, outputQty, unitId)
        }

        binding?.edtAreaUnit?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedUnit = units?.get(position)
            }
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        units = arguments?.getParcelableArrayList("unitsList")
        farmRefId = arguments?.getString("farm_ref_id")

        context?.let { context ->
            units?.let {
                val adapter = ArrayAdapter(context, R.layout.simple_list_item, it)
                binding?.edtAreaUnit?.adapter = adapter
            }
        }
    }

    fun showToast(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}