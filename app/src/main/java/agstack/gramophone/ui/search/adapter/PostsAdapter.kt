package agstack.gramophone.ui.search.adapter

import agstack.gramophone.databinding.SubItemPostBinding
import agstack.gramophone.ui.search.model.Item
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amnix.xtension.extensions.isNotNullOrEmpty
import java.text.SimpleDateFormat
import java.util.*

class PostsAdapter(
    val list: List<Item>,
    private val listener: (String) -> Unit,
) : RecyclerView.Adapter<PostsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(SubItemPostBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val post = list[position]
        holder.binding.item = post

        if (post.description.isNotNullOrEmpty())
            holder.binding.txtPost.text = Html.fromHtml(post.description)

        holder.binding.postContainer.setOnClickListener {
            listener.invoke(post.id!!)
        }

        if(post.createdDate.isNotNullOrEmpty()){
            ("Posted on : " + post.createdDate).also { holder.binding.txtPostedDate.text = it }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun getDateTime(s: Long): String? {
        return try {
            val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)
            val netDate = Date(s)
            sdf.format(netDate)
        } catch (e: Exception) {
            e.toString()
        }
    }

    inner class MyViewHolder(var binding: SubItemPostBinding) :
        RecyclerView.ViewHolder(binding.root)
}