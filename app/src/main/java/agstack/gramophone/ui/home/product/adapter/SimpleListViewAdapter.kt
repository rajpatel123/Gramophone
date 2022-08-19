package agstack.gramophone.ui.home.product.adapter

import agstack.gramophone.R
import agstack.gramophone.BR
import agstack.gramophone.databinding.ItemSimpleListBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil

class SimpleListViewAdapter(list: ArrayList<String>) : BaseAdapter(),Filterable {


    var spinnerList = list

    var onItemClick: ((String) -> Unit)? = null


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var mBinding: ItemSimpleListBinding? = null
        var view = convertView

        if (convertView == null) {
            mBinding = DataBindingUtil.bind<ItemSimpleListBinding>(
                LayoutInflater.from(parent?.context).inflate(R.layout.item_simple_list, null)
            )

            mBinding!!.setVariable(BR.model, spinnerList[position])
            view = mBinding.root


            mBinding.tvCategory.setOnClickListener {

                onItemClick?.invoke(spinnerList[position])

            }


        }

        return view!!
    }

    override fun getItem(position: Int): String {
        return spinnerList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    override fun getCount(): Int {
        return spinnerList.size
    }


    /*
    *
    * click listener for item selected
    * */

    fun OnItemSelectedListener(onItemClick: (String) -> Unit) {
        this.onItemClick = onItemClick
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                return FilterResults().apply { values = spinnerList }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                results?.values as ArrayList<String>
                notifyDataSetChanged()
            }

        }
    }




}