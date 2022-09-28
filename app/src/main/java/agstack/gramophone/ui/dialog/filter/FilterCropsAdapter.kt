package agstack.gramophone.ui.dialog.filter


import agstack.gramophone.databinding.ItemSubFilterBinding
import agstack.gramophone.ui.home.subcategory.model.Crops
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class FilterCropsAdapter(list: List<Crops>,
                         private val listener: () -> Unit,) :
    RecyclerView.Adapter<FilterCropsAdapter.CustomViewHolder>() {
    private val cropsList = list

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemSubFilterBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.binding.chkName.text = cropsList[position].crop_name
        holder.binding.chkName.isChecked = cropsList[position].isChecked
        holder.binding.chkName.setOnClickListener {
            cropsList[position].isChecked = !cropsList[position].isChecked
            notifyDataSetChanged()
            listener.invoke()
        }
    }

    override fun getItemCount(): Int {
        return cropsList.size
    }

    inner class CustomViewHolder(var binding: ItemSubFilterBinding) :
        RecyclerView.ViewHolder(binding.root)
}