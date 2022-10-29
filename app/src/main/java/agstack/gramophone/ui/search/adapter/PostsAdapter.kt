package agstack.gramophone.ui.search.adapter

import agstack.gramophone.databinding.SubItemPostBinding
import agstack.gramophone.ui.search.model.Item
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class PostsAdapter  (
    val list: List<Item>,
    private val listener: (String) -> Unit,
): RecyclerView.Adapter<PostsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(SubItemPostBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val post = list[position]
        holder.binding.item = post

        holder.binding.postContainer.setOnClickListener {
            listener.invoke(post.id!!)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(var binding: SubItemPostBinding) :
        RecyclerView.ViewHolder(binding.root)
}