package agstack.gramophone.ui.search.adapter

import agstack.gramophone.databinding.SubItemCompaniesBinding
import agstack.gramophone.ui.search.model.Item
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CompaniesAdapter  (
    val list: List<Item>,
    private val listener: (String, String?, String?) -> Unit,
): RecyclerView.Adapter<CompaniesAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(SubItemCompaniesBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        holder.binding.item = item

        holder.binding.companyContainer.setOnClickListener {
            listener.invoke(item.id!!, item.name, item.image)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(var binding: SubItemCompaniesBinding) :
        RecyclerView.ViewHolder(binding.root)
}