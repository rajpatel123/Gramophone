package agstack.gramophone.ui.apptour.adapter

import agstack.gramophone.BR
import agstack.gramophone.databinding.CardItemsBinding
import agstack.gramophone.ui.language.model.LoginBanner
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class DotIndicatorPager2Adapter(val items: List<LoginBanner>) :
    RecyclerView.Adapter<DotIndicatorPager2Adapter.CardViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder(
            CardItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val loginBanner = items[position]
        holder.binding.setVariable(BR.model, loginBanner)
    }

    inner class CardViewHolder(var binding: CardItemsBinding) :
        RecyclerView.ViewHolder(binding.root)
}
