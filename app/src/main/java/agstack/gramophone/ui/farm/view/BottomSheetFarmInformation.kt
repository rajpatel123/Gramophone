package agstack.gramophone.ui.farm.view

import agstack.gramophone.databinding.BottomSheetFarmInformationBinding
import agstack.gramophone.ui.farm.model.unit.GpApiResponseData
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFarmInformation(
    private val listener: (String, Int, String) -> Unit
) :
    BottomSheetDialogFragment() {
    var binding: BottomSheetFarmInformationBinding? = null

    var units: List<GpApiResponseData>? = null
    var selectedUnit: GpApiResponseData? = null
    var farmRefId: String? = null

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
            val farm_reference_id = farmRefId
            var output_qty = 0
            var unit_id = selectedUnit?.unit_id

            try {
                output_qty = binding?.edtArea?.text.toString().toInt()
            } catch (nfe: NumberFormatException) {
                // not a valid int
            }

            if (farm_reference_id.isNullOrEmpty()) {
                showToast("Invalid farm selected")
                return@setOnClickListener
            }
            if (output_qty < 1) {
                showToast("Invalid Quantity selected")
                return@setOnClickListener
            }

            if (unit_id.isNullOrEmpty()) {
                showToast("Invalid unit selected")
                return@setOnClickListener
            }

            listener.invoke(farm_reference_id, output_qty, unit_id)
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
        units = arguments?.getParcelableArrayList<GpApiResponseData>("unitsList")
        farmRefId = arguments?.getString("farm_ref_id")

        context?.let { context ->
            units?.let {
                val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, it)
                binding?.edtAreaUnit?.adapter = adapter
            }
        }
    }

    fun showToast(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}