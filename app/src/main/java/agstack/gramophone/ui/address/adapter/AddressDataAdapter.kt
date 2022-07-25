package agstack.gramophone.ui.address.adapter

import agstack.gramophone.R
import agstack.gramophone.databinding.ItemAddressDataNameBinding
import agstack.gramophone.ui.address.model.AddressDataModel
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter


class AddressDataAdapter(context: Context, var items: List<AddressDataModel> = arrayListOf()) :
    ArrayAdapter<AddressDataModel>(context, R.layout.item_address_data_name, items) {
    var selectedItem: ((AddressDataModel) -> Unit)? = null

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val binding = ItemAddressDataNameBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        val model= items[position]
        binding.model = model
        binding.itemTitle.setOnClickListener {
            selectedItem?.invoke(model)
        }


        return binding.root
    }

    override fun getItem(position: Int): AddressDataModel? {
        return items[position]
    }

    override fun getCount(): Int {
        return items.size
    }


    fun notifyList(dataList1: ArrayList<AddressDataModel>) {
        this.items = dataList1
        notifyDataSetChanged()
    }
}