package agstack.gramophone.ui.dialog.filter


import agstack.gramophone.databinding.ItemSubFilterBinding
import agstack.gramophone.ui.home.subcategory.model.TechnicalData
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class FilterTechnicalAdapter(
    list: List<TechnicalData>,
    private val listener: () -> Unit,
) :
    RecyclerView.Adapter<FilterTechnicalAdapter.CustomViewHolder>() {
    private val technicalList = list

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemSubFilterBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.binding.chkName.text = technicalList[position].technical_name
        holder.binding.chkName.isChecked = technicalList[position].isChecked
        holder.binding.chkName.setOnClickListener {
            technicalList[position].isChecked = !technicalList[position].isChecked
            notifyDataSetChanged()
            listener.invoke()
        }
    }

    override fun getItemCount(): Int {
        return technicalList.size
    }

    inner class CustomViewHolder(var binding: ItemSubFilterBinding) :
        RecyclerView.ViewHolder(binding.root)
}