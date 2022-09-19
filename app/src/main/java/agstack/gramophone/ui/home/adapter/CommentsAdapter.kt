package agstack.gramophone.ui.home.adapter

import agstack.gramophone.databinding.ItemCommentsBinding
import agstack.gramophone.ui.home.view.fragments.community.model.likes.Data
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CommentsAdapter(val items: List<Data>) :
    RecyclerView.Adapter<CommentsAdapter.CardViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder(
            ItemCommentsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val loginBanner = items[position]
    }

    inner class CardViewHolder(var binding: ItemCommentsBinding) :
        RecyclerView.ViewHolder(binding.root)
}
