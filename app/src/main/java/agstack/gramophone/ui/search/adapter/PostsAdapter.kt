package agstack.gramophone.ui.search.adapter

import agstack.gramophone.R
import agstack.gramophone.databinding.SubItemPostBinding
import agstack.gramophone.ui.search.model.Item
import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.amnix.xtension.extensions.isNotNullOrEmpty
import com.amnix.xtension.extensions.toggle
import java.text.SimpleDateFormat
import java.util.*

class PostsAdapter(
    val list: List<Item>,
    private val listener: (String) -> Unit,
    private val listener2: (String) -> Unit,
) : RecyclerView.Adapter<PostsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(SubItemPostBinding.inflate(LayoutInflater.from(parent.context)))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val post = list[position]
        holder.binding.item = post

        if (post.description.isNotNullOrEmpty())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.binding.txtPost.text=HtmlCompat.fromHtml(post.description!!, HtmlCompat.FROM_HTML_MODE_LEGACY)
            }else{
                holder.binding.txtPost.text=HtmlCompat.fromHtml(post.description!!, HtmlCompat.FROM_HTML_MODE_LEGACY)
            }

        holder.binding.postContainer.setOnClickListener {
            listener.invoke(post.id!!)
        }

        holder.binding.txtLike.setOnClickListener {
            listener2.invoke(post.id!!)

            post.isLiked = post.isLiked.toggle()
            val drawable: Drawable?

            if (post.isLiked) {
                post.likesCount = post.likesCount?.plus(1)
                drawable = ResourcesCompat.getDrawable(
                    holder.binding.txtLike.context.resources,
                    R.drawable.ic_liked,
                    null
                )
            } else {
                post.likesCount = post.likesCount?.minus(1)
                drawable = ResourcesCompat.getDrawable(
                    holder.binding.txtLike.context.resources,
                    R.drawable.ic_like,
                    null
                )
            }

            holder.binding.txtLike.text =
                post.likesCount.toString() + " " +
                        holder.binding.txtLike.context.getString(R.string.like)

            holder.binding.txtLike.setCompoundDrawablesWithIntrinsicBounds(
                drawable,
                null,
                null,
                null
            )
        }

        holder.binding.txtLike.text = post.likesCount.toString() + " " +
                holder.binding.txtLike.context.getString(R.string.like)

        if (post.createdDate.isNotNullOrEmpty()) {
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