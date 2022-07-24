package agstack.gramophone.ui.address.adapter

import agstack.gramophone.databinding.ItemAddressDataNameBinding
import agstack.gramophone.ui.address.model.AddressDataModel
import agstack.gramophone.ui.language.model.languagelist.Language
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter


class AddressDataAdapter(context: Context, var items: List<AddressDataModel> = arrayListOf()) :
    ArrayAdapter<AddressDataModel>(context, 0, items) {
    var selectedItem: ((AddressDataModel) -> Unit)? = null

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val binding = ItemAddressDataNameBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        binding.model = items[position]

        return binding.root
    }

    override fun getCount(): Int {
        return items.size
    }


    fun notifyList(dataList1: ArrayList<AddressDataModel>) {
        this.items = dataList1
        notifyDataSetChanged()
    }
}