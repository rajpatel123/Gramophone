package agstack.gramophone.ui.home.adapter

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.databinding.ItemCommentsBinding
import agstack.gramophone.ui.comments.model.Data
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amnix.xtension.extensions.isNotNullOrEmpty
import com.bumptech.glide.Glide

class CommentsAdapter(val items:List<Data>?) :
    RecyclerView.Adapter<CommentsAdapter.CardViewHolder>() {
    private var lastSelectPosition: Int = 0
    lateinit var context: Context
    var onDeleteComment: ((data: Data) -> Unit)? = null
    var onEditCommentMenuClicked: ((data: Data) -> Unit)? = null
    var onImageViewClciked: ((data: Data) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        context = parent.context
        return CardViewHolder(
            ItemCommentsBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun getItemCount(): Int = items?.size!!

    override fun onBindViewHolder(holder: CardViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val comment = items?.get(holder.absoluteAdapterPosition)
        holder.binding.setVariable(BR.model, comment)
        if (comment?.author?.photoUrl != null) {
            Glide.with(context).load(comment?.author?.photoUrl).into(holder.binding.ivProfileImage1)

        }

        if (comment?.author?.uuid.equals(SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.UUIdKey))){
            holder.binding.ivCommentsActions.visibility= VISIBLE
        }else{
            holder.binding.ivCommentsActions.visibility= GONE
        }
        if (comment?.isSelected == true){
           holder.binding.cvMenu.visibility= VISIBLE
        }else{
            holder.binding.cvMenu.visibility= GONE
        }

        if (comment?.author?.username.isNotNullOrEmpty()){
            holder.binding.tvName.text = comment?.author?.username
        }else{
            holder.binding.tvName.text = context.getString(R.string.anonymous)
        }
        holder.binding.ivCommentsActions.setOnClickListener {
            if (holder.binding.cvMenu.visibility== VISIBLE){
                holder.binding.cvMenu.visibility= GONE
                return@setOnClickListener
            }

            holder.binding.cvMenu.visibility= GONE
            items?.get(lastSelectPosition)?.isSelected = false
            lastSelectPosition = holder.absoluteAdapterPosition
            comment?.isSelected = true
            notifyDataSetChanged()
        }

        holder.binding.llEdit.setOnClickListener {
            onEditCommentMenuClicked?.invoke(comment!!)
            holder.binding.cvMenu.visibility= GONE
        }

        holder.binding.llDelete.setOnClickListener {
            onDeleteComment?.invoke(comment!!)
            holder.binding.cvMenu.visibility= GONE
        }
        if (comment?.image != null) {
            Glide.with(context).load(comment?.image).into(holder.binding.commentImage)
            holder.binding.imageContainer.visibility=VISIBLE

            holder.binding.imageContainer.setOnClickListener {
             onImageViewClciked?.invoke(comment)
            }
        }else{
            holder.binding.imageContainer.visibility= GONE
        }


    }
    class CardViewHolder(var binding: ItemCommentsBinding) :
        RecyclerView.ViewHolder(binding.root)
}