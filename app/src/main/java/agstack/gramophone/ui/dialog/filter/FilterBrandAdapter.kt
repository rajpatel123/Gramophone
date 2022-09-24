package agstack.gramophone.ui.dialog.filter


import agstack.gramophone.databinding.ItemSubFilterBinding
import agstack.gramophone.ui.home.subcategory.model.Brands
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class FilterBrandAdapter(list: List<Brands>,
                         private val listener: () -> Unit,) :
    RecyclerView.Adapter<FilterBrandAdapter.CustomViewHolder>() {
    private val brandsList = list

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemSubFilterBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.binding.chkName.text = brandsList[position].brand_name
        holder.binding.chkName.isChecked = brandsList[position].isChecked
        holder.binding.chkName.setOnClickListener {
            brandsList[position].isChecked = !brandsList[position].isChecked
            notifyDataSetChanged()
            listener.invoke()
        }
    }

    override fun getItemCount(): Int {
        return brandsList.size
    }

    inner class CustomViewHolder(var binding: ItemSubFilterBinding) :
        RecyclerView.ViewHolder(binding.root)
}