package agstack.gramophone.ui.home.adapter

import agstack.gramophone.BR
import agstack.gramophone.databinding.ItemCommentsBinding
import agstack.gramophone.ui.comments.model.Data
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CommentsAdapter(val items:List<Data>?) :
    RecyclerView.Adapter<CommentsAdapter.CardViewHolder>() {
    private var lastSelectPosition: Int = 0
    lateinit var context: Context
    var onItemCommentsClicked: ((commentId: String) -> Unit)? = null
    var onTripleDotMenuClicked: ((data: Data) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        context = parent.context
        return CardViewHolder(
            ItemCommentsBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun getItemCount(): Int = items?.size!!

    override fun onBindViewHolder(holder: CardViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val comment = items?.get(holder.adapterPosition)
        holder.binding.setVariable(BR.model, comment)
        if (comment?.author?.photoUrl != null) {
            Glide.with(context).load(comment?.author?.photoUrl).into(holder.binding.ivProfileImage1)

        }


        holder.binding.ivCommentsActions.setOnClickListener {
            items?.get(lastSelectPosition)?.isSelected = false
            lastSelectPosition = holder.adapterPosition
            comment?.isSelected = true
            notifyDataSetChanged()
            onTripleDotMenuClicked?.invoke(comment!!)
        }

        if (comment?.image != null) {
            Glide.with(context).load(comment?.image).into(holder.binding.commentImage)
            holder.binding.imageContainer.visibility=VISIBLE
        }else{
            holder.binding.imageContainer.visibility= GONE
        }




    }
    class CardViewHolder(var binding: ItemCommentsBinding) :
        RecyclerView.ViewHolder(binding.root)
}