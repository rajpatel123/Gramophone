package agstack.gramophone.ui.search.adapter

import agstack.gramophone.databinding.SubItemProfileBinding
import agstack.gramophone.ui.search.model.Item
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ProfilesAdapter (
    val list: List<Item>,
    private val listener: (String) -> Unit,
): RecyclerView.Adapter<ProfilesAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(SubItemProfileBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val profile = list[position]
        holder.binding.item = profile

        holder.binding.productDetailsContainer.setOnClickListener {
            listener.invoke(profile.id!!)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(var binding: SubItemProfileBinding) :
        RecyclerView.ViewHolder(binding.root)
}