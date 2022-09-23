package agstack.gramophone.ui.address.adapter

import agstack.gramophone.BR
import agstack.gramophone.databinding.ItemAddressDataNameBinding
import agstack.gramophone.ui.address.model.AddressDataModel
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView

class AddressDataListAdapter(
    private val mContext: Context,
    private val itemLayout: Int,
    private var dataList: List<AddressDataModel>
) : ArrayAdapter<Any>(
    mContext, itemLayout, dataList
) {
    var selectedItem: ((AddressDataModel) -> Unit)? = null

    private val listFilter = ListFilter()
    private var dataListAllItems: List<AddressDataModel>? = null
    override fun getCount(): Int {
        return dataList.size
    }

    override fun getItem(position: Int): String {
        Log.d(
            "CustomListAdapter",
            dataList[position].name
        )
        return dataList[position].name
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
//        var view = view
//        val model = dataList[position]
//        if (view == null) {
//            view = LayoutInflater.from(parent.context)
//                .inflate(itemLayout, parent, false)
//        }
//        val strName = view!!.findViewById<View>(R.id.itemTitle) as TextView
//        strName.text = getItem(position)
//        strName.setOnClickListener {
//            selectedItem?.invoke(model)
//        }
//        return view

        val binding = ItemAddressDataNameBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        val model= dataList[position]
        binding.setVariable(BR.model,model)
        binding.itemTitle.setOnClickListener {
            selectedItem?.invoke(model)
        }


        return binding.root
    }

    override fun getFilter(): Filter {
        return listFilter
    }

    inner class  ListFilter : Filter() {
        private val lock = Any()

        override fun performFiltering(constraint: CharSequence?): FilterResults? {
            val filterResults = FilterResults()
            synchronized(filterResults) {
                if (constraint != null) {
                    // Clear and Retrieve the autocomplete results.
                    val resultList: List<AddressDataModel> =   getFilteredResults(constraint)

                    // Assign the data to the FilterResults
                    filterResults.values = resultList
                    filterResults.count = resultList.size
                }
                return filterResults
            }
        }



         private fun getFilteredResults(prefix: CharSequence?): ArrayList<AddressDataModel> {
             val matchValues = ArrayList<AddressDataModel>()

             val results = FilterResults()
            if (dataListAllItems == null) {
                synchronized(lock) { dataListAllItems = ArrayList(dataList) }
            }
             if (prefix != null) {
                 if (prefix.isEmpty()) {
                     synchronized(lock) {
                         results.values = dataListAllItems
                         results.count = dataListAllItems?.size ?: 0
                     }
                 } else {
                     val searchStrLowerCase = prefix.toString().lowercase()
                     if (dataListAllItems!=null){
                         for (dataItem in dataListAllItems!!) {
                             //*This is the actual line where you can change your logic for startWith or Contains*
                             if (dataItem.name.lowercase().startsWith(searchStrLowerCase)) {
                                 matchValues.add(dataItem)
                             }
                         }
                     }

                 }
             }
            return matchValues
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
             if (results.values != null) {
                 dataList= results.values as ArrayList<AddressDataModel>
            }

            if (dataList!=null && results.count > 0) {
                notifyDataSetChanged()
            } else {
                notifyDataSetInvalidated()
            }
        }
    }
}