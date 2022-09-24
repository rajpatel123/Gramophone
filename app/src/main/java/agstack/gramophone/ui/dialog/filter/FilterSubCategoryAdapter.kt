package agstack.gramophone.ui.dialog.filter


import agstack.gramophone.databinding.ItemSubFilterBinding
import agstack.gramophone.ui.home.view.fragments.market.model.CategoryData
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class FilterSubCategoryAdapter(
    list: List<CategoryData>,
    private val listener: () -> Unit,
) :
    RecyclerView.Adapter<FilterSubCategoryAdapter.CustomViewHolder>() {
    private val subCategoryList = list

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemSubFilterBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.binding.chkName.text = subCategoryList[position].category_name
        holder.binding.chkName.isChecked = subCategoryList[position].isChecked
        holder.binding.chkName.setOnClickListener {
            subCategoryList[position].isChecked = !subCategoryList[position].isChecked
            notifyDataSetChanged()
            listener.invoke()
        }
    }

    override fun getItemCount(): Int {
        return subCategoryList.size
    }

    inner class CustomViewHolder(var binding: ItemSubFilterBinding) :
        RecyclerView.ViewHolder(binding.root)
}