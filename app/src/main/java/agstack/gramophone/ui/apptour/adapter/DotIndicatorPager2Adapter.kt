package agstack.gramophone.ui.apptour.adapter

import agstack.gramophone.R
import agstack.gramophone.ui.apptour.model.Card
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DotIndicatorPager2Adapter(val items: List<Card>) : RecyclerView.Adapter<DotIndicatorPager2Adapter.CardViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.card_items, parent, false)
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
      val card = items[position]
        holder.image.setImageResource(card.id)
        holder.subheading.text = card.message
        holder.pageHeading.text = card.title
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image  = itemView.findViewById(R.id.image) as ImageView
        val pageHeading  = itemView.findViewById(R.id.pageHeading) as TextView
        val subheading  = itemView.findViewById(R.id.subheading) as TextView

    }
}
