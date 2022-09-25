package agstack.gramophone.ui.home.adapter

import agstack.gramophone.BR
import agstack.gramophone.databinding.ItemCommentsBinding
import agstack.gramophone.ui.apptour.adapter.DotIndicatorPager2Adapter
import agstack.gramophone.ui.postdetails.model.LastComment
import agstack.gramophone.ui.postdetails.model.comments.Data
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_post_details.*

class CommentsAdapter(val items: List<Data>?) :
    RecyclerView.Adapter<CommentsAdapter.CardViewHolder>() {
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        context = parent.context
        return CardViewHolder(
            ItemCommentsBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun getItemCount(): Int = items?.size!!

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val comment = items?.get(position)
        holder.binding.setVariable(BR.model, comment)
        if (comment?.author?.photoUrl != null) {
            Glide.with(context).load(comment?.author?.photoUrl).into(holder.binding.ivProfileImage1)

        }


    }
    class CardViewHolder(var binding: ItemCommentsBinding) :
        RecyclerView.ViewHolder(binding.root)
}